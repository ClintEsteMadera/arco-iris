package org.sa.rainbow;

import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;

public class AttributeValueTripleWithStimulus extends AttributeValueTriple {

	private static final long serialVersionUID = 1L;

	private final String stimulus;

	public AttributeValueTripleWithStimulus(String type, String name, Object value, String stimulus) {
		super(type, name, value);
		this.stimulus = stimulus;
	}

	public AttributeValueTripleWithStimulus(TypeNamePair attr, Object value, String stimulus) {
		super(attr, value);
		this.stimulus = stimulus;
	}

	public String getStimulus() {
		return stimulus;
	}

	@Override
	public String toString() {
		return super.toString() + " (stimulus: " + this.getStimulus();
	}
}