package ar.uba.dc.thesis.znn.sim.graphics;

import java.io.FileWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class GraphicGenerator {

	public void addPoint(double simTime, String iden, Number avgValue) {
		String pointsArrayName = iden + "Points";

		StringBuffer points = pointsPerProperty.get(iden);
		if (points == null) {
			points = new StringBuffer();
			pointsPerProperty.put(iden, points);
			countPerProperty.put(iden, 0);
		}
		try {
			points.append(pointsArrayName + "[");
			points.append(countPerProperty.get(iden));
			countPerProperty.put(iden, countPerProperty.get(iden) + 1);
			points.append("]=[");
			points.append(simTime);
			points.append(",");
			points.append(avgValue);
			points.append("];");
			for (Entry<String, StringBuffer> entry : pointsPerProperty.entrySet()) {
				context.put(entry.getKey() + "Points", entry.getValue());
			}
			String fileName = MessageFormat.format(OUTPUT, iden);
			Writer writer = new FileWriter(fileName);
			getVelocityEngine().mergeTemplate("graphic.vm", "UTF-8", context, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private final VelocityContext context;

	private static GraphicGenerator instance;

	private final Map<String, StringBuffer> pointsPerProperty = new HashMap<String, StringBuffer>();
	private final Map<String, Integer> countPerProperty = new HashMap<String, Integer>();

	private static VelocityEngine engine;

	private final static String OUTPUT = "graphics_output/grafico.html";

}
