package ar.uba.dc.thesis.repository;

import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.selfhealing.RepairStrategySpecification;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;
import ar.uba.dc.thesis.selfhealing.SelfHealingTradeoff;

public class RepairStrategySpecRepository {

	/**
	 * This strategy negatively affects client's experienced response time.
	 */
	public static RepairStrategySpecification getReduceOverallCostRepairStrategy() {
		List<SelfHealingScenario> affectedScenarios = Collections
				.singletonList(SelfHealingScenarioRepository.CLIENT_RESPONSE_TIME_SCENARIO);
		List<SelfHealingScenario> brokenScenarios = Collections.<SelfHealingScenario> emptyList();

		SelfHealingTradeoff tradeoff = new SelfHealingTradeoff(affectedScenarios, brokenScenarios);

		return new RepairStrategySpecification("reduceOverallCost", new String[] {}, tradeoff);
	}

	/**
	 * This strategy does not affects or breaks any other scenario.
	 */
	public static RepairStrategySpecification getSimpleReduceResponseTimeRepairStrategy() {
		List<SelfHealingScenario> affectedScenarios = Collections.<SelfHealingScenario> emptyList();
		List<SelfHealingScenario> brokenScenarios = Collections.<SelfHealingScenario> emptyList();

		SelfHealingTradeoff tradeoff = new SelfHealingTradeoff(affectedScenarios, brokenScenarios);

		return new RepairStrategySpecification("simpleReduceResponseTime", new String[] {}, tradeoff);
	}
}