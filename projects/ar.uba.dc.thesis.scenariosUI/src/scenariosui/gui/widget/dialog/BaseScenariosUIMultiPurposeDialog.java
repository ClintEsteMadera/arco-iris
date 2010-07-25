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

package scenariosui.gui.widget.dialog;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.ScenariosUIWindow;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.common.ThesisPojo;

import commons.exception.ApplicationException;
import commons.exception.ServiceException;
import commons.gui.background.BackgroundInvocationException;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

/**
 * Modela un di�logo b�sico d�nde se puede saber si el mismo corresponde a un di�logo de Alta, Edici�n u otro tipo ,
 * determinado por un tipo enumerado adecuado para tal fin.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.8 $ $Date: 2008/02/26 14:45:34 $
 */

public abstract class BaseScenariosUIMultiPurposeDialog<T extends ThesisPojo> extends BaseCompositeModelBoundedDialog<T> {

	protected ScenariosUIPurpose purpose;

	public BaseScenariosUIMultiPurposeDialog(T model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, new FakeEnumProperty(title.toString() + " - " + purpose.toString()), purpose.isReadOnly());
		super.readOnly = this.isReadOnly(super.getModel(), purpose);
		this.purpose = purpose;
	}

	/**
	 * Adds the current model of this dialog to the current self healing configuration
	 */
	public abstract void addModelToCurrentSHConfiguration();

	/**
	 * Obtains the message to be used for displaying the "successful operation message" to the user
	 * 
	 * @param operation
	 *            the operation that is taking place
	 * @return the message to be used for displaying the "successful operation message" to the user
	 */
	public abstract String getSuccessfulOperationMessage(String operation);

	/**
	 * Provides the unique identifier of the table that contains the type of elements modified on this dialog
	 * 
	 * @return the unique identifier of the table that contains the type of elements modified on this dialog
	 */
	public abstract UniqueTableIdentifier getUniqueTableIdentifier();

	/**
	 * Indica el estado de readOnly. Por defecto, el mismo est� dado por el prop�sito, aunque dicho comportamiento puede
	 * ser sobreescrito ante la necesidad de evaluar m�s condiciones dependientes del modelo.
	 * 
	 * @param model
	 *            el modelo involucrado en la apertura del di�logo.
	 * @param scenariosUIPurpose
	 *            el pro�sito con el que se abre el di�logo.
	 */
	protected boolean isReadOnly(T model, ScenariosUIPurpose scenariosUIPurpose) {
		return scenariosUIPurpose.isReadOnly();
	}

	@Override
	protected boolean performOK() {
		String operation = null;
		try {
			ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();
			switch (this.purpose) {
			case CREATION:
				operation = "created";
				this.addModelToCurrentSHConfiguration();
				scenariosUIController.saveSelfHealingConfiguration();
				break;
			case EDIT:
				operation = "updated";
				scenariosUIController.saveSelfHealingConfiguration();
				break;
			default:
				throw new RuntimeException("The code does not contemplate the purpose " + this.purpose + "yet");
			}
		} catch (BackgroundInvocationException ex) {
			// Se supone que se atrapa m�s arriba esta excepci�n...
			throw ex;
		} catch (ServiceException ex) {
			// Esta jerarqu�a de excepciones ya viene preparada para ser
			// mostradas
			throw ex;
		} catch (Exception ex) {
			throw new ApplicationException("Error when accesing the service", ex);
		}
		showSuccessfulOperationDialog(this.getSuccessfulOperationMessage(operation));
		ScenariosUIWindow.getInstance().resetQuery(getUniqueTableIdentifier(), super.getModel().getId());

		return true;
	}

	@Override
	protected void cancelPressed() {
		if (doIHaveToAbandonChanges()) {
			if (this.purpose.isCreation()) {
				ScenariosUIController.getInstance().returnRecentlyRequestedId(); // since we won't use it
			}
			super.cancelPressed();
		}
	}

	@Override
	protected boolean isOkButtonAllowed() {
		boolean noAcceptButton = (this.isReadOnly(super.getModel(), this.purpose) && this.purpose
				.equals(ScenariosUIPurpose.EDIT))
				|| this.purpose.equals(ScenariosUIPurpose.VIEW);
		return !noAcceptButton;
	}

	@Override
	protected EnumProperty getAcceptButtonText() {
		EnumProperty result = super.getAcceptButtonText();
		EnumProperty acceptEnumProp = this.purpose.getAcceptButtonText();
		if (acceptEnumProp != null) {
			result = acceptEnumProp;
		}
		return result;
	}

	/**
	 * Provee el texto para el bot�n "Cancelar". Por defecto, este valor est� dado por el prop�sito, siempre y cuando no
	 * sea nulo. Si dicho valor fuera nulo, y no existe el bot�n "Aceptar", el texto es el dado por
	 * {@link #CLOSE(CommonLabels)}.<br>
	 * Si no se cumplen las anteriores condiciones, el texto es el dado por la superclase
	 * 
	 * @see {@link #getCancelButtonText()BaseEmesDialog}
	 */
	@Override
	protected EnumProperty getCancelButtonText() {
		EnumProperty result = super.getCancelButtonText();

		EnumProperty cancelEnumProp = this.purpose.getCancelButtonText();
		if (cancelEnumProp != null) {
			result = cancelEnumProp;
		} else if (!this.isOkButtonAllowed()) {
			result = CommonLabels.CLOSE;
		}
		return result;
	}
}