package ar.uba.dc.thesis.znn.sim.graphics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.sa.rainbow.core.Oracle;

public class GraphicGenerator {

	public synchronized void addPoint(String property, Number avgValue) {
		StringBuffer points = getPointsFor(property);
		try {
			addCurrentPointIntoArray(property, avgValue, points);
			putPointsWithinContext();
			putThresholdsWithinContext();
			write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setThreshold(String property, Number constant) {
		this.thresholdPerProperty.put(property + "Threshold", constant);
	}

	public static GraphicGenerator getInstance() {
		if (instance == null) {
			try {
				instance = new GraphicGenerator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	private GraphicGenerator() throws Exception {
		super();
		Velocity.init();
		context = new VelocityContext();
		context.put("plot", "$.plot");
	}

	private static VelocityEngine getVelocityEngine() {
		if (engine == null) {
			try {
				Properties props = new Properties();
				props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
				props.setProperty("resource.loader", "class");
				props.setProperty("class.resource.loader.class",
						"ar.uba.dc.thesis.znn.sim.graphics.ClassResourceLoader");

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

	private void write() throws IOException, Exception {
		Writer writer = new FileWriter(OUTPUT);
		getVelocityEngine().mergeTemplate("graphic.vm", "UTF-8", context, writer);
		writer.close();
		writer = new FileWriter(STATIC_OUTPUT);
		getVelocityEngine().mergeTemplate("graphic_static.vm", "UTF-8", context, writer);
		writer.close();
	}

	private void addCurrentPointIntoArray(String property, Number avgValue, StringBuffer points) {
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

	private final VelocityContext context;

	private static GraphicGenerator instance;

	private final Map<String, StringBuffer> pointsPerProperty = new HashMap<String, StringBuffer>();
	private final Map<String, Integer> countPerProperty = new HashMap<String, Integer>();
	private final Map<String, Number> thresholdPerProperty = new HashMap<String, Number>();

	private static VelocityEngine engine;

	private final static String OUTPUT = "graphics_output/graphic.html";

	private final static String STATIC_OUTPUT = "graphics_output/graphic_static.html";

}
