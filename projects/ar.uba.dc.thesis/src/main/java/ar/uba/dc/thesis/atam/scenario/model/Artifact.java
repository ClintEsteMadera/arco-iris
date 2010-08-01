package ar.uba.dc.thesis.atam.scenario.model;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("artifact")
public class Artifact extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String systemName;

	public Artifact() {
		super();
	}

	public Artifact(Long id, String systemName, String name) {
		super(id);
		this.systemName = systemName;
		this.name = name;

		this.validate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@Override
	public String toString() {
		return this.getSystemName() + "." + this.getName();
	}

	@Override
	public void validate() {
		if (StringUtils.isEmpty(this.systemName)) {
			throw new RuntimeException("The system name cannot be empty");
		}
		if (StringUtils.isEmpty(this.name)) {
			throw new RuntimeException("The artifact name cannot be empty");
		}
	}
}
