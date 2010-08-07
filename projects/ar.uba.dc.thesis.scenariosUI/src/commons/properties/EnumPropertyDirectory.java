package commons.properties;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import commons.properties.CommonLabels;

/**
 * Directorio contiene todos los enumerados en dónde buscar labels para la creación de tablas con preferencias.
 * 
 */

public class EnumPropertyDirectory {

	public EnumPropertyDirectory() {
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
			// // CommonLabels tiene precedencia con respecto a cualquier otro EnumProperty.
			// if (enumPropClass.equals(CommonLabels.class)) {
			// enumProps.add(0, enumPropClass);
			// } else {
			enumProps.add(enumPropClass);
			// }
		} else {
			throw new IllegalArgumentException("La clase " + enumPropClass.getName() + " no es una clase enumerada!");
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
		String msg = "No se pudo encontrar la constante " + name + " en los EnumProperties registrados";
		if (LOG.isErrorEnabled()) {
			LOG.error(msg);
		}
		throw new RuntimeException(msg);
	}

	private List<Class<? extends EnumProperty>> enumProps = new ArrayList<Class<? extends EnumProperty>>();

	private static final Log LOG = LogFactory.getLog(EnumPropertyDirectory.class);
}