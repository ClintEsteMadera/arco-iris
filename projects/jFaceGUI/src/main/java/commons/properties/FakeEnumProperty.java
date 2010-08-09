package commons.properties;

/**
 * Fake Enum Property utilazada para wrappear un String.
 * 
 * 
 */

public class FakeEnumProperty implements EnumProperty {

	public FakeEnumProperty(String label) {
		super();
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public String toString(Object... reemplazos) {
		return label;
	}

	public String name() {
		return label;
	}

	private String label;

	private static final long serialVersionUID = 1L;
}