package commons.gui.widget.adapter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

public class SpinnerAdapter extends Spinner implements IntegerModifierControl {

	public SpinnerAdapter(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * This method is implemented just in order to avoid SWT prevent us to subclass Spinner class.
	 */
	@Override
	protected void checkSubclass() {
		// do nothing
	}
}
