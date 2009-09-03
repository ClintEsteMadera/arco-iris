package ar.uba.dc.thesis.acme;

import java.util.Collection;
import java.util.HashSet;


public class Architecture {

	private Collection<Component> components = new HashSet<Component>();

	public void addComponent(Component component) {
		this.components.add(component);
	}

	public Collection<Component> getComponents() {
		return this.components;
	}
}
