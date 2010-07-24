package scenariosui.gui.widget.page;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.composite.EnvironmentComposite;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.model.CompositeModel;
import commons.gui.widget.page.BasePreferencesPage;
import commons.properties.EnumProperty;

public class EnvironmentPage extends BasePreferencesPage<Environment> {

	private final ScenariosUIPurpose purpose;

	public EnvironmentPage(CompositeModel<Environment> model, EnumProperty title, boolean readOnly,
			ScenariosUIPurpose purpose) {
		super(model, title, readOnly);
		this.purpose = purpose;
	}

	@Override
	protected void addFields(Composite parent) {
		new EnvironmentComposite(parent, this.purpose, this.getCompositeModel());
	}
}