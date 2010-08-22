/*
 * $Id: CrudTableComposite.java,v 1.17 2008/04/22 19:20:02 cvspasto Exp $
 */
package commons.gui.table;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import commons.core.Predicate;
import commons.gui.model.collection.ListValueModel;
import commons.gui.widget.composite.SimpleComposite;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.utils.Clonator;

/**
 * This composite provides CRUD functionality
 */
public class CrudTableComposite extends SimpleComposite {

	public static final int ADD_BUTTON = (1 << 0);

	public static final int INSERT_BUTTON = (1 << 1);

	public static final int REMOVE_BUTTON = (1 << 2);

	public static final int EDIT_BUTTON = (1 << 3);

	public static final int DEFAULT_BUTTONS = ADD_BUTTON | REMOVE_BUTTON | EDIT_BUTTON;

	public static final int ALL_BUTTONS = ADD_BUTTON | INSERT_BUTTON | REMOVE_BUTTON | EDIT_BUTTON;

	public CrudTableComposite(TableMetainfo metainfo, EditHandler handler) {
		this(metainfo, handler, DEFAULT_BUTTONS);
	}

	@SuppressWarnings("unchecked")
	public CrudTableComposite(TableMetainfo metainfo, Class editDialogClass, int buttonFlags) {
		this(metainfo, new GenericEditHandler(metainfo.parent.getShell(), editDialogClass, metainfo.itemClass),
				buttonFlags);
	}

	@SuppressWarnings("unchecked")
	public CrudTableComposite(TableMetainfo metainfo, Class editDialogClass) {
		this(metainfo, new GenericEditHandler(metainfo.parent.getShell(), editDialogClass, metainfo.itemClass),
				DEFAULT_BUTTONS);
	}

	public CrudTableComposite(TableMetainfo metainfo, EditHandler handler, int buttonFlags) {

		super(metainfo.parent, metainfo.readOnly, 1);

		this.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite parent = metainfo.parent; // save this parent since we will use "this" as the table's parent
		metainfo.parent = this;

		this.table = new GenericTable(metainfo);

		metainfo.parent = parent; // restore the original parent within the metainfo

		this.model = (ListValueModel) metainfo.bindingInfo.getCompositeModel().getValueModel(
				metainfo.bindingInfo.getPropertyName());

		this.readOnly = metainfo.readOnly;

		buttonsComposite = new Composite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.CENTER;
		buttonsComposite.setLayoutData(gridData);

		if (readOnly) {
			buttonsComposite.setLayout(new GridLayout(getButtonBarCount(), false));

			if ((buttonFlags & EDIT_BUTTON) != 0) {
				viewButton = createButton(CommonLabels.VIEW, CommonLabels.VIEW, Predicate.NOT_NULL);
				viewButton.addSelectionListener(getViewSelectionAdapter(handler, viewButton));
				this.table.addDoubleClickListener(getReadOnlyDoubleClickListener(handler));
			}
		} else {
			buttonsComposite.setLayout(new GridLayout(getButtonBarCount(), false));
			createButtonBarAndListeners(this, handler, buttonFlags);
		}
	}

	public GenericTable getTable() {
		return table;
	}

	public Button getAddButton() {
		return addButton;
	}

	public Button getEditButton() {
		return editButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	protected SelectionAdapter getViewSelectionAdapter(final EditHandler handler, final Button view) {
		return new SelectionAdapter() {
			@Override
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				int selectedIndex = table.getTable().getSelectionIndex();

				if (selectedIndex < 0) {
					return;
				}

				final Object item = table.getSelectedElement();

				if (item != null) {
					handler.handleView(item, selectedIndex);
				}
			}
		};
	}

	protected void createButtonBarAndListeners(Composite parent, EditHandler rowSelectionHandler, int buttonFlags) {

		if ((buttonFlags & ADD_BUTTON) != 0) {
			addButton = createButton(CommonLabels.ADD, CommonLabels.ADD_NEW, Predicate.TRUE);
			addButton.addSelectionListener(getAddButtonListener(rowSelectionHandler, true));
		}
		if ((buttonFlags & INSERT_BUTTON) != 0) {
			addButton = createButton(CommonLabels.INSERT, CommonLabels.INSERT_NEW, Predicate.NOT_NULL);
			addButton.addSelectionListener(getAddButtonListener(rowSelectionHandler, false));
		}
		if ((buttonFlags & EDIT_BUTTON) != 0) {
			editButton = createButton(CommonLabels.EDIT, CommonLabels.EDIT_SELECTED_ITEM, Predicate.NOT_NULL);
			editButton.addSelectionListener(getEditButtonListener(rowSelectionHandler));
		}
		if ((buttonFlags & REMOVE_BUTTON) != 0) {
			deleteButton = createButton(CommonLabels.DELETE, CommonLabels.DELETE_SELECTED_ITEM, Predicate.NOT_NULL);
			deleteButton.addSelectionListener(getDeleteButtonListener(parent, rowSelectionHandler));
		}

		table.addDoubleClickListener(getDoubleClickListener(rowSelectionHandler));
	}

	@SuppressWarnings("unchecked")
	private boolean handleEdit(EditHandler handler) {
		// NOTA: no se usa el indice seleccionado para el modelo
		// porque el ordenamiento puede des-sincronizar las dos
		// colecciones (modelo y tabla)
		int selectedIndex = table.getTable().getSelectionIndex();

		if (selectedIndex < 0) {
			return false;
		}

		final Object item = table.getSelectedElement();
		int modelIndex = model.indexOf(item);

		if (modelIndex < 0) {
			throw new IllegalStateException("No se encontro el elemento '" + item + "' en el modelo");
		}

		final Object clonedItem = Clonator.clone(item);

		if (handler.handleUpdate(clonedItem, selectedIndex)) {

			int i = updateItem(model, modelIndex, selectedIndex, clonedItem);

			table.getTable().select(i);

			// refresh de la seleccion
			table.getTable().notifyListeners(SWT.Selection, null);

			return true;
		}

		return false;
	}

	protected IDoubleClickListener getDoubleClickListener(final EditHandler handler) {
		return new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				handleEdit(handler);
			}
		};
	}

	public Button createButton(EnumProperty text, EnumProperty toolTipText, Predicate predicate) {
		Button button = addButton(text, toolTipText);
		table.addAdditionalButton(button, predicate);
		return button;
	}

	public final Button addButton(EnumProperty text, EnumProperty toolTipText) {
		GridData gridData = new GridData();
		gridData.widthHint = 100;
		Button button = new Button(buttonsComposite, SWT.PUSH | SWT.CENTER);
		button.setText(text.toString());
		button.setToolTipText(toolTipText.toString());
		button.setLayoutData(gridData);
		return button;
	}

	public Composite getButtonsComposite() {
		return buttonsComposite;
	}

	protected IDoubleClickListener getReadOnlyDoubleClickListener(final EditHandler rowSelectionHandler) {
		return new IDoubleClickListener() {
			@SuppressWarnings("unchecked")
			public void doubleClick(DoubleClickEvent event) {
				viewButton.notifyListeners(SWT.Selection, null);
			}
		};
	}

	protected SelectionAdapter getAddButtonListener(final EditHandler handler, final boolean addAtEnd) {
		return new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent event) {

				int index;

				if (addAtEnd) {
					index = table.getTable().getItemCount();
				} else {
					index = table.getTable().getSelectionIndex();
				}

				Object newItem = handler.handleCreation(index);

				if (newItem != null) {
					index = insertItem(model, index, newItem);

					table.getTable().setSelection(index);
					table.getTable().showSelection();
				}
			}
		};
	}

	protected SelectionAdapter getEditButtonListener(final EditHandler handler) {
		return new SelectionAdapter() {
			@Override
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {

				if (handleEdit(handler)) {
					table.getTable().showSelection();
					model.notifyChange();
				}
			}
		};
	}

	protected SelectionAdapter getDeleteButtonListener(final Composite parent, final EditHandler handler) {
		return new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleDelete(handler);
			}
		};
	}

	@SuppressWarnings("unchecked")
	private void handleDelete(EditHandler handler) {
		int index = table.getTable().getSelectionIndex();

		if (index < 0) {
			return;
		}

		final Object item = table.getSelectedElement();
		final int modelIndex = model.indexOf(item);

		if (modelIndex < 0) {
			throw new IllegalStateException("No se encontro el elemento '" + item + "' en el modelo");
		}

		if (handler.handleDelete(item, index)) {
			removeItem(model, modelIndex, item);

			index = (index < table.getTable().getItemCount()) ? index : table.getTable().getItemCount() - 1;
			if (index != -1) {
				table.getTable().select(index);
			}
		}

		table.getTable().showSelection();
	}

	public void removeAddButton() {
		removeButton(addButton);
	}

	public void removeEditButton() {
		removeButton(editButton);
	}

	public void removeDeleteButton() {
		removeButton(deleteButton);
	}

	public ListValueModel getModel() {
		return model;
	}

	private void removeButton(Button button) {
		if (!this.readOnly) {
			table.removeAdditionalButton(button);
			button.dispose();
		}
	}

	protected int getButtonBarCount() {
		return 10;
	}

	/**
	 * Inserta el item en el modelo
	 * 
	 * @param listValueModel
	 * @param index
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	protected int insertItem(ListValueModel listValueModel, int index, Object item) {
		listValueModel.add(index, item);
		return index;
	}

	/**
	 * Elimina el item del modelo
	 * 
	 * @param listValueModel
	 * @param index
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	protected void removeItem(ListValueModel listValueModel, int index, Object item) {
		listValueModel.remove(index);
	}

	/**
	 * Actualiza el item
	 * 
	 * @param model
	 * @param modelIndex
	 * @param selectedIndex
	 * @param updatedItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected int updateItem(ListValueModel model, int modelIndex, int selectedIndex, Object updatedItem) {
		model.set(modelIndex, updatedItem);
		return selectedIndex;
	}

	private Button addButton;

	protected Button editButton;

	protected Button deleteButton;

	protected Button viewButton;

	protected boolean readOnly;

	private final Composite buttonsComposite;

	private ListValueModel model;

	private GenericTable table;

}