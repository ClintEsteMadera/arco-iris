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
 * $Id: BaseSystemConfiguration.java,v 1.2 2008/04/23 19:15:30 cvschioc Exp $
 */
package commons.core;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Gabriel Tursi
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/04/23 19:15:30 $
 */
public abstract class BaseSystemConfiguration extends Properties {

	protected BaseSystemConfiguration() {
		super();
		try {
			this.load(getClass().getResourceAsStream("/configuration.properties"));
		} catch (Exception e) {
			log.fatal("Error al cargar la configuracion del sistema. "
					+ "Se utilizar� la configuraci�n por defecto");
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
	 * Este m�todo est� pensado para inicializar los valores por defecto de las propiedades. Puede
	 * ser sobreescrito para extender funcionalidad.
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