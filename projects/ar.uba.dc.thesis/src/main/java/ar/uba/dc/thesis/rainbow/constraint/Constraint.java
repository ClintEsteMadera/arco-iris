package ar.uba.dc.thesis.rainbow.constraint;

import org.acmestudio.acme.model.IAcmeModel;

import ar.uba.dc.thesis.atam.Environment.HeuristicType;
import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	boolean holds(IAcmeModel acmeModel, String involvedArtifactName);

	boolean holds(IAcmeModel acmeModel, HeuristicType heuristicType);
}
