package commons.gui.action;

import org.eclipse.jface.action.Action;

public abstract class OpenPageAction extends Action {

	public OpenPageAction() {
		super();
	}

	public OpenPageAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		openPage();
	}

	protected abstract void openPage();

}
