package commons.properties;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class EnumPropertiesHelper {

	private static final Log log = LogFactory.getLog(EnumPropertiesHelper.class);

	public static String getString(ResourceBundle resourceBundle, String key) {
		return resourceBundle.getString(key);
	}

	public static String getString(ResourceBundle resourceBundle, String key, Object... reemplazos) {
		String result = "";
		try {
			result = MessageFormat.format(getString(resourceBundle, key), reemplazos);
		} catch (IllegalArgumentException ex) {
			log.fatal("Cannot format: " + key);
		}
		return result;
	}

	public static String getString(Properties props, String key) {
		return props.getProperty(key);
	}

	public static String getString(Properties props, String key, Object[] reemplazos) {
		String result = "";
		try {
			result = MessageFormat.format(getString(props, key), reemplazos);
		} catch (IllegalArgumentException ex) {
			log.fatal("Cannot format: " + key);
		}
		return result;
	}

	public static String getString(Properties props, String key, boolean useKeyAsDefault) {
		String value;
		if (useKeyAsDefault) {
			value = props.getProperty(key, key);
		} else {
			value = props.getProperty(key);
		}
		return value;
	}

	public static Properties load(String fileName) {
		Properties props = new Properties();
		try {
			props.load(EnumPropertiesHelper.class.getResourceAsStream(fileName));
		} catch (Exception e) {
			throw new RuntimeException("Cannot format read file: " + fileName);
		}
		return props;
	}

	public static Properties load(Class clazz) {
		Properties props = new Properties();
		try {
			props.load(clazz.getResourceAsStream(clazz.getSimpleName() + ".properties"));
		} catch (Exception e) {
			throw new MissingResourceException("Cannot format read file: " + clazz.getSimpleName(),
					clazz.getSimpleName(), "");
		}
		return props;
	}
}