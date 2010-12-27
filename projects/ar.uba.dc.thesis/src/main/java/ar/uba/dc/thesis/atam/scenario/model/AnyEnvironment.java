package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.SortedMap;

import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This is an instance of {@link Environment} which is intended to be used as a wildcard (i.e. "this scenario applies
 * for <b>any</b> environment"). The weights in this particular environment are equally distributed considering the
 * amount of concerns returned by {@link Concern#values()}
 */
@XStreamAlias("anyEnvironment")
public final class AnyEnvironment extends Environment {

	private static final long serialVersionUID = 1L;

	private static final String DESCRIPTION = "Any Environment (no restrictions)";

	private static final String NAME = "DEFAULT";

	// this id should not be used by any other environment, see validation in Environment's constructor.
	static final Long ID = -1L;

	private static final ArrayList<Constraint> CONDITIONS = new ArrayList<Constraint>();

	private static final SortedMap<Concern, Double> equallyDistributedWeights = createMapWithEquallyDistributedWeights();

	private static final AnyEnvironment instance = new AnyEnvironment();

	public static synchronized AnyEnvironment getInstance() {
		return instance;
	}

	/**
	 * For a AnyEnvironment there is no (easy ;-) ) way of using a different id from the specified in
	 * {@link AnyEnvironment#ID}
	 * 
	 * @return true, always.
	 * 
	 * @see {@link Environment#isMyIdValid()}
	 */
	@Override
	protected boolean isMyIdValid() {
		return true;
	}

	private AnyEnvironment() {
		super(ID, NAME, CONDITIONS, equallyDistributedWeights);
	}

	@Override
	public String toString() {
		return DESCRIPTION;
	}
}
