package ar.uba.dc.thesis.selfhealing;

import java.util.List;

import ar.uba.dc.thesis.atam.Artifact;

public class RepairStrategy {

	private final String name;

	private final String code;

	public RepairStrategy(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}

	public void execute(List<Artifact> params) {
		System.out.println("Executing " + this.getName() + " using " + params);
		// TODO: Read the code and do something
		System.out.println("The Strategy " + this.getName() + " has been successfully executed");
	}
}