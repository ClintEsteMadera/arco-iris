package commons.gui.widget.wizard;

import org.eclipse.jface.wizard.Wizard;

import commons.gui.model.CompositeModel;
import commons.gui.model.bean.BeanModel;

/**
 * 
 * 
 */

public abstract class BaseWizard<T> extends Wizard {

	public BaseWizard(String title) {
		super();
		setWindowTitle(title);
		setNeedsProgressMonitor(true);
		this.compositeModel = new BeanModel<T>(newInstance());
	}

	protected CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}

	protected T getModel() {
		return this.compositeModel.getValue();
	}

	/**
	 * Provee una nueva instancia del modelo a usar en el Wizard
	 */
	protected abstract T newInstance();

	private CompositeModel<T> compositeModel;
}
