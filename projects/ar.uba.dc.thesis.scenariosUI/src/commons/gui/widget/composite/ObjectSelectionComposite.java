/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: ObjectSelectionComposite.java,v 1.13 2008/05/15 20:53:42 cvspasto Exp $
 */

package commons.gui.widget.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;

import commons.gui.model.CompositeModel;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.bean.BeanModel;
import commons.gui.model.validation.ValidationManager;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.properties.CommonLabels;
import commons.utils.ClassUtils;

public abstract class ObjectSelectionComposite<T> extends SimpleComposite {

	private BeanModel<T> selectionModel;

	private String descriptionProperty;

	private ToolBar toolbar;

	private ToolItem createItem;

	private ToolItem clearItem;

	private ToolItem selectItem;

	private Hyperlink hiperLink;

	// this field is used to infer the concrete generic parameter of this class
	@SuppressWarnings("unused")
	private T dummyField;

	@SuppressWarnings("unchecked")
	public ObjectSelectionComposite(ObjectSelectionMetainfo info) {
		super(info.parent, info.readOnly, calculateColumns(info), info.columnsToSpan);

		// HACK: infer the concrete generic parameter of this class
		final Class selectionClass = ClassUtils.getField(getClass(), "dummyField").getType();

		this.selectionModel = new BeanModel<T>(selectionClass);
		this.descriptionProperty = info.descriptionProperty;

		info.binding.bind(this.selectionModel.getValueModel(info.selectedProperty));

		addFields(info);
	}

	protected CompositeModel<T> getSelectionModel() {
		return this.selectionModel;
	}

	protected abstract void launchSelection();

	protected abstract void viewObject(T object);

	protected T createNew(Object createOption) {
		return null;
	}

	protected final void setSelectionModel(T value) {
		this.selectionModel.setValue(value);
	}

	/**
	 * Provides the value to be set when clearing. By Default, the value is just <code>null</code>
	 * 
	 * @return the value to be set when clearing.
	 */
	protected T getValueToSetWhenClearing() {
		return null;
	}

	private static int calculateColumns(ObjectSelectionMetainfo info) {
		return info.readOnly ? 2 : 3;
	}

	private void addFields(final ObjectSelectionMetainfo info) {
		if (!CommonLabels.NO_LABEL.equals(info.label)) {
			if (info.canView) {
				this.createLink(info);
			} else {
				this.createLabel(info);
			}
		}
		GridData gridData = DefaultLayoutFactory.getGridData(1);
		gridData.verticalAlignment = SWT.CENTER;
		gridData.horizontalAlignment = SWT.FILL;

		final TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(this, CommonLabels.NO_LABEL, new BindingInfo(
				this.selectionModel, this.descriptionProperty), TextFieldMetainfo.READONLY_STYLE_TEXT);
		textMetainfo.layoutData = gridData;
		Control text = TextFactory.createText(textMetainfo);
		ValidationManager.setValidationProperty(text, info.binding);

		this.toolbar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);

		if (!super.readOnly) {
			createSelectItem();

			if (info.canCreate) {
				createCreateButton(info);
			}
			if (info.nullable) {
				createClearButton();
			}
		}

		this.selectionModel.addValueChangeListener(new ValueChangeListener() {
			@SuppressWarnings("unchecked")
			public void valueChange(ValueChangeEvent ev) {
				updateState();
			}
		});

		updateState();
	}

	private void createLink(final ObjectSelectionMetainfo info) {
		this.hiperLink = new Hyperlink(this, SWT.LEFT);
		hiperLink.setText(info.label.toString() + ":");

		hiperLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkActivated(HyperlinkEvent event) {
				viewObject(selectionModel.getValue());
			}

			public void linkEntered(HyperlinkEvent event) {
			}

			public void linkExited(HyperlinkEvent event) {
			}
		});
	}

	private void createLabel(final ObjectSelectionMetainfo info) {
		Label label = new Label(this, SWT.LEFT);
		label.setText(info.label.toString() + ":");
	}

	private void createCreateButton(final ObjectSelectionMetainfo info) {
		if (info.createOptions != null && info.createOptions.length > 1) {
			this.createItem = new ToolItem(toolbar, SWT.DROP_DOWN);

			final Menu menu = getCreateMenu(info);

			createItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					if (event.detail == SWT.ARROW) {
						Rectangle rect = createItem.getBounds();
						Point pt = new Point(rect.x, rect.y + rect.height);
						pt = toolbar.toDisplay(pt);
						menu.setLocation(pt.x, pt.y);
						menu.setVisible(true);
					} else {
						int i = getCheckeditem(menu);
						if (i < 0) {
							i = 0;
						}
						handleCreation(info.createOptions[i]);
					}
				}
			});

		} else {
			final Object option = info.createOptions != null ? info.createOptions[0] : null;

			this.createItem = new ToolItem(toolbar, SWT.PUSH);

			createItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					handleCreation(option);
				}
			});
		}

		this.createItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/images/nuevo.gif")));
		this.createItem.setToolTipText("Create and assign new element");
	}

	private void createSelectItem() {
		this.selectItem = new ToolItem(toolbar, SWT.PUSH);
		this.selectItem.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(
				"/images/buscar-largavistas.gif")));
		this.selectItem.setToolTipText("Find");
		this.selectItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				launchSelection();
			}
		});
	}

	private void createClearButton() {
		this.clearItem = new ToolItem(toolbar, SWT.PUSH);
		this.clearItem
				.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/images/delete16.gif")));
		this.clearItem.setToolTipText("Clear");

		this.clearItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectionModel.setValue(getValueToSetWhenClearing());
			}
		});
	}

	private void handleCreation(Object createOption) {
		final T newValue = this.createNew(createOption);

		if (newValue != null) {
			this.selectionModel.setValue(newValue);
		}
	}

	private void updateState() {
		boolean enabled = this.selectionModel.getValue() != null;

		if (this.clearItem != null) {
			this.clearItem.setEnabled(enabled);
		}
		if (this.hiperLink != null) {
			this.hiperLink.setUnderlined(enabled);

			final int color = enabled ? SWT.COLOR_BLUE : SWT.COLOR_WIDGET_FOREGROUND;

			this.hiperLink.setForeground(this.getDisplay().getSystemColor(color));
			this.hiperLink.setEnabled(enabled);
		}
	}

	private Menu getCreateMenu(final ObjectSelectionMetainfo info) {
		if (info.createOptions == null) {
			return null;
		}

		final Menu menu = new Menu(info.parent.getShell(), SWT.POP_UP);

		for (int i = 0; i < info.createOptions.length; i++) {
			final Object option = info.createOptions[i];

			final MenuItem item = new MenuItem(menu, SWT.CHECK);
			item.setText(option.toString());

			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					setCheckeditem(menu, item);
					T value = createNew(option);
					if (value != null) {
						selectionModel.setValue(value);
					}
				}
			});
		}
		return menu;
	}

	private void setCheckeditem(Menu menu, MenuItem item) {
		final MenuItem[] items = menu.getItems();

		for (MenuItem i : items) {
			if (i == item) {
				i.setSelection(true);
			} else {
				i.setSelection(false);
			}
		}
	}

	private int getCheckeditem(Menu menu) {
		final MenuItem[] items = menu.getItems();

		for (int i = 0; i < items.length; i++) {
			if (items[i].getSelection()) {
				return i;
			}
		}
		return -1;
	}
}