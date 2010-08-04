package ar.uba.dc.thesis.atam.scenario.persist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

public class SelfHealingConfigurationPersister {

	private static final String CHARSET = "UTF-8";

	private static final Log logger = LogFactory.getLog(SelfHealingConfigurationPersister.class);

	private XStream xstream;

	public SelfHealingConfigurationPersister() {
		super();
		this.initXStream();
	}

	public SelfHealingConfiguration readFromFile(String selfHealingConfigXmlFullPath) {
		try {
			logger.info("Loading Self Healing Configuration from " + selfHealingConfigXmlFullPath + "...");
			String scenariosInXml = FileUtils.readFileToString(new File(selfHealingConfigXmlFullPath), CHARSET);
			SelfHealingConfiguration configLoadedFromXML = (SelfHealingConfiguration) this.xstream
					.fromXML(scenariosInXml);
			logger.info("Self Healing Configuration successfully loaded from " + selfHealingConfigXmlFullPath);

			this.preventNullLists(configLoadedFromXML);

			return configLoadedFromXML;
		} catch (IOException e) {
			throw new RuntimeException("Cannot load Self Healing Configuration from " + selfHealingConfigXmlFullPath, e);
		} catch (XStreamException xstreamException) {
			logger.error("Error while unmarshaling scenarios", xstreamException);
			throw xstreamException;

		}
	}

	public void saveToFile(SelfHealingConfiguration selfHealingConfig, String fileFullPath) {
		String xml = this.xstream.toXML(selfHealingConfig);
		try {
			FileUtils.writeStringToFile(new File(fileFullPath), xml, CHARSET);
			logger.info("Self Healing Configuration successfully saved to " + fileFullPath);
		} catch (IOException e) {
			throw new RuntimeException("Cannot save the Scenario in " + fileFullPath);
		}
	}

	/**
	 * In the event the scenarios, the environments or the artifacts are null, this method initializes them with an
	 * empty list in order to preserve the invariant of never having a null list of those properties.
	 * 
	 * @param configLoadedFromXML
	 *            a selfHealingConfiguration loaded from XML
	 */
	private void preventNullLists(SelfHealingConfiguration configLoadedFromXML) {
		if (configLoadedFromXML.getScenarios() == null) {
			configLoadedFromXML.setScenarios(new ArrayList<SelfHealingScenario>());
		}
		if (configLoadedFromXML.getEnvironments() == null) {
			configLoadedFromXML.setEnvironments(new ArrayList<Environment>());
		}
		if (configLoadedFromXML.getArtifacts() == null) {
			configLoadedFromXML.setArtifacts(new ArrayList<Artifact>());
		}
	}

	private void initXStream() {
		this.xstream = new XStream();
		this.xstream.autodetectAnnotations(true);
		this.xstream.alias("selfHealingConfiguration", SelfHealingConfiguration.class);
		this.xstream.alias("numericBinaryRelationalConstraint", NumericBinaryRelationalConstraint.class);
		this.xstream.alias("defaultEnvironment", DefaultEnvironment.class);
		this.xstream.registerConverter(new DefaultEnvironmentConverter());
	}
}