package ar.uba.dc.thesis.common;

/**
 * Provides a uniform way for a domain object to validate itself.
 */
public interface Validatable {
	/**
	 * Validates the instance and throws an Exception (typically {@link IllegalArgumentException} if something is wrong.
	 */
	void validate();
}
