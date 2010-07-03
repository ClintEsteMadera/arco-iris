package commons.gui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import commons.core.Predicate;
import commons.gui.model.table.AbstractRowAdapter;
import commons.gui.model.table.AttributesRowAdapter;
import commons.gui.model.table.TableRowAdapter;
import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.properties.EnumProperty;

public class GenericTable extends TableViewer {

	public static final int DEFAULT_TABLE_STYLE = SWT.FULL_SELECTION | SWT.BORDER;

	public GenericTable(final Composite parent, final Class clazz, EnumProperty tableName, Object elements,
			boolean sorteable, int style) {
		this(parent, style, clazz, tableName, sorteable);
		super.setInput(elements);

		// Si la tabla tiene un único ítem, entonces seleccionarlo
		if (getTable().getItemCount() > 0) {
			getTable().select(0);
		}
		refresh();
	}

	public GenericTable(TableMetainfo info) {
		this(info.parent, info.tableStyle, info.itemClass, info.tableName, info.sorteable);

		info.bindingInfo.bind(this);

		// Si la tabla tiene un único ítem, entonces seleccionarlo
		if (getTable().getItemCount() > 0) {
			getTable().select(0);
		}
		refresh();
	}

	public GenericTable(Composite parent, Class clazz, EnumProperty tableName, Object elements, boolean sorteable) {
		this(parent, clazz, tableName, elements, sorteable, DEFAULT_TABLE_STYLE);
	}

	public GenericTable(Composite composite, int style, Class itemClass, EnumProperty tableName, boolean sorteable) {
		super(composite, style);
		init(new AttributesRowAdapter(itemClass, tableName), sorteable, tableName);
	}

	public GenericTable(Composite composite, int style, TableRowAdapter rowAdapter, EnumProperty tableName,
			boolean sorteable) {
		super(composite, style);
		init(rowAdapter, sorteable, tableName);
	}

	public GenericTable(Composite composite, AbstractRowAdapter rowAdapter, boolean sorteable) {
		super(composite);
		init(rowAdapter, sorteable, rowAdapter.getTableName());
	}

	/**
	 * Por defecto, es ordenable.
	 */
	public GenericTable(Composite composite, AbstractRowAdapter rowAdapter) {
		super(composite);
		init(rowAdapter, true, rowAdapter.getTableName());
	}

	/**
	 * Devuelve el primer elemento seleccionado o null si no hay ningún elemento seleccionado
	 */
	public Object getSelectedElement() {
		Object result = null;
		List selectionFromWidget = this.getSelectionFromWidget();
		if (!selectionFromWidget.isEmpty()) {
			result = selectionFromWidget.get(0);
		}
		return result;
	}

	/**
	 * Devuelve todos los elementos seleccionados.
	 * 
	 * @return
	 */
	public List getSelectedElements() {
		return this.getSelectionFromWidget();
	}

	/**
	 * Selecciona los elementos de la lista.
	 * 
	 * @param list
	 */
	public void setSelectedElements(List list) {
		this.setSelectionToWidget(list, true);
	}

	public TableRowAdapter getRowAdapter() {
		return rowAdapter;
	}

	/**
	 * Obtiene la lista de elementos chequeados
	 * 
	 * @return
	 */
	public List getCheckedItems() {
		TableItem[] items = this.getTable().getItems();

		ArrayList<Object> list = new ArrayList<Object>();

		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];

			if (item.getChecked()) {
				final Object e = item.getData();
				if (e != null) {
					list.add(e);
				}
			}
		}
		return list;
	}

	public void setFilterPredicate(final Predicate predicate) {
		final ViewerFilter filter = new ViewerFilter() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return predicate.evaluate(element);
			}

		};
		this.setFilters(new ViewerFilter[] { filter });
	}

	/**
	 * Setea la lista de elementos chequeados.
	 * 
	 * @param checkedList
	 */
	public void setCheckedItems(List checkedList) {
		TableItem[] items = this.getTable().getItems();

		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			Object e = item.getData();
			if (e != null && checkedList.indexOf(e) >= 0) {
				item.setChecked(true);
			} else {
				item.setChecked(false);
			}
		}
	}

	public void addAdditionalButton(Button button) {
		addAdditionalButton(button, Predicate.NOT_NULL);
	}

	public void addAdditionalButton(Button button, Predicate predicate) {
		List<Predicate> predicates = this.buttonPredicates.get(button);
		if (predicates == null) {
			predicates = new ArrayList<Predicate>();
		}
		buttonPredicates.put(button, predicates);
		predicates.add(predicate);

		applyEnabledState(button, predicates);
	}

	public GenericContentProvider getGenericContentProvider() {
		return (GenericContentProvider) this.getContentProvider();
	}

	public void setVisibleRows(int n) {
		int itemHeight = this.getTable().getItemHeight();
		int headerHeight = this.getTable().getHeaderHeight();
		int heigthHint = (itemHeight * n) + headerHeight;

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = heigthHint;
		this.getTable().setLayoutData(gridData);
	}

	@SuppressWarnings("unchecked")
	protected void enableButtons(Object element) {
		for (Entry<Button, List<Predicate>> e : this.buttonPredicates.entrySet()) {
			applyEnabledState(e.getKey(), e.getValue());
		}
	}

	protected void enableButtons(boolean enabled) {
		for (Button b : buttonPredicates.keySet()) {
			b.setEnabled(enabled);
		}
	}

	protected void applyEnabledState(Button button) {
		this.applyEnabledState(button, Arrays.asList(new Predicate[] { Predicate.TRUE }));
	}

	@SuppressWarnings("unchecked")
	protected void applyEnabledState(Button button, List<Predicate> predicates) {
		Object o = this.getSelectedElement();

		boolean enabled = true;
		for (Predicate p : predicates) {
			if (!p.evaluate(o)) {
				enabled = false;
				break;
			}
		}
		button.setEnabled(enabled);
	}

	@SuppressWarnings("unchecked")
	private void init(TableRowAdapter aRowAdapter, boolean sorteable, EnumProperty tableName) {
		GC gc = new GC(Display.getCurrent());
		gc.setFont(this.getTable().getFont());

		this.rowAdapter = aRowAdapter;

		this.setContentProvider(new GenericContentProvider());
		this.setLabelProvider(new GenericLabelProvider(aRowAdapter));

		this.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		this.getTable().setHeaderVisible(true);
		this.getTable().setLinesVisible(true);

		this.addSelectionListeners();

		if (sorteable) {
			setSorter(getSorterImpl());

			int c = this.rowAdapter.getSortColumn();
			if (c >= 0) {
				getSorterImpl().doSort(c);
			}
		}

		// agrego las columnas
		for (int i = 0; i < aRowAdapter.getColumnCount(); i++) {

			int alignementStyle = SWT.LEFT;

			final EditConfiguration eC = EditConfigurationManager.getInstance().getConfiguration(
					new EditType(aRowAdapter.getColumnClass(i), aRowAdapter.getColumnEditConfiguration(i)));

			if (eC != null && eC.isRightAligned()) {
				alignementStyle = SWT.RIGHT;
			}

			// TODO: en swt la primera columna es siempre "left aligned" en este
			// caso si la primera columna es rigth-aligned podriamos crear un
			// columna de ancho "0"
			int style = i == 0 ? SWT.NULL : alignementStyle;
			final TableColumn column = new TableColumn(this.getTable(), style);
			column.setAlignment(alignementStyle);

			// HACK: Se setea para recibir el nombre en los eventos generados
			// por la columna
			column.setData(aRowAdapter.getColumnKey(i));

			int width = aRowAdapter.getColumnWidth(i);

			final String columnTitle = aRowAdapter.getColumnTitle(i);
			column.setText(columnTitle);

			if (width <= 0) {
				// calcula el tamaño inicial de la columna
				final int headerWidth = gc.stringExtent(columnTitle).x;
				final int protoWidth = calcPrototypeWidth(aRowAdapter, i, gc);

				width = Math.max(headerWidth, protoWidth) + COLUMN_MARGINS;
			}
			column.setWidth(width);
			if (sorteable) {
				final int columnIndex = i;
				column.addSelectionListener(new SelectionColumnAdapter(tableName, columnIndex, this));
				column.addControlListener(new SizeControlAdapter(tableName));
			}

		}
		gc.dispose();
	}

	@SuppressWarnings( { "unchecked", "unchecked" })
	private int calcPrototypeWidth(TableRowAdapter aRowAdapter, int i, GC gc) {
		Object proto = null;
		String protoText = null;

		final EditConfiguration eC = EditConfigurationManager.getInstance().getConfiguration(
				new EditType(aRowAdapter.getColumnClass(i), aRowAdapter.getColumnEditConfiguration(i)));

		if (eC != null) {
			proto = eC.getColumnPrototype();
		}

		if (eC != null && proto != null && eC.getFormat() != null) {
			protoText = eC.getFormat().format(proto);
		}

		if (protoText != null) {
			Point p = gc.stringExtent(protoText);
			return p.x;
		}

		return -1;
	}

	private void addSelectionListeners() {
		// SelectionAdapter that enables/disables buttons
		getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Table table = (Table) event.widget;

				if (table.getSelectionCount() == 0) {
					enableButtons(null);
				} else {
					enableButtons(getSelectedElement());
				}
			}
		});

		addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				enableButtons(getSelectedElement());
			}
		});
	}

	protected void removeAdditionalButton(Button button) {
		buttonPredicates.remove(button);
	}

	private GenericTableViewerSorter getSorterImpl() {
		if (sorter == null) {
			sorter = new GenericTableViewerSorter(rowAdapter);
		}
		return sorter;
	}

	private HashMap<Button, List<Predicate>> buttonPredicates = new HashMap<Button, List<Predicate>>();

	private static int COLUMN_MARGINS = 20;

	private GenericTableViewerSorter sorter;

	private TableRowAdapter rowAdapter;
}
