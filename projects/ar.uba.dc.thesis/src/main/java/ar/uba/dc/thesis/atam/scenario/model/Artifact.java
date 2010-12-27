package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.IdentifiableArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.Assert;
import ar.uba.dc.thesis.common.validation.ValidationError;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("artifact")
public class Artifact extends IdentifiableArcoIrisDomainObject {

	private static final long serialVersionUID = 1L;

	private static final String EMPTY_STRING = "";

	private static final String FRIENDLY_NAME = "Artifact";

	private static final String VALIDATION_MSG_SYSTEM_NAME = "System name cannot be empty";

	private static final String VALIDATION_MSG_NAME = "Artifact name cannot be empty";

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String systemName;

	public Artifact() {
		super();
	}

	public Artifact(Long id, String systemName, String name) {
		super(id);
		this.systemName = systemName;
		this.name = name;

		this.validate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@Override
	public String toString() {
		if (StringUtils.isNotBlank(this.systemName) && StringUtils.isNotBlank(this.name)) {
			return this.getSystemName() + "." + this.getName();
		}
		return EMPTY_STRING;
	}

	@Override
	protected List<ValidationError> collectValidationErrors() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Assert.notBlank(this.systemName, VALIDATION_MSG_SYSTEM_NAME, validationErrors);

		Assert.notBlank(this.name, VALIDATION_MSG_NAME, validationErrors);

		return validationErrors;
	}

	@Override
	protected String getObjectFriendlyName() {
		return FRIENDLY_NAME;
	}
}
