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

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.ScenariosUIWindow;
import scenariosui.gui.widget.page.EnvironmentPage;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.exception.ApplicationException;
import commons.exception.ServiceException;
import commons.gui.background.BackgroundInvocationException;
import commons.properties.EnumProperty;
import commons.properties.Messages;

public class EnvironmentDialog extends BaseScenariosUIMultiPurposeDialog<Environment> {

	public EnvironmentDialog(Environment model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected Environment newModel() {
		return new Environment();
	}

	@Override
	protected void doCreateNodes() {
		addNode(null, "environmentPage", new EnvironmentPage(super.getCompositeModel(), ScenariosUILabels.ENVIRONMENT,
				super.readOnly, super.purpose));
	}

	@Override
	protected boolean performOK() {
		boolean okStatus = false;
		String operacion = null;
		try {
			ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();
			switch (super.purpose) {
			case CREATION:
				operacion = "created";
				scenariosUIController.getCurrentSelfHealingConfiguration().addEnvironment(this.getModel());
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
			throw new ApplicationException("Error al acceder al servicio de Environments", ex);
		}
		okStatus = true;
		String denominacion = super.getModel().getName() != null ? super.getModel().getName() : "";
		String mensaje = Messages.SUCCESSFUL_ENVIRONMENT.toString(denominacion, operacion);
		mostrarDialogoOperacionExitosa(mensaje);
		ScenariosUIWindow.getInstance().resetQuery(TableConstants.ENVIRONMENTS, super.getModel().getId());

		return okStatus;
	}
}