package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.dummy.PageRenderer;

public class ComponentRepository {

	public static PageRenderer getPageRenderer() {
		return new PageRenderer();
	}
}
