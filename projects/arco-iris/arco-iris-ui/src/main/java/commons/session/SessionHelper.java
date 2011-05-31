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
 * Helper class to get information about the user currently connected.
 */
public abstract class SessionHelper {

	public static String usernameOfTheConnectedUser() {
		String username = null;
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			username = authentication.getName();
		}
		return username;
	}

	public static String passwordOfTheConnectedUser() {
		String passwd = null;
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			passwd = (String) authentication.getCredentials();
		}
		return passwd;
	}

	public static List<String> rolesOfTheConnectedUser() {
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
	 * Set user credentials in the security context.
	 * 
	 * @param username
	 *            user name to set
	 * @param password
	 *            password to set
	 * @param roles
	 *            the roles that <code>username</code> has. Should this parameter is null, that means that this user is
	 *            not authenticated yet.
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
			throw new RuntimeException("Internal configuration error. Null Security Context.");
		}
		return securityContext.getAuthentication();
	}
}