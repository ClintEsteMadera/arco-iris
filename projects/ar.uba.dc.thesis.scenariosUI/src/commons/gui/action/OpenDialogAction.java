package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.springframework.util.Assert;

public abstract class OpenDialogAction<T> implements GuiAction<T> {

	private final String shortcut;

	private String uniqueId;

	protected OpenDialogAction(String uniqueId, String shortcut) {
		super();
		Assert.notNull(uniqueId, "The unique id cannot be null");

		this.uniqueId = uniqueId;
		this.shortcut = shortcut == null ? "" : shortcut;
	}

	public Action getActionFor(final T model) {
		return new OpenPageAction() {
			@Override
			protected void openPage() {
				getDialogFor(model).open();
			}
		};
	}

	public String getUniqueId() {
		return this.uniqueId;
	}

	public String getShortcut() {
		return this.shortcut;
	}

	protected abstract Dialog getDialogFor(T model);
}