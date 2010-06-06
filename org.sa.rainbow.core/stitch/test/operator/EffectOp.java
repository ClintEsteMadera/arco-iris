/**
 * Created July 4, 2006.
 */
package test.operator;

import java.util.Set;

import org.acmestudio.acme.core.exception.AcmeException;
import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.acme.element.IAcmeConnector;
import org.acmestudio.acme.element.IAcmeElementInstance;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.acme.model.util.core.UMFloatValue;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * Test library of system change operators, specific to style..?
 */
public class EffectOp {

	private static RainbowLogger m_logger = RainbowLoggerFactory.logger(EffectOp.class);

	/**
	 * Creates a new Gaga component in the system and link the supplied comp to it...
	 */
	public static void connectNewGaga (IAcmeElementInstance<?,?> src) {
		m_logger.trace("connectNewGaga has been called with src == " + src.toString());
	}

	/**
	 * Simulates restart of process identified in param set
	 */
	public static void restartProcess (Set<IAcmeComponent> s) {
		m_logger.trace("restartProcess has been called with src set == " + s.toString());

		// assuming elements have type IAcmeComponent
		
	}

	/**
	 * Simulates reset of a connection identified in param set
	 */
	public static void resetConnection (Set<IAcmeConnector> s) {
		m_logger.trace("resetConnection has been called with src set == " + s.toString());

		// assuming elements have type IAcmeConnector
		for (IAcmeConnector conn : s) {
			IAcmeProperty prop = conn.getProperty("latency");
			IAcmeCommand<?> cmd = conn.getCommandFactory().propertyValueSetCommand(prop, new UMFloatValue(1000));
			try {
				cmd.execute();
			} catch (IllegalStateException e) {
				m_logger.error("Acme Command execution failed!", e);
			} catch (AcmeException e) {
				m_logger.error("Acme Command execution failed!", e);
			}
		}
	}

}
