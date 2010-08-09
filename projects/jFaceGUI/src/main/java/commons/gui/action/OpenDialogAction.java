package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;

public abstract class OpenDialogAction<T> extends BaseGuiAction<T> {

	protected OpenDialogAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	public Action getActionFor(final T model) {
		return new OpenPageAction() {
			@Override
			protected void openPage() {
				getDialogFor(model).open();
			}
		};
	}

	protected abstract Dialog getDialogFor(T model);
}