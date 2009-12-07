/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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
 * Modela un di�logo b�sico d�nde se puede saber si el mismo corresponde a un di�logo de Alta,
 * Edici�n u otro tipo , determinado por un tipo enumerado adecuado para tal fin.
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
	 * Indica el estado de readOnly. Por defecto, el mismo est� dado por el prop�sito, aunque dicho
	 * comportamiento puede ser sobreescrito ante la necesidad de evaluar m�s condiciones
	 * dependientes del modelo.
	 * @param model
	 *            el modelo involucrado en la apeertura del di�logo.
	 * @param emesProposito
	 *            el pro�sito con el que se abre el di�logo.
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
	 * Provee el texto para el bot�n "Cancelar". Por defecto, este valor est� dado por el prop�sito,
	 * siempre y cuando no sea nulo. Si dicho valor fuera nulo, y no existe el bot�n "Aceptar", el
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