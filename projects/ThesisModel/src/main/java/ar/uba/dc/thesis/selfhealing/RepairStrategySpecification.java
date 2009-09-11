package ar.uba.dc.thesis.selfhealing;


public class RepairStrategySpecification {

	private final String repairStrategyName;

	private final String[] params;

	private final SelfHealingTradeoff selfHealingTradeoff;

	public RepairStrategySpecification(String repairStrategyName, String[] params,
			SelfHealingTradeoff selfHealingTradeoff) {
		super();
		this.repairStrategyName = repairStrategyName;
		this.params = params;
		this.selfHealingTradeoff = selfHealingTradeoff;
	}

	public String getRepairStrategyName() {
		return this.repairStrategyName;
	}

	public String[] getParams() {
		return this.params;
	}

	public SelfHealingTradeoff getSelfHealingTradeoff() {
		return this.selfHealingTradeoff;
	}
}