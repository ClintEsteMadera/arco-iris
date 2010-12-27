package ar.uba.dc.thesis.atam.scenario.model;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.ValidationError;
import ar.uba.dc.thesis.common.validation.ValidationException;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Stimulus extends ArcoIrisDomainObject {

	private static final long serialVersionUID = 1L;

	private static final ValidationError VALIDATION_ERROR_STIMULUS_NAME = new ValidationError(
			"Stimulus cannot be blank");

	/**
	 * Convenience instance intended to act as a shortcut for the concept of "ANY" environment.<br>
	 */
	public static final Stimulus ANY = new Stimulus();

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String source;

	@XStreamAsAttribute
	private boolean any;

	/**
	 * By default, all stimuli are "ANY"
	 */
	public Stimulus() {
		super();
		this.any = true;
	}

	/**
	 * Constructs an stimulus with no descriptive information about its source.
	 * 
	 * @param name
	 *            the stimulus name
	 */
	public Stimulus(String name) {
		this(name, "");
	}

	/**
	 * Constructs an explicit stimulus
	 * 
	 * @param name
	 *            the stimulus name
	 * @param source
	 *            the stimulus source
	 */
	public Stimulus(String name, String source) {
		this.name = name;
		this.source = source;
		this.any = false;

		this.validate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This field is only meant for descriptive purposes.
	 * 
	 * @return the source of the stimulus
	 */
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isAny() {
		return any;
	}

	public void setAny(boolean any) {
		this.any = any;
	}

	@Override
	public String toString() {
		if (this.isAny()) {
			return "ANY";
		}
		return super.toString();
	}

	@Override
	public int hashCode() {
		if (this.isAny()) {
			return 31 * super.hashCode() + 1231;
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.isAny()) {
			return obj != null && obj instanceof Stimulus && ((Stimulus) obj).isAny();
		}
		return super.equals(obj);
	}

	/**
	 * The source of the stimulus and the any flag are not considered when checking equality of Stimuli.
	 */
	@Override
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return new String[] { "source", "any" };
	}

	@Override
	public void validate() throws ValidationException {
		if (!this.isAny() && StringUtils.isBlank(this.getName())) {
			throw new ValidationException(VALIDATION_ERROR_STIMULUS_NAME);
		}
	}

}
