package commons.gui.widget.factory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import commons.gui.model.validation.ValidationStatusAware;
import commons.gui.widget.DefaultLayoutFactory;

/**
 * 
 * 
 */

public class LabelStatusAware implements ValidationStatusAware {

	public LabelStatusAware(Composite composite) {
		this.label = new Label(composite, SWT.CENTER);
		GridData gd = DefaultLayoutFactory.getGridData(label);
		gd.heightHint = 12;
		gd.widthHint = 12;
		gd.horizontalIndent = 0;
		gd.verticalIndent = 0;
		gd.grabExcessHorizontalSpace = false;

		this.ok = true;
	}

	public boolean isOkStatus() {
		return this.ok;
	}

	public void setErrorStatus(String message) {
		label.setToolTipText(message);

		if (this.image == null) {
			this.image = new Image(Display.getCurrent(), getClass().getResourceAsStream("/images/bullet_delete.png"));
		}
		label.setImage(this.image);
	}

	public void setOkStatus() {
		label.setToolTipText("");
		label.setImage(null);
	}

	private boolean ok;

	private Image image;
	private Label label;
}
