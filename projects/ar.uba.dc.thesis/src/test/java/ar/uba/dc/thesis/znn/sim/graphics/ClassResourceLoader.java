package ar.uba.dc.thesis.znn.sim.graphics;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * Carga templates en forma relativa a esta clase.
 * 
 * @author H. Adrián Uribe
 */
public class ClassResourceLoader extends ResourceLoader {

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
		// NOTA: Vacío
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}

	private static final Class CLASS = ClassResourceLoader.class;

	private static final String DIR = "templates/";
}
