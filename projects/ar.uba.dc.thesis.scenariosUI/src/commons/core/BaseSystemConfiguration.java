package commons.core;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Gabriel Tursi
 * 
 */
public abstract class BaseSystemConfiguration extends Properties {

	protected BaseSystemConfiguration() {
		super();
		try {
			this.load(getClass().getResourceAsStream("/configuration.properties"));
		} catch (Exception e) {
			log.fatal("Error al cargar la configuracion del sistema. " + "Se utilizará la configuración por defecto");
			this.initDefaultValues();
		}
	}

	public boolean ambienteDesarrollo() {
		return getBoolean(AMBIENTE_DESARROLLO);
	}

	public String usuarioDesarrollo() {
		return getProperty(USUARIO_DESARROLLO);
	}

	public String passwordDesarrollo() {
		return getProperty(PASSWORD_DESARROLLO);
	}

	public boolean stackTraceEnErrores() {
		return getBoolean(STACK_TRACE_EN_ERRORES);
	}

	protected Boolean getBoolean(String property) {
		return Boolean.valueOf(getProperty(property));
	}

	/**
	 * Este método está pensado para inicializar los valores por defecto de las propiedades. Puede ser sobreescrito para
	 * extender funcionalidad.
	 */
	protected void initDefaultValues() {
		setProperty(AMBIENTE_DESARROLLO, Boolean.FALSE.toString());
		setProperty(USUARIO_DESARROLLO, "");
		setProperty(PASSWORD_DESARROLLO, "");
		setProperty(STACK_TRACE_EN_ERRORES, Boolean.FALSE.toString());
	}

	private static final String AMBIENTE_DESARROLLO = "AMBIENTE_DESARROLLO";

	private static final String USUARIO_DESARROLLO = "USUARIO_DESARROLLO";

	private static final String PASSWORD_DESARROLLO = "PASSWORD_DESARROLLO";

	private static final String STACK_TRACE_EN_ERRORES = "STACK_TRACE_EN_ERRORES";

	private static final Log log = LogFactory.getLog(BaseSystemConfiguration.class);

	private static final long serialVersionUID = 1L;
}