package commons.gui.action;

import org.springframework.util.Assert;

import commons.gui.util.purpose.Purpose;

/**
 * Open Dialog Action with a purpose
 */
public abstract class OpenDialogWithPurposeAction<T, P extends Purpose> extends OpenDialogAction<T> {

	private final P purpose;

	protected OpenDialogWithPurposeAction(String id, String shortcut, P purpose) {
		super(id, shortcut);
		Assert.notNull(purpose, "The purpose cannot be null");
		this.purpose = purpose;
	}

	public P getPurpose() {
		return this.purpose;
	}
}