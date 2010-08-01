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
	public final void setLayout(Layout layout) {
		// this operation is not intended to be overwritten
	}

	public Control getTab(int index) {
		Control[] children = this.getChildren();
		Control tab = null;
		if (index >= 0 && index < children.length) {
			tab = children[index];
		}
		return tab;
	}

	public void setSelection(int index) {
		Control tab = this.getTab(index);
		if (tab != null) {
			((StackLayout) getLayout()).topControl = tab;
			layout();
		}
	}
}
