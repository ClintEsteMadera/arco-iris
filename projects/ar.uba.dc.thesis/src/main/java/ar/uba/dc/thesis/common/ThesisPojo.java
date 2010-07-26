package ar.uba.dc.thesis.common;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Abstract behavior for all POJOs (Plain old Java objects) used in this project.
 */
public abstract class ThesisPojo implements Identifiable, Validatable, Serializable {

	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private Long id;

	public ThesisPojo() {
		super();
	}

	public ThesisPojo(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
		return ToStringBuilder.reflectionToString(this, ThesisToStringStyle.THESIS_STYLE);
	}

	/**
	 * This method provides the excluded fields when generating the equals and hashCode methods.
	 * <p>
	 * {@link ThesisPojo}'s default behavior is to not exclude any field.
	 * 
	 * @return an array with the names of the excluded fields for the generation of equals and hashCode methods. By
	 *         default, this method returns <code>null</code>.
	 */
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return null;
	}

	/**
	 * By default, the only validation performed on a ThesisPojo is just asserting the non-nullity of the unique
	 * identifier (id)
	 */
	public void validate() {
		if (this.id == null) {
			throw new RuntimeException("Object's unique identifier (id) cannot be null");
		}
	}

}
