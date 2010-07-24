package scenariosui.gui.widget.dialog;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.ScenariosUIWindow;
import scenariosui.gui.widget.page.SelfHealingScenarioPage;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.exception.ApplicationException;
import commons.exception.ServiceException;
import commons.gui.background.BackgroundInvocationException;
import commons.properties.EnumProperty;
import commons.properties.Messages;

public class SelfHealingScenarioDialog extends BaseScenariosUIMultiPurposeDialog<SelfHealingScenario> {

	public SelfHealingScenarioDialog(SelfHealingScenario model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected SelfHealingScenario newModel() {
		return new SelfHealingScenario();
	}

	@Override
	protected void doCreateNodes() {
		addNode(null, "selfHealingScenarioPage", new SelfHealingScenarioPage(super.getCompositeModel(),
				ScenariosUILabels.SCENARIO, super.readOnly, super.purpose));
	}

	@Override
	protected boolean performOK() {
		// TODO Ver de poner este metodo en la superclase ya que casi todos son iguales...
		boolean okStatus = false;
		String operacion = null;
		try {
			ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();
			switch (super.purpose) {
			case CREATION:
				operacion = "created";
				scenariosUIController.getCurrentSelfHealingConfiguration().addScenario(this.getModel());
				scenariosUIController.saveSelfHealingConfiguration();
				break;
			case EDIT:
				operacion = "updated";
				scenariosUIController.saveSelfHealingConfiguration();
				break;
			default:
				throw new RuntimeException("Se utilizó un Propósito no contemplado!!");
			}
		} catch (BackgroundInvocationException ex) {
			// Se supone que se atrapa más arriba esta excepción...
			throw ex;
		} catch (ServiceException ex) {
			// Esta jerarquía de excepciones ya viene preparada para ser
			// mostradas
			throw ex;
		} catch (Exception ex) {
			throw new ApplicationException("Error al acceder al servicio de Escenarios", ex);
		}
		okStatus = true;
		String denominacion = super.getModel().getName() != null ? super.getModel().getName() : "";
		String mensaje = Messages.SUCCESSFUL_SCENARIO.toString(denominacion, operacion);
		showSuccessfulOperationDialog(mensaje);
		ScenariosUIWindow.getInstance().resetQuery(TableConstants.SCENARIOS, super.getModel().getId());

		return okStatus;
	}
}