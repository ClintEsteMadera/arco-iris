package commons.auth;

/**
 * Interfaz que define m�todos relacionados con la autenticaci�n de un Usuario.
 * 
 */

public interface AuthenticationHelper {

	boolean authenticate(String username, String password);

	public boolean tiempoRemanenteExpirado();

	public void cambiarPassword(String oldPassword, String newPassword);
}
