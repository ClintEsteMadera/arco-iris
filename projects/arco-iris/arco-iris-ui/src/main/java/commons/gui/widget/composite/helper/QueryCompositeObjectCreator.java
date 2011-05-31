package commons.gui.widget.composite.helper;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.QueryComposite;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;
import commons.properties.EnumProperty;

/**
 * Provides a common way of creating a new object when the type of that object has a QueryComposite related to it.
 * 
 * @param <T>
 *            the type of the object to be created.
 */
public class QueryCompositeObjectCreator<T> {

	private final EnumProperty relatedTabId;

	private final OpenDialogWithPurposeAction<T, Purpose> action4NewObject;

	public QueryCompositeObjectCreator(EnumProperty relatedTabId,
			OpenDialogWithPurposeAction<T, Purpose> action4NewObject) {
		super();
		this.relatedTabId = relatedTabId;
		this.action4NewObject = action4NewObject;
	}

	public final T createNew(Object createOption) {
		CTabItem tab = PageHelper.getMainWindow().getTabItem(this.relatedTabId);
		QueryComposite<?> artifactsQueryComposite = (QueryComposite<?>) tab.getControl();

		Button createButton = artifactsQueryComposite.getCreateButton();
		this.simulateClick(createButton);

		BaseCompositeModelBoundedDialog<T> dialog4CreationAlreadyOpened = this.action4NewObject.getDialog();

		T newObject = null;
		// we don't want to return an object whose edition has been cancelled
		if (dialog4CreationAlreadyOpened.getReturnCode() == Window.OK) {
			newObject = dialog4CreationAlreadyOpened.getModel();
		}

		return newObject;

	}

	private void simulateClick(Button createButton) {
		createButton.setSelection(true);
		createButton.notifyListeners(SWT.Selection, new Event());
	}
}
