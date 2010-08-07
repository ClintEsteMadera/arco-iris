package commons.gui.table.editor;

import java.text.MessageFormat;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.gui.widget.factory.ComboFactory;

/**
 * 
 */
public class EnumComboCellEditor extends CellEditor {

	/**
	 * Creates a new cell editor with no control and no st of choices. Initially, the cell editor has no cell validator.
	 * 
	 * @since 2.1
	 * @see CellEditor#setStyle
	 * @see CellEditor#create
	 * @see ComboBoxCellEditor#setItems
	 * @see CellEditor#dispose
	 */
	public EnumComboCellEditor() {
		super();
		setStyle(DEFAULT_STYLE);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of choices and parented under the given control.
	 * The cell editor value is the zero-based index of the selected item. Initially, the cell editor has no cell
	 * validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 */
	public EnumComboCellEditor(Composite parent, Class<? extends Enum> enumeracion, boolean emptyOptionEnabled) {
		this(parent, enumeracion, DEFAULT_STYLE, emptyOptionEnabled);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of choices and parented under the given control.
	 * The cell editor value is the zero-based index of the selected item. Initially, the cell editor has no cell
	 * validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 * @param alignment
	 *            the alignment bits
	 * @since 2.1
	 */
	public EnumComboCellEditor(Composite parent, Class<? extends Enum> enumeracion, int style,
			boolean emptyOptionEnabled) {
		super(parent, style);
		this.emptyOptionEnabled = emptyOptionEnabled;
		setItems(enumeracion.getEnumConstants());

		if (emptyOptionEnabled) {
			comboBox.setData(ComboFactory.EMPTY_ITEM, null);
		}

		for (Enum e : enumeracion.getEnumConstants()) {
			comboBox.setData(e.toString(), e);
		}
	}

	/**
	 * Returns the list of choices for the combo box
	 * 
	 * @return the list of choices for the combo box
	 */
	public Enum[] getItems() {
		return this.enums.clone();
	}

	/**
	 * Sets the list of choices for the combo box
	 * 
	 * @param enums
	 *            the list of choices for the combo box
	 */
	public void setItems(Enum[] enums) {
		Assert.isNotNull(enums);
		this.enums = enums.clone();
		populateComboBoxItems();
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method sets the
	 * minimum width of the cell. The minimum width is 10 characters if <code>comboBox</code> is not <code>null</code>
	 * or <code>disposed</code> eles it is 60 pixels to make sure the arrow button and some text is visible. The list
	 * of CCombo will be wide enough to show its longest item.
	 */
	@Override
	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		if ((comboBox == null) || comboBox.isDisposed()) {
			layoutData.minimumWidth = 60;
		} else {
			// make the comboBox 10 characters wide
			GC graphicsContext = new GC(comboBox);
			layoutData.minimumWidth = (graphicsContext.getFontMetrics().getAverageCharWidth() * 10) + 10;
			graphicsContext.dispose();
		}
		return layoutData;
	}

	@Override
	protected Control createControl(Composite parent) {

		comboBox = new CCombo(parent, getStyle());
		comboBox.setFont(parent.getFont());

		comboBox.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			@Override
			public void keyPressed(KeyEvent event) {
				keyReleaseOccured(event);
			}
		});

		comboBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				selection = comboBox.getSelectionIndex();
			}
		});

		comboBox.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE || event.detail == SWT.TRAVERSE_RETURN) {
					event.doit = false;
				}
			}
		});

		comboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				EnumComboCellEditor.this.focusLost();
			}
		});
		return comboBox;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method returns
	 * the zero-based index of the current selection.
	 * 
	 * @return the zero-based index of the current selection wrapped as an <code>Integer</code>
	 */
	@Override
	protected Object doGetValue() {
		return comboBox.getData(comboBox.getText());
	}

	@Override
	protected void doSetFocus() {
		comboBox.setFocus();
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method accepts a
	 * zero-based index of a selection.
	 * 
	 * @param value
	 *            the zero-based index of the selection wrapped as an <code>Integer</code>
	 */
	@Override
	protected void doSetValue(Object value) {
		comboBox.setText(value.toString());
	}

	@Override
	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		}
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems() {
		if (comboBox != null && enums != null) {
			comboBox.removeAll();

			if (emptyOptionEnabled) {
				comboBox.add(ComboFactory.EMPTY_ITEM, 0);
			}

			for (int i = 0; i < enums.length; i++) {
				comboBox.add(enums[i].toString());
			}
			setValueValid(true);
			selection = 0;
		}
	}

	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	private void applyEditorValueAndDeactivate() {
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex();
		Object newValue = doGetValue();
		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);

		if (!isValid) {
			// Only format if the 'index' is valid
			if (enums.length > 0 && selection >= 0 && selection < enums.length) {
				// try to insert the current value into the error message.
				setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { enums[selection] }));
			} else {
				// Since we don't have a valid index, assume we're using an 'edit'
				// combo so format using its text value
				setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { comboBox.getText() }));
			}
		}

		fireApplyEditorValue();
		deactivate();
	}

	/**
	 * The list of items to present in the combo box.
	 */
	private Enum[] enums;

	/**
	 * The zero-based index of the selected item.
	 */
	private int selection;

	/**
	 * The custom combo box control.
	 */
	private CCombo comboBox;

	/**
	 * Specifies if it's enabled the empty option
	 */
	private boolean emptyOptionEnabled;

	/**
	 * Default ComboBoxCellEditor alignment
	 */
	private static final int DEFAULT_STYLE = SWT.READ_ONLY;
}