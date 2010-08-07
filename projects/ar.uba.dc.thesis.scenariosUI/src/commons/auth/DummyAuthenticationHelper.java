package commons.auth;

/**
 * Helper que no realiza ninguna acción (dummy)
 */
public class DummyAuthenticationHelper implements AuthenticationHelper {

	public boolean authenticate(String username, String password) {
		return true;
	}

	public boolean tiempoRemanenteExpirado() {
		return false;
	}

	public void cambiarPassword(String oldPassword, String newPassword) {
		// do nothing
	}
}