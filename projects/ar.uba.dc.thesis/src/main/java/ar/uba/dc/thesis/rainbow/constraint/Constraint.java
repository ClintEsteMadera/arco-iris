package ar.uba.dc.thesis.rainbow.constraint;

import ar.uba.dc.thesis.common.Validatable;

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

	/**
	 * Restores all the properties of the receiver to its default values FIXME we shouldn't need this
	 */
	void restoreToDefaultValues();

}
