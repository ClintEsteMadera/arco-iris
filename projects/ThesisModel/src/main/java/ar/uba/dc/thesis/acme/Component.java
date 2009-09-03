package ar.uba.dc.thesis.acme;

import java.util.Collection;

import ar.uba.dc.thesis.atam.Artifact;

public class Component implements Artifact {

	private String name;

	private Collection<Operation> operations;

	public Component(String name, Collection<Operation> operations) {
		super();
		this.name = name;
		this.operations = operations;
	}

	public String getName() {
		return this.name;
	}

	public Collection<Operation> getOperations() {
		return this.operations;
	}

	public Operation getOperation(String operationName) {
		for (Operation operation : this.operations) {
			if (operation.getName().equals(operationName)) {
				return operation;
			}
		}
		throw new RuntimeException("The operation " + operationName + " does not belong to this artifact.");
	}
}