package ar.uba.dc.thesis.selfhealing;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

public class StitchParser implements Serializable {

	private static final long serialVersionUID = 1L;

	private static RainbowLogger logger = RainbowLoggerFactory.logger(StitchParser.class);

	private List<Stitch> repertoire = new ArrayList<Stitch>();

	private boolean shouldLog;

	/**
	 * When created using this constructor, there is no logging available.
	 * 
	 * @param stitchDirectory
	 *            the full path to the directory where the .s files are located.
	 */
	public StitchParser(File stitchDirectory) {
		this(stitchDirectory, false);
	}

	/**
	 * 
	 * @param stitchDirectory
	 *            the full path to the directory where the .s files are located.
	 * @param shouldLog
	 *            whether this parser should log its actions using Rainbow's standard way of logging (this includes
	 *            using probably Rainbow's GUI)
	 */
	public StitchParser(File stitchDirectory, boolean shouldLog) {
		super();
		this.shouldLog = shouldLog;
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
	public String[] getAllStrategyNames() {
		List<String> strategyNames = new ArrayList<String>();
		for (Stitch stitch : this.repertoire) {
			for (Strategy strategy : stitch.script.strategies) {
				strategyNames.add(strategy.getName());
			}
		}
		return strategyNames.toArray(new String[] {});
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
						// apply attribute vectors to tactics, if available
						defineAttributes(stitch, Rainbow.instance().preferenceDesc().attributeVectors);
						this.repertoire.add(stitch);
						log(Level.DEBUG, "Parsed script " + stitch.path);
					} else {
						log(Level.DEBUG, "Previously known script " + stitch.path);
					}
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
				if (logger.isTraceEnabled())
					log(Level.TRACE, "Found attributes for tactic " + t.getName() + ", saving pairs...");
				for (Map.Entry<String, Object> e : attributes.entrySet()) {
					t.putAttribute(e.getKey(), e.getValue());
					if (logger.isTraceEnabled())
						log(Level.TRACE, " - (" + e.getKey() + ", " + e.getValue() + ")");
				}
			}
		}
	}

	private void log(Level level, String txt, Throwable... t) {
		if (this.shouldLog) {
			Oracle.instance().writeEnginePanel(logger, level, txt, t);
		}
	}
}
