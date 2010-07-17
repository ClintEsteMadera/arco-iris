package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This is an instance of {@link Environment} which is intended to be used as a wildcard (i.e. "this scenario applies
 * for <b>any</b> environment"). The weights in this particular environment are equally distributed considering the
 * amount of concerns returned by {@link Concern#values()}
 */
@XStreamAlias("defaultEnvironment")
public final class DefaultEnvironment extends Environment {

	private static final long serialVersionUID = 1L;

	private static final String NAME = "DEFAULT";

	// this id should not be used by any other environment, see validation in Environment's constructor.
	static final Long ID = -1L;

	private static final ArrayList<Constraint> CONDITIONS = new ArrayList<Constraint>();

	private static final Map<Concern, Double> equallyDistributedWeights = initWeights();

	private static final DefaultEnvironment instance = new DefaultEnvironment();

	public static synchronized DefaultEnvironment getInstance() {
		return instance;
	}

	/**
	 * Nothing special to validate specially on this class.
	 * 
	 * @see super{@link #validateId()}
	 */
	@Override
	protected void validateId() {
		// we specifically want to do nothing on this case
	}

	private DefaultEnvironment() {
		super(ID, NAME, CONDITIONS, equallyDistributedWeights);
	}

	private static Map<Concern, Double> initWeights() {
		Map<Concern, Double> equallyDistributedWeights = new HashMap<Concern, Double>();
		Concern[] values = Concern.values();
		Double aWeight = (double) 1 / values.length;
		for (Concern concern : values) {
			equallyDistributedWeights.put(concern, aWeight);
		}

		return equallyDistributedWeights;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
