package ar.uba.dc.thesis.rainbow.constraint;

import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	boolean holds(Number value);

	String getFullyQualifiedPropertyName();

	Quantifier getQuantifier();

}
