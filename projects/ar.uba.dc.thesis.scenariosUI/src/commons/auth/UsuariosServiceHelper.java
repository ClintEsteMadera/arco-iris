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
 * $Id: UsuariosServiceHelper.java,v 1.9 2008/04/24 20:04:04 cvschioc Exp $
 */

package commons.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;
import org.springframework.jndi.JndiTemplate;

import sba.common.dao.DataIterator;
import sba.common.exception.ApplicationException;
import sba.common.security.exception.AuthenticationException;
import sba.common.session.SessionHelper;
import cajval.usuarios.service.ejb.ServiciosUsuariosRemote;
import cajval.usuarios.service.to.RolTO;

import commons.gui.widget.dialog.ChangePwdDialog;
import commons.gui.widget.dialog.InternalErrorDialog;

/**
 * Helper que abstrae los accesos al sistema de Usuarios.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.9 $ $Date: 2008/04/24 20:04:04 $
 */

public class UsuariosServiceHelper implements AuthenticationHelper {

	public UsuariosServiceHelper(JndiTemplate jndiTemplate) {
		super();
		this.factory = new SimpleRemoteStatelessSessionProxyFactoryBean();
		this.factory.setJndiTemplate(jndiTemplate);
		this.factory.setJndiName("ServiciosUsuarios");
		this.factory.setBusinessInterface(ServiciosUsuariosRemote.class);
	}

	public boolean authenticate(String username, String password) {
		boolean authenticated = false;
		try {
			this.setCredentials(username, password, null);
			this.obtenerRolesDelUsuario();

			log.info("Usuario " + username
					+ " autenticado exitosamente. Consultando tiempo remanente...");

			if (this.tiempoRemanenteExpirado()) {
				log.warn("El tiempo remanente del usuario " + username
						+ " expiró. Debe cambiar su password.");
				MessageDialog.openWarning(null, "Contraseña Expirada",
						"Su contraseña ha expirado. Debe cambiar su contraseña ahora.");
				ChangePwdDialog.openWithUserLoggedIn(null, true, this);
			}
			authenticated = true;
		} catch (Exception ex) {
			log.error("Intento fallido de autenticación " + ex.getMessage() + " " + ex.getCause());

			final String DIALOG_TITLE = "Error de autenticación";
			String message;
			if (ex instanceof NamingException && !(ex instanceof NameNotFoundException)) {
				message = "Usuario y/o password incorrecto";
				MessageDialog.openError(null, DIALOG_TITLE, message);
			} else {
				message = "Error interno de autenticación. Contacte al administrador.";
				InternalErrorDialog.openError(null, DIALOG_TITLE, message, ex);
			}
		}
		return authenticated;
	}

	public boolean tiempoRemanenteExpirado() {
		String usuario = SessionHelper.nombreDeUsuarioConectado();
		try {
			Long tiempoRemanente = (Long) getUsuariosService().tiempoRemanente();
			log.debug("Tiempo remanente del usuario " + usuario + ": " + tiempoRemanente);
			return tiempoRemanente <= 0L;
		} catch (Exception ex) {
			log.error(
					"La invocación al servicio de Usuarios para pedir el tiempo remanente del usuario "
							+ usuario + " produjo una excepción.", ex);
			throw new ApplicationException(
					"No se pudo consultar el tiempo remanente para el cambio de password del usuario "
							+ usuario + ". Contacte al administrador.", ex);
		}
	}

	public void cambiarPassword(String oldPassword, String newPassword) {
		try {
			getUsuariosService().cambiarPassword(oldPassword, newPassword);
			this.setCredentials(SessionHelper.nombreDeUsuarioConectado(), newPassword,
					SessionHelper.rolesDelUsuarioConectado());
		} catch (Exception ex) {
			throw new ApplicationException(ex.getMessage(), ex);
		}
	}

	public List<String> obtenerRolesDelUsuario() {
		List<String> roles = new ArrayList<String>();
		try {
			DataIterator rolesIterator = (DataIterator) getUsuariosService()
					.obtenerRolesDelUsuario();
			RolTO currentRolTO;
			while (rolesIterator.hasNext()) {
				currentRolTO = (RolTO) rolesIterator.next();
				roles.add(currentRolTO.getNombre().toUpperCase());
			}
			this.setCredentials(SessionHelper.nombreDeUsuarioConectado(), SessionHelper
					.passwordDeUsuarioConectado(), roles);
		} catch (NamingException ex) {
			throw new AuthenticationException(
					"No se pudieron obtener los roles para el usuario conectado. Contacte al administrador.",
					ex);
		} catch (Exception ex) {
			throw new ApplicationException(ex.getMessage(), ex);
		}
		return roles;
	}

	public void setCredentials(String username, String password, List<String> roles)
			throws NamingException {
		SessionHelper.setCredentials(username, password, roles);

		Properties env = factory.getJndiTemplate().getEnvironment();
		env.setProperty("java.naming.security.principal", SessionHelper.nombreDeUsuarioConectado());
		env.setProperty("java.naming.security.credentials", SessionHelper
				.passwordDeUsuarioConectado());
		factory.afterPropertiesSet();
	}

	public ServiciosUsuariosRemote getUsuariosService() {
		return (ServiciosUsuariosRemote) factory.getObject();
	}

	private SimpleRemoteStatelessSessionProxyFactoryBean factory;

	private static final Log log = LogFactory.getLog(UsuariosServiceHelper.class);
}