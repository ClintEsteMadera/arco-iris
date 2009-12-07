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
 * $Id: BaseEmesMultiPurposeDialog.java,v 1.8 2008/02/26 14:45:34 cvspasto Exp $
 */

package commons.gui.widget.dialog;

import sba.common.properties.EnumProperty;
import sba.common.properties.FakeEnumProperty;

import commons.gui.util.proposito.ScenariosUIProposito;
import commons.properties.CommonLabels;

/**
 * Modela un diálogo básico dónde se puede saber si el mismo corresponde a un diálogo de Alta,
 * Edición u otro tipo , determinado por un tipo enumerado adecuado para tal fin.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.8 $ $Date: 2008/02/26 14:45:34 $
 */

public abstract class BaseScenariosUIMultiPurposeDialog<T> extends BaseScenariosUIDialog<T> {

	public BaseScenariosUIMultiPurposeDialog(T model, EnumProperty title, ScenariosUIProposito proposito) {
		super(model, new FakeEnumProperty(title.toString() + " - " + proposito.toString()),
				proposito.esReadOnly());
		super.readOnly = this.esReadOnly(super.getModel(), proposito);
		this.proposito = proposito;
	}

	/**
	 * Indica el estado de readOnly. Por defecto, el mismo está dado por el propósito, aunque dicho
	 * comportamiento puede ser sobreescrito ante la necesidad de evaluar más condiciones
	 * dependientes del modelo.
	 * @param model
	 *            el modelo involucrado en la apeertura del diálogo.
	 * @param emesProposito
	 *            el proósito con el que se abre el diálogo.
	 */
	protected boolean esReadOnly(T model, ScenariosUIProposito emesProposito) {
		return emesProposito.esReadOnly();
	}

	@Override
	protected boolean isOkButtonAllowed() {
		boolean sinBotonAceptar = (this.esReadOnly(super.getModel(), this.proposito)
				&& this.proposito.equals(ScenariosUIProposito.EDICION)) ||
				this.proposito.equals(ScenariosUIProposito.VER);
		return !sinBotonAceptar;
	}

	@Override
	protected EnumProperty getAcceptButtonText() {
		EnumProperty result = super.getAcceptButtonText();
		EnumProperty acceptEnumProp = this.proposito.getAcceptButtonText();
		if (acceptEnumProp != null) {
			result = acceptEnumProp;
		}
		return result;
	}

	/**
	 * Provee el texto para el botón "Cancelar". Por defecto, este valor está dado por el propósito,
	 * siempre y cuando no sea nulo. Si dicho valor fuera nulo, y no existe el botón "Aceptar", el
	 * texto es el dado por {@link #CERRAR(CommonLabels)}.<br>
	 * Si no se cumplen las anteriores condiciones, el texto es el dado por la superclase
	 * @see {@link #getCancelButtonText()BaseEmesDialog}
	 */
	@Override
	protected EnumProperty getCancelButtonText() {
		EnumProperty result = super.getCancelButtonText();

		EnumProperty cancelEnumProp = this.proposito.getCancelButtonText();
		if (cancelEnumProp != null) {
			result = cancelEnumProp;
		} else if(!this.isOkButtonAllowed()) {
			result = CommonLabels.CERRAR;
		}
		return result;
	}

	protected ScenariosUIProposito proposito;
}