package ar.uba.dc.thesis.znn.sim.graphics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class GraphicGenerator extends AbstractRainbowRunnable {

	private RainbowModelWithScenarios rainbowModelWithScenarios;

	@Override
	protected void runAction() {
		this.addPoints(getCostPoint(), getRespTimePoint());
	}

	private GraphicPoint getCostPoint() {
		List<Number> values = rainbowModelWithScenarios.getAllInstancesPropertyValues("ZNewsSys", "ServerT", "cost");
		double sum = NumberUtils.DOUBLE_ZERO;
		for (Number number : values) {
			sum += number.doubleValue();
		}
		GraphicPoint costPoint = new GraphicPoint("cost", sum);
		return costPoint;
	}

	private GraphicPoint getRespTimePoint() {
		List<Number> values = rainbowModelWithScenarios.getAllInstancesPropertyValues("ZNewsSys", "ClientT",
				"experRespTime");
		double sum = NumberUtils.DOUBLE_ZERO;
		for (Number number : values) {
			sum += number.doubleValue();
		}
		Number average = values.isEmpty() ? sum : sum / values.size();
		GraphicPoint costPoint = new GraphicPoint("experRespTime", average);
		return costPoint;
	}

	public void dispose() {
		// nothing to do
	}

	@Override
	protected void log(String txt) {
		// nothing to do
	}

	private void addPoints(GraphicPoint... points) {
		for (GraphicPoint point : points) {
			StringBuffer propertyPoints = getPointsFor(point.getProperty());
			addCurrentPointIntoArray(point.getProperty(), point.getAvgValue(), propertyPoints);
		}
		try {
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

	public GraphicGenerator(RainbowModelWithScenarios rainbowModel) {
		super("Simulation Graphic Generator");
		setSleepTime(500/* ms */);
		this.rainbowModelWithScenarios = rainbowModel;
		initializeThresholds();
		try {
			Velocity.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		context = new VelocityContext();
		context.put("plot", "$.plot");
	}

	private void initializeThresholds() {
		Collection<SelfHealingScenario> enabledScenarios = Oracle.instance().selfHealingConfigurationRepository()
				.getEnabledScenarios();
		for (SelfHealingScenario scenario : enabledScenarios) {
			Constraint constraint = scenario.getResponseMeasure().getConstraint();
			if (constraint instanceof NumericBinaryRelationalConstraint) {
				NumericBinaryRelationalConstraint numericConstraint = ((NumericBinaryRelationalConstraint) constraint);
				Number constantToCompareThePropertyWith = numericConstraint.getConstantToCompareThePropertyWith();
				this.setThreshold(numericConstraint.getProperty(), constantToCompareThePropertyWith);
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

	private final Map<String, StringBuffer> pointsPerProperty = new HashMap<String, StringBuffer>();
	private final Map<String, Integer> countPerProperty = new HashMap<String, Integer>();
	private final Map<String, Number> thresholdPerProperty = new HashMap<String, Number>();

	private static VelocityEngine engine;

	private final static String OUTPUT = "graphics_output/graphic.html";

	private final static String STATIC_OUTPUT = "graphics_output/graphic_static.html";

}
