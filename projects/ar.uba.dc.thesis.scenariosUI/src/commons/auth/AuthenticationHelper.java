package commons.auth;

/**
 * Interfaz que define métodos relacionados con la autenticación de un Usuario.
 * 
 */

public interface AuthenticationHelper {

	boolean authenticate(String username, String password);

	public boolean tiempoRemanenteExpirado();

	public void cambiarPassword(String oldPassword, String newPassword);
}
