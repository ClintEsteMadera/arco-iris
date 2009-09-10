package ar.uba.dc.thesis.selfhealing;

import ar.uba.dc.thesis.atam.Artifact;

public class DummyRepairStrategy extends RepairStrategy {

	private static final DummyRepairStrategy instance = new DummyRepairStrategy();

	public static DummyRepairStrategy getInstance() {
		return instance;
	}

	private DummyRepairStrategy() {
		super("Dummy Repair Strategy", "");
	}

	@Override
	@SuppressWarnings("unused")
	public void execute(Artifact... params) {
		// nothing to do
	}

}
