package commons.gui.widget.composite;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class InvisibleTabFolder extends Composite {

	public InvisibleTabFolder(Composite parent, int style) {
		super(parent, style);
		StackLayout stackLayout = new StackLayout();
		stackLayout.marginWidth = -5;
		super.setLayout(stackLayout);
	}

	@Override
	public void setLayout(Layout layout) {
		// no se puede establecer un nuevo Layout
	}

	public void setSelection(int index) {
		Control[] children = getChildren();
		if (index >= 0 && index < children.length) {
			((StackLayout) getLayout()).topControl = children[index];
			layout();
		}
	}

}
