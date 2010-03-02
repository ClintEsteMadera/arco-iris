package ar.uba.dc.thesis.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.instance.BooleanLiteralConstraint;

public final class ScenarioEnvironmentRepository {

	private static final Map<Concern, Float> normalConcernMultiplierMap = new HashMap<Concern, Float>();

	private static final Map<Concern, Float> highLoadConcernMultiplierMap = new HashMap<Concern, Float>();

	// TODO: Include all the other concerns?
	static {
		normalConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.5F);
		normalConcernMultiplierMap.put(Concern.SERVER_LOAD, 0.5F);

		highLoadConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.8F);
		highLoadConcernMultiplierMap.put(Concern.SERVER_LOAD, 0.2F);
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
