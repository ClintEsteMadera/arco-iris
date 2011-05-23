package commons.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import commons.core.BaseSystemConfiguration;

public abstract class BaseApplicationContext extends ClassPathXmlApplicationContext {

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> beanClass) {
		Map<String, Object> beans = getBeansOfType(beanClass);
		String canonicalName = beanClass.getCanonicalName();
		assert !beans.isEmpty() : "No bean registered for class: " + canonicalName;
		assert beans.size() < 2 : "More than one bean for class:" + canonicalName;
		return (T) beans.values().toArray()[0];
	}

	protected BaseApplicationContext() {
		super(locations.toArray(new String[locations.size()]));
	}

	/**
	 * Provides concrete's system configuration.
	 */
	public abstract BaseSystemConfiguration getSystemConfiguration();

	/**
	 * Subclasses may (statically) add specific locations to this list.
	 */
	protected static List<String> locations;

	static {
		locations = new ArrayList<String>();
		locations.add("spring/common-enumProperties.xml");
	}
}