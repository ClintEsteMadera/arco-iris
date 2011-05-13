package ar.uba.dc.arcoiris.dao;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.util.Util;

import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.common.FileChangeDetector;
import ar.uba.dc.arcoiris.repository.SelfHealingConfigurationChangeListener;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;
import ar.uba.dc.arcoiris.selfhealing.config.SelfHealingConfiguration;
import ar.uba.dc.arcoiris.selfhealing.config.persistence.SelfHealingConfigurationPersister;

public class FileSelfHealingConfigurationDao implements SelfHealingConfigurationDao {

	private static final long CONFIG_RELOAD_INTERVAL_MS = Long.valueOf(Rainbow
			.property("customize.scenarios.reloadInterval"));

	private static final String SELF_HEALING_CONFIG_FILE_NAME = Rainbow.property("customize.scenarios.path");

	private static final File SELF_HEALING_CONFIG_FILE = Util.getRelativeToPath(Rainbow.instance().getTargetPath(),
			SELF_HEALING_CONFIG_FILE_NAME);

	private static final Log logger = LogFactory.getLog(FileSelfHealingConfigurationDao.class);

	private SelfHealingConfiguration scenariosConfig;

	private final Collection<SelfHealingConfigurationChangeListener> listeners;

	public FileSelfHealingConfigurationDao() {
		super();
		this.listeners = new HashSet<SelfHealingConfigurationChangeListener>();
		this.loadSelfHealingConfigurationFromFile();

		TimerTask task = new FileChangeDetector(SELF_HEALING_CONFIG_FILE) {
			@Override
			protected void onChange(File file) {
				logger.info(SELF_HEALING_CONFIG_FILE_NAME + " has just changed, reloading Self Healing Configuration!");
				loadSelfHealingConfigurationFromFile();
				notifyListeners();
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, new Date(), CONFIG_RELOAD_INTERVAL_MS);
	}

	public List<SelfHealingScenario> getAllScenarios() {
		return this.scenariosConfig.getScenarios();
	}

	public List<Environment> getAllEnvironments() {
		return this.scenariosConfig.getEnvironments();
	}

	public Environment getEnvironment(String name) {
		for (Environment environment : this.getAllEnvironments()) {
			if (environment.getName().equals(name)) {
				return environment;
			}
		}
		throw new RuntimeException("Environment " + name + " not defined.");
	}

	public List<Artifact> getAllArtifacts() {
		return this.scenariosConfig.getArtifacts();
	}

	public void register(SelfHealingConfigurationChangeListener listener) {
		this.listeners.add(listener);
	}

	protected void notifyListeners() {
		for (SelfHealingConfigurationChangeListener listener : this.listeners) {
			listener.selfHealingConfigurationHasChanged();
		}
	}

	private void loadSelfHealingConfigurationFromFile() {
		SelfHealingConfigurationPersister persister = new SelfHealingConfigurationPersister();
		this.scenariosConfig = persister.readFromFile(SELF_HEALING_CONFIG_FILE.getAbsolutePath());
	}
}
