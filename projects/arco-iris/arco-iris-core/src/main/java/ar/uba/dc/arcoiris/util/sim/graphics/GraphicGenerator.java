package ar.uba.dc.arcoiris.util.sim.graphics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.scenario.model.ArcoIrisModel;

import ar.uba.dc.arcoiris.rainbow.constraint.Constraint;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.Quantifier;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public class GraphicGenerator extends AbstractRainbowRunnable {

	private ArcoIrisModel rainbowModelWithScenarios;

	private SimGraphicConfiguration simGraphicConfiguration;

	private final VelocityContext context;

	private final Map<String, StringBuffer> pointsPerProperty = new HashMap<String, StringBuffer>();

	private final Map<String, Integer> countPerProperty = new HashMap<String, Integer>();

	private final Map<String, Number> thresholdPerProperty = new HashMap<String, Number>();

	private static VelocityEngine engine;

	private static final String GRAPHICS_OUTPUT_DIR = "graphics_output";

	private final static String OUTPUT = GRAPHICS_OUTPUT_DIR + "/graphic.html";

	private final static String STATIC_OUTPUT = GRAPHICS_OUTPUT_DIR + "/graphic_static.html";

	public GraphicGenerator(ArcoIrisModel rainbowModel) {
		super("Simulation Graphic Generator");
		setSleepTime(500/* ms */);
		this.rainbowModelWithScenarios = rainbowModel;
		initializeGraphicConfiguration();
		initializeThresholds();
		try {
			Velocity.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		context = new VelocityContext();
		context.put("plot", "$.plot");
	}

	public void dispose() {
		// nothing to do
	}

	@Override
	protected void log(String txt) {
		// nothing to do
	}

	@Override
	protected void runAction() {
		graphicPoints(computePoints());
	}

	private List<GraphicPoint> computePoints() {
		List<GraphicPoint> points = new ArrayList<GraphicPoint>();
		for (SimPropertyGraphicConfiguration propGraphicConf : this.simGraphicConfiguration
				.getPropertyGraphicConfiguration()) {
			Number value = (Number) rainbowModelWithScenarios.getProperty(propGraphicConf.getEavgPropertyName());
			points.add(new GraphicPoint(propGraphicConf.getProperty(), value));
		}
		return points;
	}

	private void graphicPoints(List<GraphicPoint> points) {
		for (GraphicPoint point : points) {
			StringBuffer propertyPoints = getPointsFor(point.getProperty());
			addPointIntoArray(point.getProperty(), point.getAvgValue(), propertyPoints);
		}
		try {
			putPointsWithinContext();
			putThresholdsWithinContext();
			writeToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO externalize this configuration into SelfHealingConfiguration or in a another xml.
	 */
	private void initializeGraphicConfiguration() {
		this.simGraphicConfiguration = new SimGraphicConfiguration();
		SimPropertyGraphicConfiguration respTimeGraphicConfig = new SimPropertyGraphicConfiguration("ClientT",
				"experRespTime");
		SimPropertyGraphicConfiguration costGraphicConfig = new SimPropertyGraphicConfiguration("ServerT", "cost",
				Quantifier.SUM);
		simGraphicConfiguration.add(respTimeGraphicConfig);
		simGraphicConfiguration.add(costGraphicConfig);
	}

	private void initializeThresholds() {
		Collection<SelfHealingScenario> enabledScenarios = Oracle.instance().selfHealingConfigurationRepository()
				.getEnabledScenarios();
		for (SelfHealingScenario scenario : enabledScenarios) {
			Constraint constraint = scenario.getResponseMeasure().getConstraint();
			if (constraint instanceof NumericBinaryRelationalConstraint) {
				NumericBinaryRelationalConstraint numericConstraint = ((NumericBinaryRelationalConstraint) constraint);
				Number constantToCompare = numericConstraint.getConstantToCompareThePropertyWith();
				this.thresholdPerProperty.put(numericConstraint.getProperty() + "Threshold", constantToCompare);
			}
		}
	}

	private static VelocityEngine getVelocityEngine() {
		if (engine == null) {
			try {
				Properties props = new Properties();
				props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
				props.setProperty("resource.loader", "class");
				props.setProperty("class.resource.loader.class",
						"ar.uba.dc.arcoiris.util.sim.graphics.ClassResourceLoader");

				engine = new VelocityEngine();
				engine.init(props);
				return engine;
			} catch (Exception exc) {
				throw new RuntimeException(exc);
			}
		}
		return engine;
	}

	private StringBuffer getPointsFor(String property) {
		StringBuffer points = pointsPerProperty.get(property);
		if (points == null) {
			points = new StringBuffer();
			pointsPerProperty.put(property, points);
			countPerProperty.put(property, 0);
		}
		return points;
	}

	private void writeToFile() throws IOException, Exception {
		new File(GRAPHICS_OUTPUT_DIR).mkdir(); // so that FileWriter doesn't fail when the output dir doesn't exist
		Writer writer = new FileWriter(OUTPUT);
		getVelocityEngine().mergeTemplate("graphic.vm", "UTF-8", context, writer);
		writer.close();
		writer = new FileWriter(STATIC_OUTPUT);
		getVelocityEngine().mergeTemplate("graphic_static.vm", "UTF-8", context, writer);
		writer.close();
	}

	private void addPointIntoArray(String property, Number avgValue, StringBuffer points) {
		points.append(property + "Points[");
		points.append(countPerProperty.get(property));
		countPerProperty.put(property, countPerProperty.get(property) + 1);
		points.append("]=[");
		points.append(Oracle.instance().simTime());
		points.append(",");
		points.append(avgValue);
		points.append("];");
	}

	private void putPointsWithinContext() {
		for (Entry<String, StringBuffer> entry : pointsPerProperty.entrySet()) {
			context.put(entry.getKey() + "Points", entry.getValue());
		}
	}

	private void putThresholdsWithinContext() {
		for (Entry<String, Number> entry : thresholdPerProperty.entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
	}
}
