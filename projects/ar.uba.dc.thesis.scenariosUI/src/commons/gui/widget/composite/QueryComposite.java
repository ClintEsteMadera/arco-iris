package commons.gui.widget.composite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import commons.auth.AuthorizationHelper;
import commons.gui.GuiStyle;
import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.model.ValueHolder;
import commons.gui.model.ValueModel;
import commons.gui.model.bean.BeanModel;
import commons.gui.table.GenericTable;
import commons.gui.util.ListenerHelper;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.creation.binding.Binding;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.CalendarMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.ComboValuesMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.CalendarFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.LabelFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.gui.widget.group.query.FilterButtonsGroup;
import commons.properties.CommonLabels;
import commons.properties.CommonMessages;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;
import commons.utils.ClassUtils;
import commons.utils.DateUtils;

/**
 * Composite base para todas las consultas que tengan un filtro sobre lo consultado y acciones a realizar sobre los
 * items seleccionados.
 */
public abstract class QueryComposite<T> extends Composite {

	private EnumProperty tableName;

	private Class<T> tableContentClass;

	private FilterButtonsGroup filterButtonsGroup;

	private Menu contextMenu;

	private SimpleComposite leftButtonBarComposite;

	private SimpleComposite rightButtonBarComposite;

	private Button editButton;

	private Button viewButton;

	private Button deleteButton;

	private Button closeButton;

	private BeanModel<BaseSearchCriteria<T>> wrappedCriteria;

	private GenericTable<T> table;

	private ValueModel<String> informationText;

	private static AuthorizationHelper authHelper = PageHelper.getMainWindow().getAuthorizationHelper();

	private static final Log log = LogFactory.getLog(QueryComposite.class);

	public QueryComposite(Composite parent, EnumProperty tableName, Class<T> tableElementsClassName,
			BaseSearchCriteria<T> searchCriteria) {
		super(parent, SWT.NONE);
		this.tableName = tableName;
		this.tableContentClass = tableElementsClassName;
		this.wrappedCriteria = new BeanModel<BaseSearchCriteria<T>>(searchCriteria);
		this.informationText = new ValueHolder<String>("");
		this.init();
		this.reset();
	}

	public GenericTable<T> getTable() {
		return this.table;
	}

	/**
	 * Refresca la consulta en la fila correspondiente al objeto con id igual al parámetro <code>id</code>.
	 * 
	 * @param id
	 *            el identificador del ítem que hay que refrescar.
	 */
	public void reset(Long id) {
		this.getCriteria().setId(id);
		// "Search" simulation
		SelectionListener filterListener = this.getFilterListener();
		if (filterListener != null) {
			filterListener.widgetSelected(null);
		}
	}

	/**
	 * Su utilidad es limpiar los filtros y actualizar la consulta del QueryComposite.
	 */
	public void reset() {
		// "Clean filters" simulated
		SelectionListener cleanUpListener = this.getCleanUpListener();
		if (cleanUpListener != null) {
			cleanUpListener.widgetSelected(null);
		}
		// "Search" simulation
		SelectionListener filterListener = this.getFilterListener();
		if (filterListener != null) {
			filterListener.widgetSelected(null);
		}
	}

	public BaseSearchCriteria<T> getCriteria() {
		return this.getCriterioWrapped().getValue();
	}

	public BeanModel<BaseSearchCriteria<T>> getCriterioWrapped() {
		return wrappedCriteria;
	}

	public T getModel() {
		return getTable().getSelectedElement();
	}

	public List<T> getSelectedElements() {
		return getTable().getSelectedElements();
	}

	/**
	 * Método de conveniencia. Provee acceso a la única instancia de AuthorizationHelper.
	 */
	protected AuthorizationHelper getAuthorizationHelper() {
		return authHelper;
	}

	/**
	 * Agrega un item al menú contextual de la tabla, utilizando el texto y la acción asociada al botón pasado por
	 * parámetro.
	 * 
	 * @param button
	 *            el botón del cual extraer el texto y la acción a realizar cuando se seleccione el MenuItem agregado al
	 *            menú contextual.
	 */
	protected void addMenuItem2MenuContextual(final Button button) {
		MenuItem item = new MenuItem(contextMenu, SWT.PUSH);
		item.setText(button.getText());
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				button.notifyListeners(SWT.Selection, null);
			}
		});
	}

	/**
	 * Habilita o deshabilita el botón pasado por parámetro, si el mismo no es nulo. También se actualiza el ítem del
	 * menú contextual correspondiente (de existir el mismo).
	 * 
	 * @param button
	 *            el botón al cuál cambiar el estado.
	 * @param enabledState
	 *            <code>true</code> indica que el control debe habilitarse, <code>false</code> indica que debe
	 *            deshabilitarse.
	 */
	protected void setEnabledState(Button button, boolean enabledState) {
		if (button != null) {
			button.setEnabled(enabledState);
			MenuItem menuItem = getMenuContextualItem(button.getText());
			if (menuItem != null) {
				menuItem.setEnabled(enabledState);
			}
		}
	}

	/**
	 * La subclases deberán sobreescribir este método para especificar otro estilo distinto para la tabla.
	 */
	protected int getTableStyle() {
		return GenericTable.DEFAULT_TABLE_STYLE;
	}

	/**
	 * Realiza la consulta definida en <code>getBackgroundThread</code> y actualiza la tabla.
	 */
	protected final void doQuery() {
		boolean justRefreshOneSingleItem = this.getCriteria().getId() != null;

		if (justRefreshOneSingleItem) {
			List<T> refreshList = this.executeQuery(); // ask the persistence layer just for the element that changed
			this.refreshItemsAccordingTo(refreshList);
			this.getCriteria().setId(null);
		} else {
			// refresh the entire table
			List<T> queryResult = this.executeQuery();
			getTable().setInput(queryResult);

			if (showMessageAfterQuery()) {
				String message;
				switch (queryResult.size()) {
				case 0:
					message = CommonMessages.QUERY_NO_RESULTS.toString();
					break;
				case 1:
					message = CommonMessages.QUERY_ONLY_ONE_RESULT.toString(queryResult.size());
					break;
				default:
					message = CommonMessages.QUERY_MORE_THAN_ONE_RESULT.toString(queryResult.size());
					break;
				}
				this.informationText.setValue("[" + DateUtils.getCurrentTimeAsString() + "] " + message);
			}
		}
	}

	/**
	 * This method can be overriden in order to disable the message shown after the query is completed
	 * 
	 * @return
	 */
	protected boolean showMessageAfterQuery() {
		return true;
	}

	/**
	 * Por defecto, se agregan los botones de "Nuevo", "Edición", "Borrar" y "Cerrar", si es que están soportados por el
	 * QueryComposite concreto.
	 */
	protected void addButtons() {
		if (newButtonAllowed()) {
			this.editButton = addButton(getActionForNew());
		}
		if (editButtonAllowed()) {
			this.editButton = addButton(getActionForEdit());
		} else if (viewButtonAllowed()) {
			this.viewButton = addButton(getActionForView());
		}
		if (deleteButtonAllowed()) {
			this.deleteButton = addButton(getActionForDelete());
		}
		if (closeButtonAllowed()) {
			this.addCloseButton();
		}
	}

	/**
	 * Método para agregar fácilmente botones al QueryComposite, considerando la autorización pertinente al usuario
	 * conectado. El método está pensado para no ser reescrito porque se quiere garantizar el chequeo de permisos.
	 * 
	 * @param action
	 *            la acción asociada al botón
	 * @return el botón recién agregado o <code>null</code> si el usuario conectado no posee permisos para efectuar la
	 *         acción especificada por parámetro.
	 */
	protected final <P extends Purpose> Button addButton(OpenDialogWithPurposeAction<T, P> action) {
		Button button = null;
		if (getAuthorizationHelper().isUserAuthorized(action)) {
			button = new Button(this.leftButtonBarComposite, SWT.PUSH | SWT.CENTER);
			Purpose purpose = action.getPurpose();

			button.setText(purpose.getTextForLauncherButton().toString());
			button.setFont(JFaceResources.getDialogFont());
			button.setEnabled(purpose.getLauncherButtonInitialEnabledState());
			button.addSelectionListener(getButtonSelectionListenerFor(action));

			this.addMenuItem2MenuContextual(button);
			DefaultLayoutFactory.setButtonRowLayoutData(button);
		}
		return button;
	}

	protected CalendarComposite agregarFiltroPorFecha(Composite parent, EnumProperty label, String propertyName) {
		BindingInfo bindingInfo = new BindingInfo(this.getCriterioWrapped(), propertyName);
		CalendarMetainfo metainfo = CalendarMetainfo.create(parent, label, bindingInfo, false);
		return CalendarFactory.createCalendar(metainfo);
	}

	protected Text agregarFiltroNumero(Composite composite, EnumProperty label, String propertyName) {
		LabelFactory.createLabel(composite, label, false, true);
		Text text = new Text(composite, GuiStyle.DEFAULT_TEXTBOX_STYLE);
		ListenerHelper.addIntegerFieldKeyListener(text);
		return text;
	}

	protected Text agregarFiltroTexto(Composite parent, EnumProperty label, String propertyName) {
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		gridData.widthHint = PageHelper.getCantidadDePixels(20);
		return agregarFiltroTexto(parent, label, propertyName, gridData);
	}

	protected Text agregarFiltroTexto(Composite parent, EnumProperty label, String propertyName, GridData gridData) {
		Binding binding = new BindingInfo(this.getCriterioWrapped(), propertyName);
		TextFieldMetainfo metainfo = TextFieldMetainfo.create(parent, label, binding, false);
		Control control = TextFactory.createText(metainfo);
		control.setLayoutData(gridData);
		return (Text) control;
	}

	protected Combo agregarFiltroCombo(Composite composite, EnumProperty label, String propertyName) {
		return this.agregarFiltroCombo(composite, label, propertyName, null);
	}

	protected Combo agregarFiltroCombo(Composite composite, EnumProperty label, String propertyName,
			ComboValuesMetainfo comboValuesMetainfo) {

		Binding binding = new BindingInfo(this.getCriterioWrapped(), propertyName);
		ComboMetainfo metainfo = ComboMetainfo.create(composite, label, binding, false);

		if (comboValuesMetainfo != null && comboValuesMetainfo.useStringItems()) {
			metainfo.items = comboValuesMetainfo.items;
		}
		// TODO: Hay casos dónde el combo tiene un solo elemento que no se estan considerando...
		boolean tieneUnSoloElemento = metainfo.items != null && metainfo.items.length == 1;
		metainfo.addEmptyOption = !tieneUnSoloElemento;

		Combo combo = (Combo) ComboFactory.createCombo(metainfo);
		combo.setEnabled(!tieneUnSoloElemento);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return combo;
	}

	protected SelectionListener getFilterListener() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				doQuery();
			}
		};
		return listener;
	}

	/**
	 * Método que generaliza la limpieza de filtros.
	 * 
	 * @return un listener que se activará cuando se seleccione el botón que limpia los filtros.
	 */
	protected SelectionListener getCleanUpListener() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				for (Control control : getFilterControls()) {
					if (control instanceof Text) {
						if (control.isEnabled()) {
							((Text) control).setText("");
						}
					} else if (control instanceof Combo) {
						if (control.isEnabled()) {
							((Combo) control).select(0);
						}
					} else if (control instanceof CalendarComposite) {
						if (control.isEnabled()) {
							((CalendarComposite) control).cleanText();
						}
					} else {
						log.fatal("Tipo de Control no soportado! Imposible limpiar control!");
					}
				}
			}
		};
	}

	protected <P extends Purpose> SelectionListener getButtonSelectionListenerFor(
			final OpenDialogWithPurposeAction<T, P> action) {
		return new SelectionAdapter() {
			@Override
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				action.getActionFor(getModel()).run();
				if (resetAfterAnyActionFinishedExecution()) {
					QueryComposite.this.reset();
				}
			}
		};
	}

	protected ISelectionChangedListener getEnableButtonsSelectionChangedListener() {
		return new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				boolean thereIsSomethingSelected = !getTable().getSelectedElements().isEmpty();

				setEnabledState(viewButton, thereIsSomethingSelected);
				setEnabledState(editButton, thereIsSomethingSelected);
				setEnabledState(deleteButton, thereIsSomethingSelected);
			}
		};
	}

	/**
	 * Método encargado de crear el DoubleClickListener que utilizará la tabla. Por defecto se lanzará la vista o
	 * edición (de acuerdo a los permisos que el usuario posea para efectuar dichas operaciones)
	 * 
	 * @return DoubleClickListener que utilizará la tabla
	 */
	protected IDoubleClickListener getTableDoubleClickListener() {
		return new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if (editButtonAllowed()) {
					editButton.notifyListeners(SWT.Selection, null);
				} else if (viewButtonAllowed()) {
					viewButton.notifyListeners(SWT.Selection, null);
				}
			}
		};
	}

	/**
	 * Obtiene el listener para el cambio en la selección de la tabla asociada al QueryComposite.
	 */
	protected abstract ISelectionChangedListener getTableSelectionChangedListener();

	/**
	 * Con este método se indica si se permite el botón "Nuevo". Esto deberá decidirlo cada una de las subclases
	 * considerando requerimientos funcionales y permisos del usuario para efectuar dicha operación.
	 */
	protected abstract boolean newButtonAllowed();

	/**
	 * Con este método se indica si se permite el botón "Editar". Esto deberá decidirlo cada una de las subclases
	 * considerando requerimientos funcionales y permisos del usuario para efectuar dicha operación.
	 */
	protected abstract boolean editButtonAllowed();

	/**
	 * Con este método se indica si se permite el botón "Ver". Esto deberá decidirlo cada una de las subclases
	 * considerando requerimientos funcionales y permisos del usuario para efectuar dicha operación.
	 */
	protected abstract boolean viewButtonAllowed();

	/**
	 * Con este método se indica si se permite el botón "Eliminar". Esto deberá decidirlo cada una de las subclases
	 * considerando requerimientos funcionales y permisos del usuario para efectuar dicha operación.
	 */
	protected abstract boolean deleteButtonAllowed();

	/**
	 * Con este método se indica si se permite el botón "Cerrar". Esto deberá decidirlo cada una de las subclases
	 * considerando requerimientos funcionales y permisos del usuario para efectuar dicha operación.
	 */
	protected abstract boolean closeButtonAllowed();

	/**
	 * Provee la acción con el comportamiento concreto que la aplicación le da al término "Nuevo"
	 */
	protected abstract <P extends Purpose> OpenDialogWithPurposeAction<T, P> getActionForNew();

	/**
	 * Provee la acción con el comportamiento concreto que la aplicación le da al término "Editar"
	 */
	protected abstract <P extends Purpose> OpenDialogWithPurposeAction<T, P> getActionForEdit();

	/**
	 * Provee la acción con el comportamiento concreto que la aplicación le da al término "Ver"
	 */
	protected abstract <P extends Purpose> OpenDialogWithPurposeAction<T, P> getActionForView();

	/**
	 * Provee la acción con el comportamiento concreto que la aplicación le da al término "Eliminar"
	 */
	protected abstract <P extends Purpose> OpenDialogWithPurposeAction<T, P> getActionForDelete();

	/**
	 * This method specified whether a composite for search filters should be drawn.
	 * 
	 * @return <code>true</code>, if the filter composite should be created. <code>false</code> otherwise.
	 */
	protected abstract boolean showFilters();

	/**
	 * Agrega los filtros específicos.
	 * 
	 * @param grupoFiltros
	 *            el grupo sobre el cual se agregarán dichos filtros.
	 */
	protected abstract void addSpecificFilters(Group grupoFiltros);

	/**
	 * @return todos los controles de filtro (Texts, combos, etc)
	 */
	protected abstract List<Control> getFilterControls();

	/**
	 * Permite especificar si se desea que se resetee la consulta después de que cualquier acción iniciada por algunos
	 * de los botones asociados a esta consulta han terminado su trabajo (e.g. después de una edición o creacion de un
	 * objeto)
	 * 
	 * @return <code>true</code> si se desea que se resetee la consulta en tal caso, <code>false</code> en caso
	 *         contrario.
	 */
	protected abstract boolean resetAfterAnyActionFinishedExecution();

	/**
	 * Ejecuta la consulta asociada a este QueryComposite.
	 * 
	 * @return una lista con el resultado de la consulta.
	 */
	protected abstract List<T> executeQuery();

	private QueryComposite<T> init() {
		this.setLayout(new GridLayout());
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		if (this.showFilters()) {
			this.addFiltersComposite(this);
		}
		this.addTable(this);
		if (this.anyButtonAllowed()) {
			this.configureButtonBar();
		}
		this.pack();
		return this;
	}

	/**
	 * Crea el composite de filtrado respectivo a la consulta a implementar.
	 * 
	 * @param parent
	 */
	private void addFiltersComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		addSpecificFilters((new SimpleGroup(composite, CommonLabels.FILTERS, false)).getSwtGroup());
		filterButtonsGroup = new FilterButtonsGroup(composite, getFilterListener(), getCleanUpListener());
		addEnterKeySelectionListener(getFilterControls());
	}

	/**
	 * En este método se crea el objeto GenericTable con todos sus datos correspondientes
	 * 
	 * @param parent
	 *            composite sobre el cuál se creará la tabla.
	 */
	private void addTable(Composite parent) {
		GenericTable<T> newTable = new GenericTable<T>(parent, tableContentClass, tableName, null, true, this
				.getTableStyle());

		this.createMenuContextual(parent);
		newTable.getTable().setMenu(this.contextMenu);

		ISelectionChangedListener[] listeners = { this.getEnableButtonsSelectionChangedListener(),
				this.getTableSelectionChangedListener() };

		for (ISelectionChangedListener listener : listeners) {
			if (listener != null) {
				newTable.addSelectionChangedListener(listener);
			}
		}

		IDoubleClickListener tableDoubleClickListener = getTableDoubleClickListener();
		if (tableDoubleClickListener != null) {
			newTable.addDoubleClickListener(tableDoubleClickListener);
		}
		this.table = newTable;
	}

	/**
	 * Agrega a todos los controles el listener para que al presionar <code>ENTER</code> sobre ellos se dispare la
	 * consulta.
	 * 
	 * @param controls
	 *            Controles a los que se le agregará el listener.
	 */
	private void addEnterKeySelectionListener(List<Control> controls) {
		for (Control control : controls) {
			if (control != null) {
				KeyListener filterButtonListener = new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent event) {
						if (event.keyCode == SWT.Selection || event.keyCode == SWT.KEYPAD_CR) {
							filterButtonsGroup.getFilterButton().notifyListeners(SWT.Selection, null);
						}
					}
				};
				control.addKeyListener(filterButtonListener);
			}
		}
	}

	/**
	 * Crea un menú contextual vacío.
	 * 
	 * @param parent
	 */
	private void createMenuContextual(Composite parent) {
		this.contextMenu = new Menu(parent);
		this.contextMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				// Si no se seleccionó nada, todos los items se deshabilitan
				if (getTable().getSelectedElements().isEmpty()) {
					for (MenuItem menuItem : contextMenu.getItems()) {
						menuItem.setEnabled(false);
					}
				}
			}
		});
	}

	/**
	 * Provee el ítem de menú que posee el texto pasado por parámetro.
	 * 
	 * @param text
	 *            el texto que debe tener el ítem buscado.
	 * @return el ítem de menú que posee el texto pasado por parámetro en caso de existir un ítem con dicho texto o
	 *         <code>null</code> en caso de no encontrar ninguno.
	 */
	private MenuItem getMenuContextualItem(String text) {
		MenuItem result = null;
		for (MenuItem menuItem : this.contextMenu.getItems()) {
			if (menuItem.getText().equals(text)) {
				result = menuItem;
				break;
			}
		}
		return result;
	}

	private void configureButtonBar() {
		SimpleComposite buttonBarsParent = new SimpleComposite(this.getTable().getControl().getParent(), false, 2);

		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.spacing = 5;

		this.leftButtonBarComposite = new SimpleComposite(buttonBarsParent, false, 1);
		this.leftButtonBarComposite.setLayout(layout);

		this.rightButtonBarComposite = new SimpleComposite(buttonBarsParent, false, 2);

		LabelFactory.createValue(this.rightButtonBarComposite, new BindingInfo(this.informationText), false);

		this.addButtons();
	}

	@SuppressWarnings("unchecked")
	private void refreshItemsAccordingTo(List refreshList) {
		List<T> tableInput = (List<T>) getTable().getInput();

		if (tableInput != null) {
			Long idItemARefrescar = this.getCriteria().getId();
			if (refreshList.isEmpty()) {
				// delete tableItem with id equals to "idItemARefrescar"
				for (T item : tableInput) {
					if (ClassUtils.getObject(item, "id").equals(idItemARefrescar)) {
						tableInput.remove(item);
						break;
					}
				}
			} else {
				// update tableItem with the new version
				int i = 0;
				while (i < tableInput.size()) {
					if (ClassUtils.getObject(tableInput.get(i), "id").equals(idItemARefrescar)) {
						tableInput.set(i, (T) refreshList.get(0));
						break;
					}
					i++;
				}
				if (i == tableInput.size()) { // the element is new and it isn't in the table...
					tableInput.add((T) refreshList.get(0));
				}
			}
			getTable().refresh();
		}
	}

	private void addCloseButton() {
		this.closeButton = new Button(this.rightButtonBarComposite, SWT.CENTER);
		DefaultLayoutFactory.setButtonGridLayoutData(this.closeButton);
		this.closeButton.setText(CommonLabels.CLOSE.toString());
		this.closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabFolder mainTabFolder = PageHelper.getMainWindow().mainTabFolder;
				if (mainTabFolder.equals(QueryComposite.this.getParent())) {
					if (mainTabFolder.getItemCount() == 1) {
						mainTabFolder.setVisible(false);
					}
					PageHelper.getMainWindow().getTabItem(tableName).dispose();
				}
			}
		});
	}

	private boolean anyButtonAllowed() {
		return newButtonAllowed() || editButtonAllowed() || viewButtonAllowed() || deleteButtonAllowed()
				|| closeButtonAllowed();
	}
}