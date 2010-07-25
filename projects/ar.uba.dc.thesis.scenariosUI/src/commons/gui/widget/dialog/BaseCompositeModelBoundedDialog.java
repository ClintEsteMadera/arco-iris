package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.gui.model.ComplexValueChangeEvent;
import commons.gui.model.ComplexValueChangeListener;
import commons.gui.model.CompositeModel;
import commons.gui.model.bean.BeanModel;
import commons.gui.widget.DefaultLayoutFactory;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;

/**
 * Provides an abstract dialog with an underlying model bounded to it<br>
 * Also, this dialog has a dirtiness check of the model bundled-in.
 * 
 * @param <T>
 *            The type of the model bounded to this dialog.
 */
public abstract class BaseCompositeModelBoundedDialog<T> extends OpenableTrayDialog<T> {

	private CompositeModel<T> compositeModel;

	private boolean modelDirty;

	public BaseCompositeModelBoundedDialog(T model, EnumProperty title, boolean readOnly) {
		super(null, title, readOnly);
		if (model == null) {
			model = newModel();
		}
		this.compositeModel = new BeanModel<T>(model);
	}

	public CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}

	public T getModel() {
		return this.compositeModel.getValue();
	}

	protected boolean doIHaveToAbandonChanges() {
		boolean abandonChanges = true;

		if (this.modelDirty) {
			abandonChanges = MessageDialog.openQuestion(this.getShell(), CommonLabels.ATENTION.toString(),
					"Do you wish to abandon the changes made to the scenario ?");
		}
		return abandonChanges;
	}

	@Override
	protected final Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		DefaultLayoutFactory.setDefaultGridLayout(composite, getNumColumns());

		this.addWidgetsToDialogArea(composite);
		// model modification check
		this.getCompositeModel().addComplexValueChangeListener(new ComplexValueChangeListener() {
			@SuppressWarnings("unchecked")
			public void complexValueChange(ComplexValueChangeEvent ev) {
				modelDirty = true;
			}
		});
		return super.createDialogArea(composite);
	}

	protected int getNumColumns() {
		return 1;
	}

	protected void showSuccessfulOperationDialog(String msg) {
		MessageDialog.openInformation(null, CommonLabels.SUCCESSFUL_OPERATION.toString(), msg);
	}

	/**
	 * This method is the main point where subclasses should add contents to this dialog
	 * 
	 * @param parent
	 *            the composite where to add contents to
	 */
	protected abstract void addWidgetsToDialogArea(Composite parent);

	protected abstract T newModel();
}