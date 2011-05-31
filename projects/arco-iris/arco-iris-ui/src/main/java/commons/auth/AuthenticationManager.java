package commons.auth;

public interface AuthenticationManager {

	boolean authenticate(String username, String password);

	public boolean remainingTimeExpired();

	public void changePassword(String oldPassword, String newPassword);
}
