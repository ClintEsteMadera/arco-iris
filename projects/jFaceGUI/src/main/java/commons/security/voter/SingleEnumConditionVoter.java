package commons.security.voter;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.util.Assert;

/**
 * Voter que de acuerdo a una �nico par�metro (configurable) enumerado, decide si permitir el acceso o no al m�todo
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
	 *            ese valor en cuesti�n. NO puede ser nulo.
	 */
	public SingleEnumConditionVoter(Map<String, List<String>> grantedPermissions) {
		super();
		Assert.notNull(grantedPermissions, "Los permisos de ejecucion no pueden ser nulos!");
		this.grantedPermissions = grantedPermissions;
	}

	/**
	 * Obtiene el valor (enumerado) que debe poseer el par�metro de la invocaci�n al m�todo y lo utiliza para buscar en
	 * el mapa <code>grantedPermissions</code> y retornar los permisos para el m�todo en cuesti�n.
	 * 
	 * @param methodInvocation
	 *            el objeto que provee la informaci�n del m�todo al cual se quiere dirimir la autorizaci�n de acceso.
	 * @return la lista de permisos configurados para el m�todo, de acuerdo a los par�metros con los cuales se haya
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