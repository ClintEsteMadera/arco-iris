package ar.uba.dc.thesis.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.instance.BooleanLiteralConstraint;

public final class ScenarioEnvironmentRepository {

	private static final Map<Concern, Double> normalConcernMultiplierMap = new HashMap<Concern, Double>();

	private static final Map<Concern, Double> highLoadConcernMultiplierMap = new HashMap<Concern, Double>();

	// TODO: Include all the other concerns?
	static {
		normalConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.5D);
		normalConcernMultiplierMap.put(Concern.SERVER_LOAD, 0.5D);

		highLoadConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.8D);
		highLoadConcernMultiplierMap.put(Concern.SERVER_LOAD, 0.2D);
	}

	public static final Environment NORMAL = new Environment("NORMAL", Collections
			.singletonList(BooleanLiteralConstraint.TRUE), normalConcernMultiplierMap);

	public static final Environment HIGH_LOAD = new Environment("HIGH LOAD", Collections
			.singletonList(BooleanLiteralConstraint.TRUE), highLoadConcernMultiplierMap);

	/**
	 * This class is not intended to be extended.
	 */
	private ScenarioEnvironmentRepository() {
		super();
	}
}
