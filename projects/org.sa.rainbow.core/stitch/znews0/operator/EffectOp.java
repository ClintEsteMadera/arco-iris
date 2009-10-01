/**
 * Created August 25, 2006.
 */
package znews0.operator;

import org.acmestudio.acme.core.exception.AcmeException;
import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.acme.element.IAcmeElementInstance;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.acme.model.util.core.UMBooleanValue;
import org.acmestudio.acme.model.util.core.UMIntValue;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.monitor.sim.ISimulatedTargetSystem;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

/**
 * Effector library for the ZNews example system.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class EffectOp {

	private static RainbowLogger m_logger = RainbowLoggerFactory.logger(EffectOp.class);

	public static void activateServer (IAcmeElementInstance<?,?> element) {
		// 1. find the system element corresponding to this arch element
		//    - need translation lookup for this?
		// 2. get a reference to its "activate" interface
		// 3. activate it

		// SIM HACK
		boolean activeState = true;
		// assume element is component
		IAcmeComponent comp = (IAcmeComponent )element;
		switchServerStatus(comp, activeState);
	}

	public static void deactivateServer (IAcmeElementInstance<?,?> element) {
		// 1. find the system element corresponding to this arch element
		// 2. get a reference to its "deactivate" interface
		// 3. deactivate it

		// SIM HACK
		boolean activeState = false;
		// assume element is component
		IAcmeComponent comp = (IAcmeComponent )element;
		switchServerStatus(comp, activeState);
	}

	public static void setFidelity (IAcmeElementInstance<?,?> element, int fid) {
		// 1. find the system element corresponding to this arch element
		// 2. get a reference to its "tune fidelity" interface
		// 3. change its fidelity

		// SIM HACK
		// assume element is component
		IAcmeComponent comp = (IAcmeComponent )element;
		// - change arch element value
		IAcmeProperty prop = comp.getProperty("fidelity");
		IAcmeCommand<?> cmd = comp.getCommandFactory().propertyValueSetCommand(prop, new UMIntValue(fid));
		try {
			cmd.execute();
		} catch (IllegalStateException e) {
			m_logger.error("Acme Command execution failed!", e);
		} catch (AcmeException e) {
			m_logger.error("Acme Command execution failed!", e);
		}
		// - change sys element value
		ISimulatedTargetSystem sys = (ISimulatedTargetSystem )Oracle.instance().targetSystem();
		sys.effectProperty(prop.getQualifiedName(), fid);
	}

	private static void switchServerStatus (IAcmeComponent comp, boolean activeState) {
		// - change arch element value
		IAcmeProperty prop = comp.getProperty(Model.PROPKEY_ARCH_ENABLED);
		IAcmeCommand<?> cmd = comp.getCommandFactory().propertyValueSetCommand(prop, new UMBooleanValue(activeState));
		try {
			cmd.execute();
		} catch (IllegalStateException e) {
			m_logger.error("Acme Command execution failed!", e);
		} catch (AcmeException e) {
			m_logger.error("Acme Command execution failed!", e);
		}
		// - change sys element value
		ISimulatedTargetSystem sys = (ISimulatedTargetSystem )Oracle.instance().targetSystem();
		sys.effectProperty(prop.getQualifiedName(), String.valueOf(activeState));
	}

}
