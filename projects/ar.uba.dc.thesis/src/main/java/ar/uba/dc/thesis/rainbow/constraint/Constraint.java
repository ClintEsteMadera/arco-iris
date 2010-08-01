package ar.uba.dc.thesis.rainbow.constraint;

import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	boolean holds(Number value);

	String getFullyQualifiedPropertyName();

	/**
	 * Restores all the properties of the receiver to its default values
     * FIXME we shouldn't need this
	 */
	void restoreToDefaultValues();

}
