package ar.uba.dc.arcoiris.common;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.arcoiris.common.validation.Assert;
import ar.uba.dc.arcoiris.common.validation.ValidationError;
import ar.uba.dc.arcoiris.common.validation.ValidationException;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class IdentifiableArcoIrisDomainObject extends ArcoIrisDomainObject implements Identifiable {

	private static final long serialVersionUID = 1L;

	private static final String VALIDATION_MSG_ID = "'s unique identifier (id) cannot be empty";

	@XStreamAsAttribute
	private Long id;

	public IdentifiableArcoIrisDomainObject() {
		super();
	}

	public IdentifiableArcoIrisDomainObject(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * By default, the only specific validation performed on an ArcoIrisDomainObject is just asserting the non-nullity
	 * of the unique identifier (id) + invoking {@link #collectValidationErrors()} method in order to perform specific
	 * validations in the context of each subclass.
	 */
	@Override
	public final void validate() throws ValidationException {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Assert.notNull(this.id, this.getObjectFriendlyName() + VALIDATION_MSG_ID, validationErrors);

		validationErrors.addAll(this.collectValidationErrors());

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}

	/**
	 * This method is intended to assure validation is implemented among all subclasses.
	 * <p>
	 * <b>Importante Note:</b> Do not invoke {@link #validate()}, if you do so, there will be an infinite loop because
	 * {@link #validate()} invokes this method.
	 */
	protected abstract List<ValidationError> collectValidationErrors();

	/**
	 * This method is intended to provide a friendly name for the type of the concrete object which implements it. It is
	 * mostly used for customizing validation messages shown to the final user.
	 * 
	 * @return a friendly and descriptive name for the concrete Identifiable object, in order to show that name to the
	 *         final user.
	 */
	protected abstract String getObjectFriendlyName();
}
