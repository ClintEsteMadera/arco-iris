package commons.gui.widget.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import commons.gui.model.CompositeModel;
import commons.gui.widget.composite.SimpleComposite;

/**
 * Página básica para poner en un Wizard.
 * 
 */

public abstract class BaseWizardPage<T> extends WizardPage {

	protected BaseWizardPage(String title, CompositeModel<T> beanModel) {
		super(title, title, null);
		this.compositeModel = beanModel;
	}

	public void createControl(Composite parent) {
		SimpleComposite composite = new SimpleComposite(parent, false, 1);
		addFields(composite);
		super.setControl(composite);
	}

	public T getModel() {
		return this.compositeModel.getValue();
	}

	public CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}

	protected abstract void addFields(Composite composite);

	private CompositeModel<T> compositeModel;
}
