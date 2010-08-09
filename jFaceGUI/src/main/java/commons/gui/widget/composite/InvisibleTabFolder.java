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

	public Tab[] getTabs() {
		Control[] children = super.getChildren();
		Tab[] tabs = new Tab[children.length];
		System.arraycopy(children, 0, tabs, 0, children.length);
		return tabs;
	}

	public Tab getTab(int index) {
		Tab[] children = this.getTabs();
		Tab tab = null;
		if (index >= 0 && index < children.length) {
			tab = children[index];
		}
		return tab;
	}

	public void setSelection(int index) {
		Tab tab = this.getTab(index);
		if (tab != null) {
			((StackLayout) getLayout()).topControl = tab;
			layout();
		}
	}
}
