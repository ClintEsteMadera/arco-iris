package ar.uba.dc.thesis.selfhealing;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.stitch.Ohana;
import org.sa.rainbow.stitch.core.Strategy;
import org.sa.rainbow.stitch.core.Tactic;
import org.sa.rainbow.stitch.error.DummyStitchProblemHandler;
import org.sa.rainbow.stitch.visitor.Stitch;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

public class StitchLoader implements Serializable {

	private static final long serialVersionUID = 1L;

	private static RainbowLogger rainbowLogger = RainbowLoggerFactory.logger(StitchLoader.class);

	private List<Stitch> repertoire = new ArrayList<Stitch>();

	private boolean rainbowClient;

	/**
	 * 
	 * @param stitchDirectory
	 *            the full path to the directory where the .s files are located.
	 * @param rainbowClient
	 *            this flag specifies that the client of this loader is Rainbow. This will affect some functionalities
	 *            within the loader as, for example, the event logging.
	 */
	public StitchLoader(File stitchDirectory, boolean rainbowClient) {
		super();
		this.rainbowClient = rainbowClient;
		this.initAdaptationRepertoire(stitchDirectory);
	}

	/**
	 * @return the list of parsed stitches
	 */
	public List<Stitch> getParsedStitches() {
		return this.repertoire;
	}

	/**
	 * @return a list with the names of all the parsed strategies
	 */
	public List<String> getAllStrategyNames() {
		List<String> strategyNames = new ArrayList<String>();
		for (Strategy strategy : this.getAllStrategies()) {
			strategyNames.add(strategy.getName());
		}
		return strategyNames;
	}

	/**
	 * @return a list with all the parsed strategies (using a transfer object with a subset of fields)
	 */
	public List<StrategyTO> getAllStrategiesTO() {
		List<StrategyTO> strategiesTO = new ArrayList<StrategyTO>();
		for (Stitch stitch : this.repertoire) {
			for (Strategy strategy : stitch.script.strategies) {
				strategiesTO.add(new StrategyTO(strategy.getName(), strategy.nodes.values(), strategy.vars().values()));
			}
		}
		return strategiesTO;
	}

	/**
	 * @return a list with all the parsed strategies
	 */
	private List<Strategy> getAllStrategies() {
		List<Strategy> strategies = new ArrayList<Strategy>();
		for (Stitch stitch : this.repertoire) {
			strategies.addAll(stitch.script.strategies);
		}
		return strategies;
	}

	/**
	 * Retrieves the adaptation repertoire; for each tactic, store the respective tactic attribute vectors.
	 * 
	 * @param stitchDirectory
	 */
	private void initAdaptationRepertoire(File stitchPath) {
		if (stitchPath.exists() && stitchPath.isDirectory()) {
			FilenameFilter ff = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".s");
				}
			};
			for (File file : stitchPath.listFiles(ff)) {
				try {
					// don't duplicate loading of script files
					Stitch stitch = Ohana.instance().findStitch(file.getCanonicalPath());
					if (stitch == null) {
						stitch = Stitch.newInstance(file.getCanonicalPath(), new DummyStitchProblemHandler());
						Ohana.instance().parseFile(stitch);
						Map<String, Map<String, Object>> attributeVectorMap = Collections.emptyMap();
						if (this.rainbowClient) {
							attributeVectorMap = Rainbow.instance().preferenceDesc().attributeVectors;
						}
						// apply attribute vectors to tactics, if available
						defineAttributes(stitch, attributeVectorMap);
						log(Level.DEBUG, "Parsed script " + stitch.path);
					} else {
						log(Level.DEBUG, "Previously known script " + stitch.path);
					}
					this.repertoire.add(stitch);
				} catch (IOException e) {
					log(Level.ERROR, "Obtaining file canonical path failed! " + file.getName(), e);
				}
			}
		}
	}

	private void defineAttributes(Stitch stitch, Map<String, Map<String, Object>> attrVectorMap) {
		for (Tactic t : stitch.script.tactics) {
			Map<String, Object> attributes = attrVectorMap.get(t.getName());
			if (attributes != null) {
				// found attribute def for tactic, save all key-value pairs
				if (rainbowLogger.isTraceEnabled())
					log(Level.TRACE, "Found attributes for tactic " + t.getName() + ", saving pairs...");
				for (Map.Entry<String, Object> e : attributes.entrySet()) {
					t.putAttribute(e.getKey(), e.getValue());
					if (rainbowLogger.isTraceEnabled())
						log(Level.TRACE, " - (" + e.getKey() + ", " + e.getValue() + ")");
				}
			}
		}
	}

	private void log(Level level, String txt, Throwable... t) {
		if (this.rainbowClient) {
			Oracle.instance().writeEnginePanel(rainbowLogger, level, txt, t);
		} else {
			if (t.length > 0) {
				rainbowLogger.log(level, txt, t[0]);
			} else {
				rainbowLogger.log(level, txt);
			}
		}
	}
}
