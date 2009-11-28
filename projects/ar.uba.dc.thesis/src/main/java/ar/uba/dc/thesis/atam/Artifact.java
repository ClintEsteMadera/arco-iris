package ar.uba.dc.thesis.atam;

import java.util.Collection;

import ar.uba.dc.thesis.acme.Operation;

public interface Artifact {
	public String getName();

	@SuppressWarnings("unchecked")
	public Collection<Operation> getOperations();
}
