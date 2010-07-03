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
 * $Id: BaseSelectionPage.java,v 1.12 2007/11/30 20:31:08 cvsmarco Exp $
 */

package commons.gui.widget.page;

import static commons.properties.CommonMessages.NO_SELECTED_ELEMENT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;


import commons.gui.model.CompositeModel;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.factory.LabelFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.EnumProperty;

/**
 * Modela una página genérica de selección de un Objeto de un Query Composite.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.12 $ $Date: 2007/11/30 20:31:08 $
 */

public abstract class BaseSelectionPage<T> extends BasePreferencesPage<T> {

	public BaseSelectionPage(CompositeModel<T> compositeModel, EnumProperty title, boolean readOnly) {
		super(compositeModel, title, readOnly);
	}

	public T getSelectedObject() {
		return super.getModel();
	}

	
	@Override
	protected void addFields(Composite parent) {
		objectSelectionGroup = (new SimpleGroup(parent, getSelectionGroupName(), false, 3))
				.getSwtGroup();

		this.addBrowseButton(objectSelectionGroup);
		this.addUpdatableFields(objectSelectionGroup);
		this.addMessageLabel(objectSelectionGroup);

		refreshSelection();
	}

	@Override
	protected int getNumColumns() {
		return 1;
	}

	private void addBrowseButton(Composite parent) {
		if (!this.readOnly) {
			Button browseObjectButton = new Button(parent, SWT.LEFT);
			browseObjectButton.setText(getSelectObjectMessage());
			browseObjectButton.setAlignment(SWT.CENTER);
			browseObjectButton.addSelectionListener(this.getBrowseObjectButtonSelectionListener());
		}
	}

	protected void addMessageLabel(Composite parent) {
		Composite messageComposite = new SimpleComposite(parent, super.readOnly, 1);
		((GridLayout) messageComposite.getLayout()).marginTop = 10; 
		messageComposite.setLayoutData(new GridData(SWT.CENTER, SWT.DEFAULT, true, false));
		this.messageLabel = LabelFactory.createLabel(messageComposite, NO_SELECTED_ELEMENT, true,
				false);
	}


	private void updateSelection(T selection){
		super.getCompositeModel().setValue(selection);
		
		refreshSelection();
	}
	
	protected void refreshSelection() {
		T selection = getSelectedObject();

		if (selection == null) {
			this.resetUpdatableFields();
			this.messageLabel.setText(NO_SELECTED_ELEMENT.toString());
		} else {
			this.messageLabel.setText("");
		}
	}
	
	private SelectionListener getBrowseObjectButtonSelectionListener() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				updateSelection(obtainSelectedObject());
			}
		};
	}

	/**
	 * Agrega controles que se actualizarán con la elección del objeto en cuestión.
	 * @param parent
	 *            el composite sobre el cual agregar los controles.
	 */
	protected abstract void addUpdatableFields(Composite parent);

	/**
	 * @return el nombre del grupo de selección del objeto
	 */
	protected abstract EnumProperty getSelectionGroupName();

	/**
	 * @return el label que irá en el botón de browse de la página.
	 */
	protected abstract String getSelectObjectMessage();

	/**
	 * @return el compositeModel con el objeto seleccionado por el usuario. Normalmente involucra la
	 *         apertura de un diálogo adecuado para tal fin, aunque la implementación no se
	 *         restringe.
	 */
	protected abstract T obtainSelectedObject();

	/**
	 * Resetea los campos que se actualizan con la selección de un nuevo objeto.
	 */
	protected abstract void resetUpdatableFields();

	private Label messageLabel;
	
	protected Group objectSelectionGroup;
}