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
 * $Id: ActualizarJornadaDialog.java,v 1.2 2008/05/14 19:56:20 cvschioc Exp $
 */

package scenariosui.gui.widget.dialog;

import org.eclipse.jface.dialogs.MessageDialog;

import sba.common.exception.ApplicationException;
import sba.common.exception.ServiceException;
import sba.common.properties.EnumProperty;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.ScenariosUIWindow;
import scenariosui.gui.widget.page.SelfHealingScenarioPage;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.background.BackgroundInvocationException;
import commons.gui.model.ComplexValueChangeEvent;
import commons.gui.model.ComplexValueChangeListener;
import commons.properties.CommonLabels;
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
	protected void createNodes() {
		addNode(null, "selfHealingScenarioPage", new SelfHealingScenarioPage(super.getCompositeModel(),
				ScenariosUILabels.SCENARIO, super.readOnly, super.purpose));

		// chequeo de modificacion sobre el modelo
		this.getCompositeModel().addComplexValueChangeListener(new ComplexValueChangeListener() {

			@SuppressWarnings("unchecked")
			public void complexValueChange(ComplexValueChangeEvent ev) {
				modelDirty = true;
			}
		});
	}

	@Override
	protected void cancelPressed() {
		boolean abandonChanges = true;

		if (this.modelDirty) {
			abandonChanges = MessageDialog.openQuestion(this.getShell(), CommonLabels.ATENTION.toString(),
					"Do you wish to abandon the changes made to the scenario ?");
		}
		if (abandonChanges) {
			if (this.purpose.isCreation()) {
				ScenariosUIController.getInstance().returnRecentlyRequestedId(); // since we won't use it
			}
			super.cancelPressed();
		}
	}

	@Override
	protected boolean performOK() {
		boolean okStatus = false;
		String operacion = null;
		try {
			ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();
			switch (super.purpose) {
			case CREATION:
				operacion = "creado";
				scenariosUIController.getCurrentSelfHealingConfiguration().addScenario(this.getModel());
				scenariosUIController.saveSelfHealingConfiguration();
				break;
			case EDIT:
				operacion = "actualizado";
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
		mostrarDialogoOperacionExitosa(mensaje);
		ScenariosUIWindow.getInstance().resetQuery(TableConstants.SCENARIOS, super.getModel().getId());

		return okStatus;
	}

	private boolean modelDirty;
}