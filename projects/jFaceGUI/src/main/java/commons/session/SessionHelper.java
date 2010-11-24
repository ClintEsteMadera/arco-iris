package commons.session;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;

/**
 * Auxiliar para la obtención de información de la sesión del usuario conectado.
 */
public abstract class SessionHelper {

	/**
	 * Retorna el nombre del usuario conectado.
	 * 
	 * @return nombre del usuario conectado.
	 */
	public static String nombreDeUsuarioConectado() {
		String username = null;
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			username = authentication.getName();
		}
		return username;
	}

	/**
	 * Retorna el password del usuario conectado.
	 * 
	 * @return password del usuario conectado.
	 */
	public static String passwordDeUsuarioConectado() {
		String passwd = null;
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			passwd = (String) authentication.getCredentials();
		}
		return passwd;
	}

	/**
	 * Retorna la lista de nombres de roles del usuario conectado.
	 * 
	 * @return nombres de los roles que posee el usuario conectado
	 */
	public static List<String> rolesDelUsuarioConectado() {
		List<String> rolesDelUsuario = new ArrayList<String>();
		Authentication authentication = getAuthentication();

		if (authentication != null) {
			GrantedAuthority[] grantedAuthorities = authentication.getAuthorities();
			if (grantedAuthorities != null) {
				for (GrantedAuthority authority : grantedAuthorities) {
					rolesDelUsuario.add(authority.getAuthority());
				}
			}
		}
		return rolesDelUsuario;
	}

	/**
	 * Setea las credenciales del usuario en el contexto de Seguridad.
	 * 
	 * @param username
	 *            el nombre del usuario a setear
	 * @param password
	 *            el password del usuario a setear
	 * @param roles
	 *            los roles que posee el usuario. Si este parámetro fuera <code>null</code>, se entiende que el usuario
	 *            NO está autenticado todavía.
	 */
	public static void setCredentials(String username, String password, List<String> roles) {
		Authentication authentication;
		if (roles == null) {
			authentication = new UsernamePasswordAuthenticationToken(username, password);
		} else {
			GrantedAuthority[] grantedAuthorities = new GrantedAuthority[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				grantedAuthorities[i] = new GrantedAuthorityImpl(roles.get(i));
			}
			authentication = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private static Authentication getAuthentication() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext == null) {
			throw new RuntimeException("Error de Configuración interno. Contexto de Seguridad nulo.");
		}
		return securityContext.getAuthentication();
	}
}