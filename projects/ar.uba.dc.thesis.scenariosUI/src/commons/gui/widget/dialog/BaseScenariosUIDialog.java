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
 * $Id: BaseEmesDialog.java,v 1.15 2008/05/16 20:37:42 cvschioc Exp $
 */
package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.MessageDialog;

import sba.common.properties.EnumProperty;

import commons.gui.model.CompositeModel;
import commons.gui.model.bean.BeanModel;
import commons.gui.widget.dialog.BasePreferenceDialog;
import commons.properties.CommonLabels;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.15 $ $Date: 2008/05/16 20:37:42 $
 */
public abstract class BaseScenariosUIDialog<T> extends BasePreferenceDialog {

	public BaseScenariosUIDialog(T model, EnumProperty title, boolean readOnly) {
		super(null, title);
		this.readOnly = readOnly;
		if (model == null) {
			model = newModel();
		}
		this.compositeModel = new BeanModel<T>(model);
	}

	public CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}
	
	public T getModel() {
		return this.compositeModel.getValue();
	}
	
	protected void mostrarDialogoOperacionExitosa(String mensaje) {
		MessageDialog.openInformation(null, CommonLabels.SUCCESSFUL_OPERATION.toString(), mensaje);
	}

	protected abstract T newModel();

	protected boolean readOnly;

	private CompositeModel<T> compositeModel;
}