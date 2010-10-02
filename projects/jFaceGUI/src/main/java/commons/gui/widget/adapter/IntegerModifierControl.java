package commons.gui.widget.adapter;

import org.eclipse.swt.events.SelectionListener;

/**
 * Common interface for widgets that modify an integer value.
 */
public interface IntegerModifierControl {

	int getDigits();

	void setDigits(int digits);

	int getSelection();

	void setSelection(int selection);

	void addSelectionListener(SelectionListener selectionListener);

	void removeSelectionListener(SelectionListener selectionListener);
}