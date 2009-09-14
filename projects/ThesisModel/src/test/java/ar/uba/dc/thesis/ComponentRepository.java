package ar.uba.dc.thesis;

import java.util.ArrayList;
import java.util.Collection;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;

public class ComponentRepository {

	private static final String PAGE_RENDERER_NAME = "Page Renderer";

	public static final String RENDER_PAGE_OPERATION_NAME = "renderPage";

	public static Component getPageRenderer() {
		Collection<Operation> operations = new ArrayList<Operation>();
		operations.add(new Operation(RENDER_PAGE_OPERATION_NAME));
		return new Component(PAGE_RENDERER_NAME, operations);
	}
}
