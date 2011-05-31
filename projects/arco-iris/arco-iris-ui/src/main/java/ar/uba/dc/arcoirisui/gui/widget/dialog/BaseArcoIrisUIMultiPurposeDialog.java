package ar.uba.dc.arcoirisui.gui.widget.dialog;

import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.common.Identifiable;
import ar.uba.dc.arcoiris.common.validation.Validatable;

import commons.exception.ApplicationException;
import commons.exception.ServiceException;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;
import commons.gui.widget.dialog.OpenableTrayDialog;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

/**
 * Modela un di�logo b�sico d�nde se puede saber si el mismo corresponde a un di�logo de Alta, Edici�n u otro tipo ,
 * determinado por un tipo enumerado adecuado para tal fin.
 */
public abstract class BaseArcoIrisUIMultiPurposeDialog<T extends Validatable> extends
		BaseCompositeModelBoundedDialog<T> {

	protected Purpose purpose;

	public BaseArcoIrisUIMultiPurposeDialog(T model, EnumProperty title, Purpose purpose) {
		super(model, new FakeEnumProperty(title.toString() + " - " + purpose.toString()), purpose.isReadOnly());
		super.readOnly = this.isReadOnly(super.getModel(), purpose);
		this.purpose = purpose;
	}

	/**
	 * Adds the current model of this dialog to the current self healing configuration
	 */
	public abstract void addModelToCurrentSHConfiguration();

	/**
	 * Removes the current model from the underlying self healing configuration
	 */
	public abstract void removeModelFromCurrentSHConfiguration();

	/**
	 * Obtains the message to be used for displaying the "successful operation message" to the user
	 * 
	 * @param operation
	 *            the operation that is taking place
	 * @return the message to be used for displaying the "successful operation message" to the user
	 */
	public abstract String getSuccessfulOperationMessage(String operation);

	/**
	 * Indica el estado de readOnly. Por defecto, el mismo est� dado por el prop�sito, aunque dicho comportamiento puede
	 * ser sobreescrito ante la necesidad de evaluar m�s condiciones dependientes del modelo.
	 * 
	 * @param model
	 *            el modelo involucrado en la apertura del di�logo.
	 * @param purpose
	 *            el pro�sito con el que se abre el di�logo.
	 */
	protected boolean isReadOnly(T model, Purpose purpose) {
		return purpose.isReadOnly();
	}

	@Override
	protected boolean performOK() {
		this.getModel().validate();

		String operation = null;
		try {
			ArcoIrisUIManager arcoIrisUIManager = ArcoIrisUIManager.getInstance();
			if (this.purpose.isCreation()) {
				operation = "created";
				this.addModelToCurrentSHConfiguration();
			} else if (this.purpose.isEdition()) {
				operation = "updated";
			} else if (this.purpose.isDeletion()) {
				operation = "deleted";
				this.removeModelFromCurrentSHConfiguration();
			} else {
				throw new RuntimeException("The code does not contemplate the purpose " + this.purpose + " yet");
			}
			arcoIrisUIManager.saveSelfHealingConfiguration();

		} catch (ServiceException ex) {
			// Esta jerarqu�a de excepciones ya viene preparada para ser
			// mostradas
			throw ex;
		} catch (Exception ex) {
			throw new ApplicationException("Error when accesing the service", ex);
		}
		showSuccessfulOperationDialog(this.getSuccessfulOperationMessage(operation));

		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void cancelPressed() {
		if (doIHaveToAbandonChanges()) {
			Class<?> modelClass = this.getModel().getClass();
			if (Identifiable.class.isAssignableFrom(modelClass) && this.purpose.isCreation()) {
				// since we won't use it
				ArcoIrisUIManager.getInstance().returnRecentlyRequestedId((Class<Identifiable>) modelClass);
			}
			super.cancelPressed();
		}
	}

	@Override
	protected boolean isOkButtonAllowed() {
		boolean noAcceptButton = (this.isReadOnly(super.getModel(), this.purpose) && this.purpose.isEdition() || this.purpose
				.isView());
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
	 * {@link CommonLabels#CLOSE}.<br>
	 * Si no se cumplen las anteriores condiciones, el texto es el dado por la superclase.
	 * 
	 * @see {@link OpenableTrayDialog#getCancelButtonText()}
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