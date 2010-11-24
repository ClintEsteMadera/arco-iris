package commons.security.voter;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.util.Assert;

/**
 * Voter que de acuerdo a una único parámetro (configurable) enumerado, decide si permitir el acceso o no al método
 * invocado.
 */
public abstract class SingleEnumConditionVoter extends AbstractVoter {

	private Map<String, List<String>> grantedPermissions;

	private static final Log logger = LogFactory.getLog(SingleEnumConditionVoter.class);

	/**
	 * Constructor que invoca al constructor de la superclase y guarda el mapa <code>grantedPermissions</code>.
	 * 
	 * @param grantedPermissions
	 *            un mapa de valores (de un tipo enumerado) y los correspondientes roles que poseen permisos asociados a
	 *            ese valor en cuestión. NO puede ser nulo.
	 */
	public SingleEnumConditionVoter(Map<String, List<String>> grantedPermissions) {
		super();
		Assert.notNull(grantedPermissions, "Los permisos de ejecucion no pueden ser nulos!");
		this.grantedPermissions = grantedPermissions;
	}

	/**
	 * Obtiene el valor (enumerado) que debe poseer el parámetro de la invocación al método y lo utiliza para buscar en
	 * el mapa <code>grantedPermissions</code> y retornar los permisos para el método en cuestión.
	 * 
	 * @param methodInvocation
	 *            el objeto que provee la información del método al cual se quiere dirimir la autorización de acceso.
	 * @return la lista de permisos configurados para el método, de acuerdo a los parámetros con los cuales se haya
	 *         invocado el mismo.
	 */
	@Override
	protected List<String> getAuthorizedRoles(ReflectiveMethodInvocation methodInvocation) {
		Object[] arguments = methodInvocation.getArguments();

		Enum enumerate = this.obtenerEnumParamFrom(arguments);

		if (logger.isDebugEnabled()) {
			logger.debug("El mapa de grantedPermissions contiene las entradas: " + this.grantedPermissions);
		}

		String enumName = enumerate.name();
		List<String> authorizedRoles = this.grantedPermissions.get(enumName);

		if (logger.isDebugEnabled()) {
			logger.debug("Los roles configurados para el enumerado " + enumName + " son: " + authorizedRoles);
		}
		return authorizedRoles;
	}

	protected abstract Enum obtenerEnumParamFrom(Object[] arguments);
}