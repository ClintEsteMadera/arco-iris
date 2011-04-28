package ar.uba.dc.thesis.util.sim.graphics;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * Load templates relatively to this class.
 */
public class ClassResourceLoader extends ResourceLoader {

	@SuppressWarnings("rawtypes")
	private static final Class CLASS = ClassResourceLoader.class;

	private static final String DIR = "templates/";

	@Override
	public long getLastModified(Resource resource) {
		return 0;
	}

	@Override
	public InputStream getResourceStream(String name) {
		return CLASS.getResourceAsStream(DIR.concat(name));
	}

	@Override
	public void init(ExtendedProperties extendedproperties) {
		// do nothing
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}
}
