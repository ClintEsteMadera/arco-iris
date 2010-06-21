package ar.uba.dc.thesis.atam.scenario.persist;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

public class SelfHealingScenarioPersister {

	private static final String CHARSET = "UTF-8";

	private static final Log logger = LogFactory.getLog(SelfHealingScenarioPersister.class);

	private XStream xstream;

	public SelfHealingScenarioPersister() {
		super();
		this.initXStream();
	}

	public void saveToFile(SelfHealingConfiguration selfHealingConfig, String fileFullPath) {
		String xml = this.xstream.toXML(selfHealingConfig);
		try {
			FileUtils.writeStringToFile(new File(fileFullPath), xml, CHARSET);
			logger.info("Scenarios successfully saved to " + fileFullPath);
		} catch (IOException e) {
			throw new RuntimeException("Cannot save the Scenario in " + fileFullPath);
		}
	}

	public SelfHealingConfiguration readFromFile(String scenariosInXmlFullPath) {
		try {
			logger.info("Loading Scenarios from " + scenariosInXmlFullPath + "...");
			String scenariosInXml = FileUtils.readFileToString(new File(scenariosInXmlFullPath), CHARSET);
			logger.info("Scenarios successfully loaded from " + scenariosInXmlFullPath);
			return (SelfHealingConfiguration) this.xstream.fromXML(scenariosInXml);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load Scenarios from " + scenariosInXmlFullPath, e);
		} catch (XStreamException xstreamException) {
			logger.error("Error while unmarshaling scenarios");
			throw xstreamException;

		}
	}

	private void initXStream() {
		this.xstream = new XStream();
		this.xstream.autodetectAnnotations(true);
		addAlias(SelfHealingConfiguration.class);
		addAlias(NumericBinaryRelationalConstraint.class);
	}

	private void addAlias(Class<?> aClass) {
		this.xstream.alias(classToAlias(aClass), aClass);
	}

	private String classToAlias(Class<?> aClass) {
		String simpleName = aClass.getSimpleName();
		return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
	}
}
