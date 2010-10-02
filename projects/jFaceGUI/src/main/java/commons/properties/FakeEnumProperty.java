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
}