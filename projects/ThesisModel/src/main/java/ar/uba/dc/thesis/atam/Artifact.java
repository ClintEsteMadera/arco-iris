package ar.uba.dc.thesis.atam;

import java.util.Collection;

import ar.uba.dc.thesis.acme.Operation;

public class Artifact {

	private String name;

	private Collection<Operation> operations;

	public Artifact(String name, Collection<Operation> operations) {
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