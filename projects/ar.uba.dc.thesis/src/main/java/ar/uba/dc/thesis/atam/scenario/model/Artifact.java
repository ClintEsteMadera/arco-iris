package ar.uba.dc.thesis.atam.scenario.model;

import ar.uba.dc.thesis.common.ThesisPojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("artifact")
public class Artifact extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private final String name;

	@XStreamAsAttribute
	private final String systemName;

	public Artifact(Long id, String systemName, String name) {
		super(id);
		this.systemName = systemName;
		this.name = name;

		this.validate();
	}

	public String getName() {
		return this.name;
	}

	public String getSystemName() {
		return systemName;
	}

	@Override
	public void validate() {
		// Do nothing
	}
}
