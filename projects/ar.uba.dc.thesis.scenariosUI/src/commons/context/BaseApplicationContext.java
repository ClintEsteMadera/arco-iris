package commons.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import commons.core.BaseSystemConfiguration;

/**
 * 
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