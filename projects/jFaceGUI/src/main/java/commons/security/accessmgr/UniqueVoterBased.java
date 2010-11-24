package commons.security.accessmgr;

import java.util.List;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AbstractAccessDecisionManager;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.acegisecurity.vote.RoleVoter;

/**
 * Modela un DecisionManager que elije un único voter de todos los que posee configurados para efectuar la votación. Lo
 * elige de acuerdo a las anotaciones hechas en la interfaz del servicio a autorizar.
 */
public class UniqueVoterBased extends AbstractAccessDecisionManager {

	public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config)
			throws AccessDeniedException {

		AccessDecisionVoter voter = this.getDecisionVoter(config);
		int result = voter.vote(authentication, object, config);

		switch (result) {
		case AccessDecisionVoter.ACCESS_DENIED:
			throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
					"Acceso denegado"));

		case AccessDecisionVoter.ACCESS_ABSTAIN:
			super.checkAllowIfAllAbstainDecisions();
			break;

		case AccessDecisionVoter.ACCESS_GRANTED:
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "cast" })
	private AccessDecisionVoter getDecisionVoter(ConfigAttributeDefinition config) {
		AccessDecisionVoter result = null;
		if (!config.getConfigAttributes().hasNext()) {
			throw new IllegalArgumentException("ERROR DE AUTORIZACIÓN: no hay annotations"
					+ " configuradas en el servicio que sea desea autorizar!");
		}
		List<AccessDecisionVoter> voters = ((List<AccessDecisionVoter>) getDecisionVoters());
		ConfigAttribute configAttrib = (ConfigAttribute) config.getConfigAttributes().next();
		String attrib = configAttrib.getAttribute();

		boolean classNameMatches;
		boolean isRoleVoterAndSupportsConfig;

		for (AccessDecisionVoter voter : voters) {
			classNameMatches = voter.getClass().getName().equals(attrib);
			isRoleVoterAndSupportsConfig = voter instanceof RoleVoter && voter.supports(configAttrib);
			if (classNameMatches || isRoleVoterAndSupportsConfig) {
				result = voter;
				break;
			}
		}
		return result;
	}
}