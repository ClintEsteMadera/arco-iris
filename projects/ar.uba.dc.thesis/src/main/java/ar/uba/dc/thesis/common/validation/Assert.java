package ar.uba.dc.thesis.common.validation;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Yet another class named "Assert", customized for common validations across "Arco Iris" application.
 */
public final class Assert {

	/**
	 * This class is not meant to be instantiated
	 */
	private Assert() {
		super();
	}

	/**
	 * Provides a null safe validation of the specified String.
	 * 
	 * @param string
	 *            the string to validate
	 * @param the
	 *            message to use if the string is blank (i.e. it is null, the empty string or a sequence of only white
	 *            spaces)
	 * @param validationErrors
	 *            validation errors where to add all of the encountered errors
	 */
	public static void notBlank(String string, String validationMsgIfEmpty, List<ValidationError> validationErrors) {
		if (StringUtils.isBlank(string)) {
			validationErrors.add(new ValidationError(validationMsgIfEmpty));
		}
	}

	/**
	 * Provides a null validation of the specified object
	 * 
	 * @param object
	 *            the object to validate
	 * @param the
	 *            message to use if the object is null
	 * @param validationErrors
	 *            validation errors where to add all of the encountered errors
	 */
	public static void notNull(Object object, String validationMsgIfEmpty, List<ValidationError> validationErrors) {
		if (object == null) {
			validationErrors.add(new ValidationError(validationMsgIfEmpty));
		}
	}

	/**
	 * Provides a null safe validation of the specified collection of {@link Validatable}
	 * 
	 * @param validatables
	 *            the collection of validatables to be validated
	 * @param validationMsgIfEmpty
	 *            the message to use if the collection is empty
	 * @param validationErrors
	 *            validation errors where to add all of the encountered errors
	 */
	public static void notEmptyAndValid(Collection<? extends Validatable> validatables, String validationMsgIfEmpty,
			List<ValidationError> validationErrors) {
		if (CollectionUtils.isEmpty(validatables)) {
			validationErrors.add(new ValidationError(validationMsgIfEmpty));
		} else {
			isValid(validatables, validationErrors);
		}
	}

	/**
	 * Iterates over the provided collection, collecting all of the validation errors encountered as a result of
	 * invoking {@link Validatable#validate()} on each member of the collection.
	 * 
	 * @param validatables
	 *            the collection of validatables to be validated
	 * @param validationErrors
	 *            validation errors where to add all of the encountered errors
	 */
	public static void isValid(Collection<? extends Validatable> validatables, List<ValidationError> validationErrors) {
		for (Validatable validatable : validatables) {
			try {
				validatable.validate();
			} catch (ValidationException e) {
				CollectionUtils.addAll(validationErrors, e.getErrors());
			}
		}
	}

	/**
	 * Provides a null safe validation of the specified instance of {@link Validatable}
	 * 
	 * @param validatable
	 *            the instance to be validated
	 * @param validationMsgIfNull
	 *            the message to use if the object is null
	 * @param validationErrors
	 *            validation errors where to add all of the encountered errors
	 */
	public static void notNullAndValid(Validatable validatable, String validationMsgIfNull,
			List<ValidationError> validationErrors) {
		if (validatable == null) {
			validationErrors.add(new ValidationError(validationMsgIfNull));
		} else {
			try {
				validatable.validate();
			} catch (ValidationException e) {
				CollectionUtils.addAll(validationErrors, e.getErrors());
			}
		}
	}

	/**
	 * If the condition does not hold, adds a {@link ValidationError} to <code>validationErrors</code>, using the
	 * message passed in as parameter in <code>validationMsgIfFalse</code>.
	 * 
	 * @param condition
	 *            the condition to evaluate
	 * @param validationMsgIfFalse
	 *            the message to use if the condition does not hold (i.e. it is == false)
	 * @param validationErrors
	 *            validation errors where to add all of the encountered errors
	 */
	public static void isTrue(boolean condition, String validationMsgIfFalse, List<ValidationError> validationErrors) {
		if (!condition) {
			validationErrors.add(new ValidationError(validationMsgIfFalse));
		}
	}
}
