package commons.properties;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This directory contains all the enum classes where to look for labels for the creation of tables with user
 * preferences.
 */
public class EnumPropertyDirectory {

	private List<Class<? extends EnumProperty>> enumProps = new ArrayList<Class<? extends EnumProperty>>();

	private static final Log LOG = LogFactory.getLog(EnumPropertyDirectory.class);

	public EnumPropertyDirectory() {
		super();
	}

	public EnumPropertyDirectory(List<Class<? extends EnumProperty>> enumProps) {
		super();
		this.enumProps = enumProps;
	}

	public void register(Class<? extends EnumProperty>... enumPropClasses) {
		for (Class<? extends EnumProperty> aClass : enumPropClasses) {
			this.register(aClass);
		}
	}

	public void register(Class<? extends EnumProperty> enumPropClass) {
		if (enumPropClass.isEnum()) {
			// CommonLabels has precedence over any other instance of EnumProperty.
			// if (enumPropClass.equals(CommonLabels.class)) {
			// enumProps.add(0, enumPropClass);
			// } else {
			enumProps.add(enumPropClass);
			// }
		} else {
			throw new IllegalArgumentException("The classs " + enumPropClass.getName() + " is not enumerated!");
		}
	}

	@SuppressWarnings("unchecked")
	public EnumProperty getEnum(String name) {
		for (Class<? extends EnumProperty> enumPropClass : enumProps) {
			try {
				Enum enumConstant = Enum.valueOf((Class) enumPropClass, name);
				return (EnumProperty) enumConstant;
			} catch (Exception e) {
				// do nothing
			}
		}
		String msg = "Cannot find the constant " + name + " in all registered EnumProperties";
		if (LOG.isErrorEnabled()) {
			LOG.error(msg);
		}
		throw new RuntimeException(msg);
	}
}