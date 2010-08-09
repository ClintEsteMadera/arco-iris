package commons.gui.widget.composite;

import org.eclipse.swt.layout.GridLayout;

public class Tab extends SimpleComposite {

	public Tab(InvisibleTabFolder tabFolder, boolean readOnly) {
		super(tabFolder, readOnly, 1);
	}

	@Override
	protected void applyLayout() {
		super.applyLayout();
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 10;
		gridLayout.marginWidth = 15;
		this.setLayout(gridLayout);
	}
}
