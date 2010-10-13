package commons.gui.action;

import org.eclipse.jface.action.Action;

import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;

public abstract class OpenDialogAction<T> extends BaseGuiAction<T> {

	protected BaseCompositeModelBoundedDialog<T> dialog;

	protected OpenDialogAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	public Action getActionFor(final T model) {
		return new OpenPageAction() {
			@Override
			protected void openPage() {
				dialog = getDialogFor(model);
				dialog.open();
			}
		};
	}

	public BaseCompositeModelBoundedDialog<T> getDialog() {
		return this.dialog;
	}

	protected abstract BaseCompositeModelBoundedDialog<T> getDialogFor(T model);
}