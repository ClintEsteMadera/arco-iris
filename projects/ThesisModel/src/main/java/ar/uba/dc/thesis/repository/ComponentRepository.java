package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.dummy.PageRenderer;

public class ComponentRepository {

	private static final PageRenderer PAGE_RENDERER = new PageRenderer();

	public static PageRenderer getPageRenderer() {
		return PAGE_RENDERER;
	}
}
