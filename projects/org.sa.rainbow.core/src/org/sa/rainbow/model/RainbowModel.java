/**
 * Created April 7, 2006
 */
package org.sa.rainbow.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.acmestudio.acme.core.IAcmeObject;
import org.acmestudio.acme.core.exception.AcmeException;
import org.acmestudio.acme.core.exception.AcmeIllegalNameException;
import org.acmestudio.acme.core.globals.ExpressionOperator;
import org.acmestudio.acme.core.resource.IAcmeResource;
import org.acmestudio.acme.core.resource.ParsingFailureException;
import org.acmestudio.acme.core.type.IAcmeStringValue;
import org.acmestudio.acme.element.IAcmeElement;
import org.acmestudio.acme.element.IAcmeElementInstance;
import org.acmestudio.acme.element.IAcmeFamily;
import org.acmestudio.acme.element.IAcmeSystem;
import org.acmestudio.acme.element.IAcmeDesignRule.DesignRuleType;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.element.property.IAcmePropertyValue;
import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.acme.model.command.IAcmeElementCopyCommand;
import org.acmestudio.acme.model.command.IAcmeFamilyCreateCommand;
import org.acmestudio.acme.model.command.IAcmeSystemCreateCommand;
import org.acmestudio.acme.rule.node.IExpressionNode;
import org.acmestudio.acme.rule.node.NumericBinaryRelationalExpressionNode;
import org.acmestudio.acme.rule.node.NumericLiteralNode;
import org.acmestudio.acme.rule.node.ReferenceNode;
import org.acmestudio.acme.type.IAcmeTypeChecker;
import org.acmestudio.acme.type.verification.SynchronousTypeChecker;
import org.acmestudio.basicmodel.core.AcmeStringValue;
import org.acmestudio.basicmodel.element.AcmeComponent;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.standalone.environment.StandaloneEnvironment;
import org.acmestudio.standalone.environment.StandaloneEnvironment.TypeCheckerType;
import org.acmestudio.standalone.resource.StandaloneLanguagePackHelper;
import org.acmestudio.standalone.resource.StandaloneResourceProvider;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.monitor.TargetSystem.StatType;
import org.sa.rainbow.stitch.model.ModelRepository;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.Util;

/**
 * The model object shared by the various running components of Rainbow. Each component holds a reference to model to
 * change/query it. By design, only the RainbowModel Manager gets to modify the RainbowModel, while the others can only
 * read it. This "RainbowModel" encapsulates the system architectural model as well as the environment model.
 * 
 * [10-Apr-2006] Note: The simulation system model is hardcoded into this class.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class RainbowModel implements Model, ModelRepository {

	public static final String EXP_AVG_KEY = "[EAvg]";
	public static final String PENALTY_KEY = "[Penalty]";

	private RainbowLogger m_logger = null;
	/** Acme architectural model */
	protected IAcmeModel m_acme = null;
	/** Acme System instance from the model, convenience reference */
	protected IAcmeSystem m_acmeSys = null;
	/** Acme-based target system-environment model */
	protected IAcmeModel m_tgtEnv = null;
	/**
	 * Acme System instance from the Acme-based environment model, convenience reference
	 */
	protected IAcmeSystem m_tgtEnvSys = null;
	/** Map of qualified name to average values */
	protected Map<String, Double> m_propExpAvg = null;
	/** Map of additional, non-model properties */
	protected Map<String, Double> m_moreProp = null;
	/** Map of paths to IAcmeResources to make sure the same is retrieved */
	protected Map<String, IAcmeResource> m_path2Resource = null;
	/** Map of IAcmeResources to snapshot IAcmeResources to reduce redundancies */
	protected Map<IAcmeModel, IAcmeModel> m_model2Snapshot = null;
	/** Flag indicating whether any property has changed value */
	protected boolean m_propChanged = false;
	/** Flag indicating whether a constraint violation has occurred */
	protected boolean m_constraintViolated = false;
	/**
	 * Flag indicating whether an adaptation is advisable, though not strictly required
	 */
	protected boolean m_adaptationAdvised = false;

	/** Keep copy of standalone environment, so we know when we've disposed one */
	private StandaloneEnvironment m_acmeEnv = null;
	/** Stores a mapping of arch op names to corresponding generic arch operator */
	private Properties m_opMap = null;
	/** Whether to persist model at dispose time, default true */
	private boolean m_preserveModel = true;

	/**
	 * Default constructor
	 */
	public RainbowModel() {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_acmeEnv = StandaloneEnvironment.instance();
		m_opMap = new Properties();
		m_propExpAvg = new HashMap<String, Double>();
		m_moreProp = new HashMap<String, Double>();
		m_path2Resource = new HashMap<String, IAcmeResource>();
		m_model2Snapshot = new HashMap<IAcmeModel, IAcmeModel>();
		if (Rainbow.property(Rainbow.PROPKEY_MODEL_PERSIST) != null) {
			m_preserveModel = Boolean.valueOf(Rainbow.property(Rainbow.PROPKEY_MODEL_PERSIST));
		}
		// set up Acme standalone env't AND synchronous typechecker
		m_acmeEnv.useTypeChecker(TypeCheckerType.SYNCHRONOUS);

		try {
			// load Acme model
			File modelDir = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
					.property(Rainbow.PROPKEY_MODEL_PATH));
			// set the family search path to the directory containing model file
			m_acmeEnv.setProperty(StandaloneResourceProvider.FAMILY_SEARCH_PATH, modelDir.getParent());
			String modelPath = modelDir.toString();
			m_acme = (IAcmeModel) getModelForResource(modelPath);
			// add constraints to the loaded Acme Model
			addScenariosConstraintsToAcmeModel();
			m_acmeEnv.getTypeChecker().registerModel(m_acme);
			m_acmeSys = m_acme.getSystems().iterator().next();
		} catch (IOException e) {
			m_logger.error("Get Acme model resource failed!", e);
		}

		try { // load Acme Target-Environment model
			String envModelPath = Rainbow.property(Rainbow.PROPKEY_ENV_MODEL_PATH, Rainbow.DEFAULT_ENV_MODEL);
			File envModelDir = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), envModelPath);
			// set the family search path to the directory containing model file
			StandaloneEnvironment.instance().setProperty(StandaloneResourceProvider.FAMILY_SEARCH_PATH,
					envModelDir.getParent());
			envModelPath = envModelPath.toString();
			m_tgtEnv = (IAcmeModel) getModelForResource(envModelPath);
			m_acmeEnv.getTypeChecker().registerModel(m_tgtEnv);
			m_tgtEnvSys = m_tgtEnv.getSystems().iterator().next();
		} catch (IOException e) {
			m_logger.error("Get Acme Target-Environment model resource failed!", e);
		}

		try { // load archOp map
			String opPath = Util.getRelativeToPath(Rainbow.instance().getTargetPath(),
					Rainbow.property(Rainbow.PROPKEY_OP_MAP_PATH)).toString();
			FileInputStream fileIn = new FileInputStream(opPath);
			m_opMap.load(fileIn);
			fileIn.close();
		} catch (IOException e) {
			m_logger.error("Get archOp mapping failed!", e);
		}

		if (m_logger.isTraceEnabled()) {
			// test model
			IAcmeSystem sys = m_acme.getSystems().iterator().next();
			for (IAcmeObject o : sys.getChildren()) {
				m_logger.trace("Got Acme Object " + ((IAcmeElement) o).getQualifiedName());
			}
		}

		Oracle.instance().writeManagerPanel(m_logger, "Rainbow Model initialized!");
	}

	/**
	 * Take the scenarios data to augment the Acme model.
	 * 
	 * @param modelPath
	 */
	private void addScenariosConstraintsToAcmeModel() {
		// TODO estos 5 datos deberian leerse del ScenarioSpec
		String systemName = "ZNewsSys";
		String artifactName = "c1";
		int maxResponseTime = 5;
		String artifactProperty = "experRespTime";
		ExpressionOperator operator = ExpressionOperator.GREATER_OR_EQUAL_OP;

		// TODO el nombre de la regla lo seteamos nosotros tomando el nombre del scenario?
		String ruleName = "clientePerformanceRule";

		AcmeComponent client1 = (AcmeComponent) m_acme.getSystem(systemName).getComponent(artifactName);
		if (client1 == null) {
			throw new RuntimeException("No se encontro el componente " + artifactName);
		}
		m_logger.info("Se encontro el componente " + artifactName);
		// Hasta aca funciona
		AcmeDesignRule createdRule = null;
		try {
			createdRule = client1.createDesignRule(ruleName);
		} catch (AcmeIllegalNameException e) {
			m_logger.error("Error creating rule from Scenario", e);
			throw new RuntimeException(e);
		}
		// TODO siempre es invariant o puede ser heuristic?
		createdRule.setDesignRuleType(DesignRuleType.INVARIANT);
		NumericBinaryRelationalExpressionNode expression = new NumericBinaryRelationalExpressionNode(client1
				.getContext());
		// TODO le ponemos FLOAT siempre y listo?
		expression.setExpressionType(NumericBinaryRelationalExpressionNode.INT_TYPE);
		ReferenceNode first = new ReferenceNode(null);
		first.setReference(Arrays.asList("self", artifactProperty));
		expression.setFirstChild(first);
		expression.setOperator(operator);
		IExpressionNode second = new NumericLiteralNode(maxResponseTime, client1.getContext());
		expression.setSecondChild(second);
		createdRule.setDesignRuleExpression(expression);
	}

	/**
	 * Returns from the given element the value of the property named by {@linkplain Model.PROPKEY_LOCATION}, converted
	 * to lowercase.
	 * 
	 * @param element
	 *            the Acme element whose location to retrieve
	 * @return String the string value of the host location
	 */
	public static String getElementLocation(IAcmeElementInstance<?, ?> element) {
		String location = null;
		IAcmeProperty prop = element.getProperty(Model.PROPKEY_LOCATION);
		if (prop != null) {
			location = ((AcmeStringValue) prop.getValue()).getValue();
		}
		return location.toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		m_acmeEnv.getTypeChecker().deregisterModel(m_acme);
		// dispose of all known resource models
		for (IAcmeResource ar : m_path2Resource.values()) {
			if (m_preserveModel) {
				try {
					StandaloneResourceProvider.instance().saveAcmeResource(ar);
				} catch (Exception e) {
					m_logger.error("Failed saving AcmeResource!", e);
				}
			}
			StandaloneResourceProvider.instance().releaseResource(ar);
		}
		m_path2Resource.clear();
		m_path2Resource = null;
		m_acme = null;
		m_acmeSys = null;

		// dispose of the Acme standalone env't instance
		m_acmeEnv.dispose();
		m_acmeEnv = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed() {
		return m_acmeEnv == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.stitch.core.ModelRepository#getModelForResource(java.lang.String)
	 */
	public Object getModelForResource(String resName) throws IOException {
		IAcmeResource ar = m_path2Resource.get(resName);
		if (ar == null) {
			try {
				ar = StandaloneResourceProvider.instance().acmeResourceForString(resName);
				m_path2Resource.put(resName, ar);
			} catch (ParsingFailureException e) {
				m_logger.error("Failed acquiring model from " + resName, e);
			}
		}
		return ar.getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.stitch.model.ModelRepository#getSnapshotModel(java.lang.Object)
	 */
	public Object getSnapshotModel(Object modelObj) {
		IAcmeModel snapshot = null;
		if (modelObj instanceof IAcmeModel) {
			IAcmeModel model = (IAcmeModel) modelObj;
			// look for corresponding snapshot model
			snapshot = m_model2Snapshot.get(model);
			if (snapshot == null) {
				// create a temporary file and generate a new model, then copy
				// model elements
				String prefix = "tmpSnapshot_" + System.currentTimeMillis() + "-";
				try {
					File f = File.createTempFile(prefix, ".acme");
					f.deleteOnExit();
					Object snapshotObj = getModelForResource(f.getCanonicalPath());
					if (snapshotObj instanceof IAcmeModel) {
						snapshot = (IAcmeModel) snapshotObj;
						m_model2Snapshot.put(model, snapshot);
					}
				} catch (IOException e) {
					m_logger.error("Failed to create temporary file for model snapshot!", e);
				}
			} else {
				// clear all families and systems
				snapshot.getFamilies().clear();
				snapshot.getSystems().clear();
			}
			// populate snapshot model by copying elements from source model
			// first, the families
			for (IAcmeFamily fam : model.getFamilies()) {
				IAcmeFamilyCreateCommand createCmd = snapshot.getCommandFactory().familyCreateCommand(fam.getName());
				try {
					IAcmeFamily snapshotFam = createCmd.execute();
					IAcmeElementCopyCommand copyCmd = snapshot.getCommandFactory().copyElementCommand(snapshotFam, fam);
					copyCmd.execute();
				} catch (IllegalStateException e) {
					m_logger.error("Failed to create snapshot family for " + fam.getName(), e);
				} catch (AcmeException e) {
					m_logger.error("Failed to create snapshot family for " + fam.getName(), e);
				}
			}
			// next the systems
			for (IAcmeSystem sys : model.getSystems()) {
				IAcmeSystemCreateCommand createCmd = snapshot.getCommandFactory().systemCreateCommand(sys.getName());
				try {
					IAcmeSystem snapshotSys = createCmd.execute();
					IAcmeElementCopyCommand copyCmd = snapshot.getCommandFactory().copyElementCommand(snapshotSys, sys);
					copyCmd.execute();
				} catch (IllegalStateException e) {
					m_logger.error("Failed to create snapshot system for " + sys.getName(), e);
				} catch (AcmeException e) {
					m_logger.error("Failed to create snapshot system for " + sys.getName(), e);
				}
			}
		}
		return snapshot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.stitch.model.ModelRepository#markDisruption(double)
	 */
	public void markDisruption(double level) {
		m_moreProp.put(PENALTY_KEY + "Disruption", level);
		m_logger.info("X_X disruption marked: " + level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.stitch.model.ModelRepository#tacticExecutionHistoryFile()
	 */
	public File tacticExecutionHistoryFile() {
		File profPath = null;
		String pathStr = Rainbow.property(Rainbow.PROPKEY_TACTIC_PROFILE_PATH);
		if (pathStr != null) {
			profPath = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), pathStr);
		}
		return profPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#getAcmeModel()
	 */
	public IAcmeModel getAcmeModel() {
		return m_acme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#getEnvModel()
	 */
	public IAcmeModel getEnvModel() {
		return m_tgtEnv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#getProperty(java.lang.String)
	 */
	public Object getProperty(String id) {
		return internalGetProperty(id, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#predictProperty(java.lang.String, long)
	 */
	public Object predictProperty(String id, long dur) {
		return internalGetProperty(id, dur);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#getStringProperty(java.lang.String)
	 */
	public String getStringProperty(String id) {
		String propVal = null;
		IAcmeProperty prop = (IAcmeProperty) internalGetProperty(id, 0);
		if (prop != null && prop.getValue() instanceof IAcmeStringValue) {
			propVal = ((AcmeStringValue) prop.getValue()).getValue();
		}
		return propVal;
	}

	/**
	 * Gets current or predicted property, depending on whether prediction is enabled and future duration is greater
	 * than 0.
	 * 
	 * @param id
	 *            the identifier fo the property to get value for
	 * @param dur
	 *            duration into the future to predict value, if applicable
	 * @return the Object that represents the value of the sought property.
	 */
	private Object internalGetProperty(String id, long dur) {
		if (m_acme == null) {
			return null;
		}

		Object prop = null;
		if (id.startsWith(EXP_AVG_KEY)) { // special treatment for exp.avg
			// the property ID is expected to be of form <elem-type>.<prop>
			int idxStart = EXP_AVG_KEY.length();
			int idxDot = id.indexOf(".");
			if (idxDot == -1) { // property ID not of expected form
				m_logger.error("Unrecognized form of Average Property name! " + id);
				return null;
			}
			String typeName = id.substring(idxStart, idxDot);
			String propName = id.substring(idxDot + 1);

			// algorithm:
			// 1. find props of all instances of specified element type
			Set<String> propKeys = collectInstanceProps(typeName, propName);
			if (propKeys.size() > 0) {
				// 2. collect the exp.avg values
				double sum = 0.0;
				for (String k : propKeys) {
					if (Rainbow.predictionEnabled() && dur > 0L) {
						// grab predicted value from the "target system"
						sum += (Double) Oracle.instance().targetSystem().predictProperty(k, dur, StatType.SINGLE);
					} else {
						sum += m_propExpAvg.get(k);
					}
				}
				// 3. take the mean over those exp.avg values
				prop = Double.valueOf(sum / propKeys.size());
			}
			if (m_logger.isTraceEnabled()) {
				m_logger.trace("ExpAvg Prop " + id + (dur > 0 ? "(+" + dur + ") " : "") + " requested == " + prop);
			}
		} else if (id.startsWith(PENALTY_KEY)) {
			prop = m_moreProp.get(id);
		} else {
			prop = m_acme.findNamedObject(m_acme, id);
		}
		return prop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#updateProperty(java.lang.String, java.lang.Object)
	 */
	public void updateProperty(String iden, Object value) {
		if (m_acme == null) {
			return;
		}

		Object obj = m_acme.findNamedObject(m_acme, iden);
		if (obj instanceof IAcmeProperty) {
			IAcmeProperty prop = (IAcmeProperty) obj;
			if (m_logger.isDebugEnabled()) {
				m_logger.debug("Upd prop: " + prop.getQualifiedName() + " = " + value.toString());
			}
			try {
				IAcmePropertyValue pVal = StandaloneLanguagePackHelper.defaultLanguageHelper().propertyValueFromString(
						value.toString(), null);
				IAcmeCommand<?> cmd = m_acmeSys.getCommandFactory().propertyValueSetCommand(prop, pVal);
				cmd.execute();
				m_propChanged = true;

				if (pVal.getType() != null) {
					if (pVal.getType().getName().equals("float") || pVal.getType().getName().equals("int")) {
						// update exponential average
						updateExponentialAverage(iden, Double.parseDouble(value.toString()));
					}
				}
			} catch (Exception e) {
				m_logger.error("Acme Command execution failed!", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#hasPropertyChanged()
	 */
	public boolean hasPropertyChanged() {
		return m_propChanged;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#clearPropertyChanged()
	 */
	public void clearPropertyChanged() {
		m_propChanged = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#evaluateConstraints()
	 */
	public void evaluateConstraints() {
		if (m_acmeEnv == null) {
			return;
		}

		IAcmeTypeChecker typechecker = m_acmeEnv.getTypeChecker();
		if (typechecker instanceof SynchronousTypeChecker) {
			SynchronousTypeChecker synchChecker = (SynchronousTypeChecker) typechecker;
			synchChecker.typecheckAllModelsNow();
			m_constraintViolated = !synchChecker.typechecks(m_acmeSys);
			if (m_constraintViolated) {
				Set<?> errors = m_acmeEnv.getAllRegisteredErrors();
				Oracle.instance().writeEvaluatorPanel(m_logger, errors.toString());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#isConstraintViolated()
	 */
	public boolean isConstraintViolated() {
		return m_constraintViolated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.model.Model#clearConstraintViolated()
	 */
	public void clearConstraintViolated() {
		m_constraintViolated = false;
	}

	public String getGenericOperatorName(String archOpName) {
		return m_opMap.getProperty(archOpName);
	}

	private void updateExponentialAverage(String iden, double val) {
		double avg = 0.0;
		// retrieve the exponential alpha
		double alpha = Double.parseDouble(Rainbow.property(Rainbow.PROPKEY_MODEL_ALPHA));
		if (m_propExpAvg.containsKey(iden)) {
			avg = m_propExpAvg.get(iden);
			// compute the new exponential average value
			avg = (1 - alpha) * avg + alpha * val;
		} else { // assign first value as the starting point for average
			avg = val;
		}
		if (m_logger.isTraceEnabled()) {
			m_logger.trace("(iden,val,alpha,avg) == (" + iden + "," + val + "," + alpha + "," + avg + ")");
		}
		// store new/updated exp.avg value
		m_propExpAvg.put(iden, avg);
	}

	/**
	 * Iterates through model's systems to collect all instances that either declares or instantiates the specified type
	 * name, then for all instances found that also have the specified property names, collect the fully qualified
	 * property names
	 * 
	 * @param typeName
	 *            the specified element type name
	 * @param propName
	 *            the specified property name within relevant instance
	 * @return the set of qualified property names
	 */
	private Set<String> collectInstanceProps(String typeName, String propName) {
		Set<String> propKeys = new HashSet<String>();

		// Object elemObj = m_acme.findNamedObject(m_acme, typeName);
		// if (! (elemObj instanceof IAcmeElementType<?,?>)) {
		// Debug.errorln("Element type of Average Property requested NOT found!
		// " + typeName);
		// return propKeys;
		// }
		for (IAcmeSystem sys : m_acme.getSystems()) { // collect instances
			Set<IAcmeElementInstance<?, ?>> children = new HashSet<IAcmeElementInstance<?, ?>>();
			children.addAll(sys.getComponents());
			children.addAll(sys.getConnectors());
			children.addAll(sys.getPorts());
			children.addAll(sys.getRoles());
			for (IAcmeElementInstance<?, ?> child : children) {
				// seek element with specified type AND specified property
				if (child.declaresType(typeName) || child.instantiatesType(typeName)) {
					IAcmeProperty childProp = child.getProperty(propName);
					if (childProp != null) {
						String qName = childProp.getQualifiedName();
						if (m_propExpAvg.containsKey(qName)) {
							propKeys.add(qName);
						}
					}
				}
			}
		}
		return propKeys;
	}

}
