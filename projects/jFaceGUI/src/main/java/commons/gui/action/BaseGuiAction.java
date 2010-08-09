package commons.gui.action;

import org.springframework.util.Assert;

public abstract class BaseGuiAction<T> implements GuiAction<T> {

	private String uniqueId;

	private String shortcut;

	protected BaseGuiAction(String uniqueId, String shortcut) {
		super();
		Assert.notNull(uniqueId, "The unique id cannot be null");
		this.uniqueId = uniqueId;
		this.shortcut = shortcut;
	}

	public String getUniqueId() {
		return this.uniqueId;
	}

	public String getShortcut() {
		return this.shortcut;
	}
}
