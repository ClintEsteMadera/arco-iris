package scenariosui.gui.widget.dialog;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.common.Identifiable;

import commons.exception.ApplicationException;
import commons.exception.ServiceException;
import commons.gui.background.BackgroundInvocationException;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

/**
 * Modela un diálogo básico dónde se puede saber si el mismo corresponde a un diálogo de Alta, Edición u otro tipo ,
 * determinado por un tipo enumerado adecuado para tal fin.
 */
public abstract class BaseScenariosUIMultiPurposeDialog<T> extends BaseCompositeModelBoundedDialog<T> {

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
	 * Indica el estado de readOnly. Por defecto, el mismo está dado por el propósito, aunque dicho comportamiento puede
	 * ser sobreescrito ante la necesidad de evaluar más condiciones dependientes del modelo.
	 * 
	 * @param model
	 *            el modelo involucrado en la apertura del diálogo.
	 * @param scenariosUIPurpose
	 *            el proósito con el que se abre el diálogo.
	 */
	protected boolean isReadOnly(T model, ScenariosUIPurpose scenariosUIPurpose) {
		return scenariosUIPurpose.isReadOnly();
	}

	@Override
	protected boolean performOK() {
		String operation = null;
		try {
			ScenariosUIManager scenariosUIManager = ScenariosUIManager
					.getInstance();
			switch (this.purpose) {
			case CREATION:
				operation = "created";
				this.addModelToCurrentSHConfiguration();
				break;
			case EDIT:
				operation = "updated";
				break;
			case DELETE:
				operation = "deleted";
				this.removeModelFromCurrentSHConfiguration();
				break;
			default:
				throw new RuntimeException("The code does not contemplate the purpose " + this.purpose + "yet");
			}
			scenariosUIManager.saveSelfHealingConfiguration();

		} catch (BackgroundInvocationException ex) {
			// Se supone que se atrapa más arriba esta excepción...
			throw ex;
		} catch (ServiceException ex) {
			// Esta jerarquía de excepciones ya viene preparada para ser
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
			if (Identifiable.class.isInstance(modelClass) && this.purpose.isCreation()) {
				ScenariosUIManager.getInstance().returnRecentlyRequestedId(
						(Class<Identifiable>) modelClass); // since we won't use it
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
	 * Provee el texto para el botón "Cancelar". Por defecto, este valor está dado por el propósito, siempre y cuando no
	 * sea nulo. Si dicho valor fuera nulo, y no existe el botón "Aceptar", el texto es el dado por
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