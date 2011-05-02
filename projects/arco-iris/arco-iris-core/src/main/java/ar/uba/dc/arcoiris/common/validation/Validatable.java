package ar.uba.dc.arcoiris.common.validation;

/**
 * Provides a uniform way for a domain object to validate itself.
 */
public interface Validatable {
	/**
	 * Validates a <b>complete</b> instance of this interface.<br>
	 * <i>Complete</i> means that, at the point of being validated by this method, all relevant instance variables of
	 * the receiver must be properly set.
	 * 
	 * @throws validationException
	 *             if the state of the object is not valid.
	 */
	void validate() throws ValidationException;
}
