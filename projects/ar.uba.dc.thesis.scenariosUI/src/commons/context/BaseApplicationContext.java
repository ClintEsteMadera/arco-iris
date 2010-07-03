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
 * $Id: BaseApplicationContext.java,v 1.4 2008/04/23 19:15:28 cvschioc Exp $
 */
package commons.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import commons.core.BaseSystemConfiguration;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ $Date: 2008/04/23 19:15:28 $
 */
public abstract class BaseApplicationContext extends ClassPathXmlApplicationContext {

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> beanClass) {
		Map<String, Object> beans = getBeansOfType(beanClass);
		String canonicalName = beanClass.getCanonicalName();
		assert !beans.isEmpty() : "No existe bean para la clase " + canonicalName;
		assert beans.size() < 2 : "Existe mas de un bean para la misma clase" + canonicalName;
		return (T) beans.values().toArray()[0];
	}

	protected BaseApplicationContext() {
		super(locations.toArray(new String[locations.size()]));
	}

	/**
	 * Provee la configuración del sistema concreta de la aplicación.
	 */
	public abstract BaseSystemConfiguration getSystemConfiguration();

	/**
	 * Las subclases deben completar estáticamente esta lista con sus location particulares.
	 */
	protected static List<String> locations;

	static {
		locations = new ArrayList<String>();
		locations.add("spring/common-enumProperties.xml");
	}
}