package scenariosui.gui.util;

import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("constraintType")
public enum ConstraintType {
	NUMERIC_BINARY_RELATIONAL_CONSTRAINT("Numeric Binary Relational Constraint",
			NumericBinaryRelationalConstraint.class);

	private final Class<? extends Constraint> mappedClass;

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return this.getDescription();
	}

	public Class<? extends Constraint> getMappedClass() {
		return mappedClass;
	}

	public Constraint getNewConstraintInstance() {
		try {
			return this.mappedClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Class " + this.mappedClass.getName() + "could not be instantiated");
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to instantiate the class " + this.mappedClass.getName());
		}
	}

	public static ConstraintType getConstraintType(Class<? extends Constraint> mappedClass) {
		for (ConstraintType constraintType : values()) {
			if (constraintType.getMappedClass().equals(mappedClass)) {
				return constraintType;
			}
		}
		throw new RuntimeException("Unable to find the ConstraintType for " + mappedClass.getName());
	}

	ConstraintType(String description, Class<? extends Constraint> clazz) {
		this.description = description;
		this.mappedClass = clazz;
	}

	private final String description;

}
