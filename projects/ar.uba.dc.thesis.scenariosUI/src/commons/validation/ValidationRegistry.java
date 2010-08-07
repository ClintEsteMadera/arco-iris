package commons.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase base para los directorios de validadores. Permiten validar "polimorficamente"
 * 
 * 
 */
public class ValidationRegistry<VALIDATOR> {

	public ValidationRegistry(Map<String, VALIDATOR> map) {
		this.map = map;
	}

	public ValidationRegistry() {
		super();
		this.map = new HashMap<String, VALIDATOR>();
	}

	public void addValidator(Class objectClass, VALIDATOR validator) {
		this.map.put(objectClass.getName(), validator);
	}

	public VALIDATOR getValidatorFor(Class objectClass) {
		VALIDATOR ret = null;
		Class clazz = objectClass;

		while (ret == null && clazz != null) {
			String s = clazz.getName();
			ret = map.get(s);

			if (ret == null) {
				if (!clazz.equals(Object.class)) {
					clazz = clazz.getSuperclass();
				} else {
					clazz = null;
				}
			}
		}

		if (ret == null) {
			throw new IllegalArgumentException("No se definio validador para la clase " + objectClass.getName());
		}

		return ret;
	}

	private Map<String, VALIDATOR> map;
}
