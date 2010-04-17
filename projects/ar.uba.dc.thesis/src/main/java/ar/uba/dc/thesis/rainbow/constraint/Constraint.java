package ar.uba.dc.thesis.rainbow.constraint;

import org.acmestudio.acme.model.IAcmeModel;

import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	boolean holds(IAcmeModel acmeModel);
}
