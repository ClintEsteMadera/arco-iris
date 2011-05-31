package commons.properties;

/**
 * This is a wrapper for a simple String
 */
public class FakeEnumProperty implements EnumProperty, Comparable<FakeEnumProperty> {

	private String label;

	private static final long serialVersionUID = 1L;

	public FakeEnumProperty(String label) {
		super();
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public <T> String toString(T... replacements) {
		return label;
	}

	public String name() {
		return label;
	}

	@Override
	public int compareTo(FakeEnumProperty another) {
		return this.label.compareTo(another.label);
	}

	@Override
	public int hashCode() {
		return 31 + ((label == null) ? 0 : label.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FakeEnumProperty other = (FakeEnumProperty) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

}