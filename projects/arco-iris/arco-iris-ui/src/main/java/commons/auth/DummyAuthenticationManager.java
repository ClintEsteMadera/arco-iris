package commons.auth;

/**
 * Dummy Authentication Manager implementation (commonly used only for development)
 */
public class DummyAuthenticationManager implements AuthenticationManager {

	public boolean authenticate(String username, String password) {
		return true;
	}

	public boolean remainingTimeExpired() {
		return false;
	}

	public void changePassword(String oldPassword, String newPassword) {
		// do nothing
	}
}