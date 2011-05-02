package ar.uba.dc.arcoiris.common;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ar.uba.dc.arcoiris.common.validation.Validatable;

/**
 * Abstract behavior for all domain objects used in this project.
 */
public abstract class ArcoIrisDomainObject implements Validatable, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, this.getEqualsAndHashCodeExcludedFields());
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, this.getEqualsAndHashCodeExcludedFields());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ArcoIrisToStringStyle.ARCO_IRIS_STYLE);
	}

	/**
	 * This method provides the excluded fields when generating the equals and hashCode methods.
	 * <p>
	 * {@link ArcoIrisDomainObject}'s default behavior is to not exclude any field.
	 * 
	 * @return an array with the names of the excluded fields for the generation of equals and hashCode methods. By
	 *         default, this method returns <code>null</code>.
	 */
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return null;
	}
}
