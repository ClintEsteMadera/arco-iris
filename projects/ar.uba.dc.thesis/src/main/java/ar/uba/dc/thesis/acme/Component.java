package ar.uba.dc.thesis.acme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ar.uba.dc.thesis.atam.Artifact;

@SuppressWarnings("unchecked")
public class Component implements Artifact {

	private final String name;

	private final Collection<Operation> operations = new ArrayList<Operation>();

	private final String systemName;

	public Component(String systemName, String name) {
		super();
		this.systemName = systemName;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getSystemName() {
		return systemName;
	}

	public Collection<Operation> getOperations() {
		return this.operations;
	}

	public Operation getOperation(Class<? extends Operation> clazz) {
		for (Operation operation : this.operations) {
			if (operation.getClass().equals(clazz)) {
				return operation;
			}
		}
		throw new RuntimeException("The operation " + clazz + " does not belong to this artifact.");
	}

	public void addOperations(Operation<? extends Component, ? extends Object>... operations) {
		this.operations.addAll(Arrays.asList(operations));
	}

}