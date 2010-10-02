package commons.gui.widget.adapter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

public class ScaleAdapter extends Scale implements IntegerModifierControl {

	private static final String DIGITS = "digits";

	public ScaleAdapter(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public int getDigits() {
		return (Integer) super.getData(DIGITS);
	}

	@Override
	public void setDigits(int digits) {
		super.setData(DIGITS, digits);
	}

	/**
	 * This method is implemented just in order to avoid SWT prevent us to subclass Scale class.
	 */
	@Override
	protected void checkSubclass() {
		// do nothing
	}
}
