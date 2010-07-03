package ar.uba.dc.thesis.atam.scenario.persist;

import junit.framework.TestCase;
import ar.uba.dc.thesis.dao.SelfHealingScenarioDao;
import ar.uba.dc.thesis.dao.TestEnvironmentDao;
import ar.uba.dc.thesis.dao.TestSelfHealingScenarioDao;

public class SelfHealingScenarioPersisterTest extends TestCase {

	private static final String SCENARIOS_IN_XML_FULL_PATH = "src/test/resources/testScenarios.xml";

	/**
	 * Tests the whole marshaling / unmarshaling process
	 * <p>
	 * <ol>
	 * <li>First of all this test marshals an instance of {@link SelfHealingConfiguration} to an XML.
	 * <li>Then, it takes that XML and unmarshals it back to an object.
	 * <li>Finally, it compares both objects (the original and the unmarshaled). They test pass if and only if both
	 * objects are exactly the same as specified by {@link SelfHealingConfiguration#equals(Object)}
	 * </ol>
	 */
	public void testWholeProcess() {
		SelfHealingScenarioDao dao = new TestSelfHealingScenarioDao(new TestEnvironmentDao());
		SelfHealingConfiguration configToMarshal = new SelfHealingConfiguration("test", dao.getAllScenarios());

		SelfHealingScenarioPersister persister = new SelfHealingScenarioPersister();
		persister.saveToFile(configToMarshal, SCENARIOS_IN_XML_FULL_PATH);

		SelfHealingConfiguration unmarshaledConfig = persister.readFromFile(SCENARIOS_IN_XML_FULL_PATH);

		assertEquals(configToMarshal, unmarshaledConfig);

	}
}
