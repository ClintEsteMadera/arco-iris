package ar.uba.dc.thesis.acme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ar.uba.dc.thesis.atam.Artifact;

public class Component implements Artifact {

	private final String name;

	private final Collection<Operation<? extends Artifact>> operations = new ArrayList<Operation<? extends Artifact>>();

	public Component(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Collection<Operation<? extends Artifact>> getOperations() {
		return this.operations;
	}

	public Operation<? extends Artifact> getOperation(String operationName) {
		for (Operation<? extends Artifact> operation : this.operations) {
			if (operation.getName().equals(operationName)) {
				return operation;
			}
		}
		throw new RuntimeException("The operation " + operationName + " does not belong to this artifact.");
	}

	public void addOperations(Operation<? extends Component>... operations) {
		this.operations.addAll(Arrays.asList(operations));
	}
}