/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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
	 * Provee la configuraci�n del sistema concreta de la aplicaci�n.
	 */
	public abstract BaseSystemConfiguration getSystemConfiguration();

	/**
	 * Las subclases deben completar est�ticamente esta lista con sus location particulares.
	 */
	protected static List<String> locations;

	static {
		locations = new ArrayList<String>();
		locations.add("spring/common-enumProperties.xml");
	}
}