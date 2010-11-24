package commons.security.voter;

import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.util.Assert;

/**
 * Voter abstracto que decide si permite el acceso o no al m�todo invocado.
 */
public abstract class AbstractVoter implements AccessDecisionVoter {

	private static final Log logger = LogFactory.getLog(AbstractVoter.class);

	/**
	 * Por el momento siempre retorna <code>true</code>.
	 * 
	 * @return <code>true</code>
	 */
	public boolean supports(ConfigAttribute configattribute) {
		return true;
	}

	/**
	 * Se soporta �nicamente que se invoque al voter con la clase
	 * 
	 * @link MethodInvocation, pues el voter dirime su voto de acuerdo a qui�n ejecut� el m�todo y los par�metros del
	 *       mismo, con lo cual, esa informaci�n debe ser provista por el mentado objeto.
	 */
	public boolean supports(Class clazz) {
		logger.debug("supports(" + clazz.getName() + ") - BEGIN");
		boolean supports = MethodInvocation.class.equals(clazz);
		logger.debug(this.getClass().getSimpleName() + "supports " + clazz.getName() + "?: " + supports);
		return supports;
	}

	/**
	 * Vota si se permite el acceso al m�todo invocado, cuya informaci�n reside en el obj pasado por par�metro. El mismo
	 * se supone que es del tipo
	 * 
	 * @link MethodInvocation. Si los roles autorizados (mediante la configuraci�n) incluyen al menos a uno de los
	 *       permisos que residen en el objeto <code>authentication</code> pasado por par�metro, este m�todo permite el
	 *       acceso ({@link AccessDecisionVoter#ACCESS_GRANTED}). Sino, lo deniega, retornando
	 *       {@link AccessDecisionVoter#ACCESS_DENIED}.<br>
	 *       En el caso que la lista de roles autorizados sea nula (i.e.: <code>== null</code>), se interpreta que no
	 *       existen permisos de acceso expl�citos y se deniega el acceso.
	 */
	public int vote(Authentication authentication, Object obj, ConfigAttributeDefinition config) {
		int access = ACCESS_DENIED;

		if (logger.isDebugEnabled()) {
			logger.debug("vote(authentication, Object obj, configattributedefinition) - BEGIN");
			logger.debug("El \"secured object\" es: " + obj.toString());
		}

		ReflectiveMethodInvocation methodInvocation = (ReflectiveMethodInvocation) obj;
		String nombreMetodoActual = methodInvocation.getMethod().getName();

		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getSimpleName() + " invocado para el metodo " + nombreMetodoActual);
		}

		List<String> authorizedRoles = getAuthorizedRoles(methodInvocation);

		if (authorizedRoles != null) {
			Assert.notNull(authentication, "La autenticaci�n NO puede ser nula!");

			String usuario = authentication.getName();
			if (logger.isDebugEnabled()) {
				logger.debug("votando para la autorizaci�n del usuario " + usuario);
			}

			GrantedAuthority[] grantedAuthorities = authentication.getAuthorities();
			Assert.notNull(grantedAuthorities, "Las authorities del usuario " + usuario + " son nulas!");

			if (!authorizedRoles.isEmpty()) {
				for (int i = 0; access != ACCESS_GRANTED && i < grantedAuthorities.length; i++) {
					if (logger.isDebugEnabled()) {
						logger.debug("verificando autoridad: " + grantedAuthorities[i].getAuthority());
					}

					if (authorizedRoles.contains(grantedAuthorities[i].getAuthority())) {
						logger.info("El usuario " + usuario + " tiene la autoridad "
								+ grantedAuthorities[i].getAuthority() + ": Authorizaci�n CONCEDIDA.");
						access = ACCESS_GRANTED;
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("el metodo vote() retorna: " + access);
		}
		return access;
	}

	/**
	 * Se encarga de obtener los roles que est�n autorizados a acceder al m�todo subyacente en el par�metro
	 * <code>methodInvocation</code>
	 * 
	 * @param methodInvocation
	 *            el objeto que provee la informaci�n del m�todo al cual se quiere dirimir la autorizaci�n de acceso.
	 * @return la lista de permisos configurados para el m�todo, de acuerdo a los par�metros con los cuales se haya
	 *         invocado el mismo.
	 */
	protected abstract List<String> getAuthorizedRoles(ReflectiveMethodInvocation methodInvocation);
}