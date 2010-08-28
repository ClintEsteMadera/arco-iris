package commons.test;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import commons.properties.CommonConstants;
import commons.properties.CommonLabels;
import commons.properties.CommonMessages;
import commons.properties.CommonTooltips;

/**
 * Verifies that the enum classes that represent keys of properties are defined in their corresponding
 * <code>.properties</code> file.
 */
@SuppressWarnings("rawtypes")
public abstract class PropertiesVerifier {
	public static boolean testAllPropertiesAreDefined() throws Exception {
		boolean okStatus = true;
		for (Class<? extends Enum> enumClass : bundleMap.values()) {
			for (Enum enumerado : enumClass.getEnumConstants()) {
				try {
					enumerado.toString();
				} catch (MissingResourceException e) {
					System.err.println("The enum " + enumerado.getClass().getName() + "." + enumerado.name()
							+ " is not defined in the properties file");
					okStatus = false;
				}
			}
		}
		return okStatus;
	}

	@SuppressWarnings("unchecked")
	public static boolean testAllDefinedPropsAreInEnums() throws Exception {
		boolean okStatus = true;
		Set<ResourceBundle> bundles = bundleMap.keySet();
		for (ResourceBundle archivoProps : bundles) {
			Class<? extends Enum> enumClass = bundleMap.get(archivoProps);
			Enumeration<String> props = archivoProps.getKeys();
			String prop;
			String msg;
			while (props.hasMoreElements()) {
				prop = props.nextElement();
				try {
					Enum.valueOf(enumClass, prop);
				} catch (Exception e) {
					msg = "The property " + prop + " is not defined in the enum Class " + enumClass.getSimpleName();
					System.err.println(msg);
					okStatus = false;
				}
			}
		}
		return okStatus;
	}

	public static boolean testThereAreNoRepetitions(Class<? extends Enum>... excludedClasses) {
		boolean okStatus = true;
		Object[] array = bundleMap.values().toArray();
		Class<? extends Enum>[] enumProperties = manualCast(array);
		for (int i = 0; i < enumProperties.length; i++) {
			Class<? extends Enum> enumProp1 = enumProperties[i];
			if (!ArrayUtils.contains(excludedClasses, enumProp1)) {
				for (int j = i + 1; j < enumProperties.length; j++) {
					Class<? extends Enum> enumProp2 = enumProperties[j];
					if (!ArrayUtils.contains(excludedClasses, enumProp2)) {
						okStatus = isThereACollision(enumProp1, enumProp2) && okStatus;
					}
				}
			}
		}

		return okStatus;
	}

	private static boolean isThereACollision(Class<? extends Enum> enumPropClass, Class<? extends Enum> enumPropClass2) {
		boolean okStatus = true;
		List<? extends Enum> enumConstants = Arrays.asList(enumPropClass.getEnumConstants());
		List<? extends Enum> enumConstants2 = Arrays.asList(enumPropClass2.getEnumConstants());

		for (int i = 0; i < enumConstants.size(); i++) {
			for (int j = i + 1; j < enumConstants2.size(); j++) {
				if (enumConstants.get(i).name().equals(enumConstants2.get(j).name())) {
					System.err.println("The constant " + enumPropClass.getSimpleName() + "."
							+ enumConstants.get(i).name() + " is equals to the constant "
							+ enumPropClass2.getSimpleName() + "." + enumConstants2.get(j).name());
					okStatus = false;
				}
			}
		}

		return okStatus;
	}

	/**
	 * FIXME: Just when I thought I knew everything about Java, I've came across Generics...
	 * 
	 * @param objectArray
	 *            the object array to be casted.
	 * @return an array of Classes extending Enum.
	 */
	@SuppressWarnings("unchecked")
	private static Class<Enum>[] manualCast(Object[] objectArray) {
		Class<Enum>[] result = new Class[objectArray.length];
		for (int i = 0; i < objectArray.length; i++) {
			result[i] = (Class<Enum>) objectArray[i];
		}
		return result;
	}

	protected static Map<ResourceBundle, Class<? extends Enum>> bundleMap;

	static {
		bundleMap = new HashMap<ResourceBundle, Class<? extends Enum>>();
		bundleMap.put(ResourceBundle.getBundle("common_constants"), CommonConstants.class);
		bundleMap.put(ResourceBundle.getBundle("common_labels"), CommonLabels.class);
		bundleMap.put(ResourceBundle.getBundle("common_messages"), CommonMessages.class);
		bundleMap.put(ResourceBundle.getBundle("common_tooltips"), CommonTooltips.class);
	}
}