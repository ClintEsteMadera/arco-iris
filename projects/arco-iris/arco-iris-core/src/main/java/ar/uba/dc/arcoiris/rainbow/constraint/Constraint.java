package ar.uba.dc.arcoiris.rainbow.constraint;

import ar.uba.dc.arcoiris.common.validation.Validatable;

public interface Constraint extends Validatable {

	boolean holds(Number value);

	String getFullyQualifiedPropertyName();

	/**
	 * This method is intended to be used in clients using the Java Bean naming convention, where "toString()" cannot be
	 * used as-is.
	 * 
	 * @return usually, this method would return the same as toString().
	 */
	String getToString();
}
