package ar.uba.dc.thesis.acme;

import java.util.Collection;
import java.util.HashSet;

public class Architecture {

	private final Collection<Component> components = new HashSet<Component>();

	public void addComponent(Component component) {
		this.components.add(component);
	}

	public Component getComponent(String name) {
		for (Component component : components) {
			if (component.getName().equals(name)) {
				return component;
			}
		}
		throw new RuntimeException("There is no component called " + name);
	}
}
