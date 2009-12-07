/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: EnumPropertiesHelper.java,v 1.4 2008/02/04 14:52:52 cvschioc Exp $
 */

package sba.common.properties;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper que utilizan los enum properties para no repetir código.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ $Date: 2008/02/04 14:52:52 $
 */

public abstract class EnumPropertiesHelper {

	public static String getString(ResourceBundle resourceBundle, String key) {
		return resourceBundle.getString(key);
	}

	public static String getString(ResourceBundle resourceBundle, String key, Object... reemplazos) {
		String result = "";
		try {
			result = MessageFormat.format(getString(resourceBundle, key), reemplazos);
		} catch (IllegalArgumentException ex) {
			log.fatal("No se pudo formatear el texto: " + key);
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
			log.fatal("No se pudo formatear el texto: " + key);
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
			throw new RuntimeException("No se pudo leer el archivo " + fileName);
		}
		return props;
	}

	public static Properties load(Class clazz) {
		Properties props = new Properties();
		try {
			props.load(clazz.getResourceAsStream(clazz.getSimpleName() + ".properties"));
		} catch (Exception e) {
			throw new MissingResourceException("No se pudo leer el archivo "
					+ clazz.getSimpleName(), clazz.getSimpleName(), "");
		}
		return props;
	}

	private static final Log log = LogFactory.getLog(EnumPropertiesHelper.class);
}