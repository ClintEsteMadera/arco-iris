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

package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.MessageDialog;

import sba.common.exception.ApplicationException;
import sba.common.exception.ServiceException;
import sba.common.properties.EnumProperty;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.background.BackgroundInvocationException;
import commons.gui.model.ComplexValueChangeEvent;
import commons.gui.model.ComplexValueChangeListener;
import commons.gui.util.proposito.ScenariosUIProposito;
import commons.gui.widget.ScenariosUIWindow;
import commons.gui.widget.page.SelfHealingScenarioPage;
import commons.properties.CommonLabels;
import commons.properties.Messages;
import commons.properties.ScenariosUILabels;
import commons.properties.TableConstants;
import commons.service.ScenarioService;

public class ScenarioDialog extends BaseScenariosUIMultiPurposeDialog<SelfHealingScenario> {

	public ScenarioDialog(SelfHealingScenario model, EnumProperty title, ScenariosUIProposito proposito) {
		super(model, title, proposito);
	}

	@Override
	protected SelfHealingScenario newModel() {
		return new SelfHealingScenario();
	}

	@Override
	protected void crearNodos() {
		addNode(null, "emisorEmpresaExtranjeraPage", new SelfHealingScenarioPage(super.getCompositeModel(),
				ScenariosUILabels.SCENARIO, super.readOnly, super.proposito));

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
		boolean deseaAbandonarCambios = true;

		if (this.modelDirty) {
			deseaAbandonarCambios = MessageDialog.openQuestion(this.getShell(), CommonLabels.ATENCION.toString(),
					"¿ Desea abandonar las modificaciones sobre el emisor ?");
		}
		if (deseaAbandonarCambios) {
			super.cancelPressed();
		}
	}

	@Override
	protected boolean performOK() {
		boolean okStatus = false;
		String operacion = null;
		try {
			switch (super.proposito) {
			case ALTA:
				operacion = "creado";
				this.getCompositeModel().setValue(ScenarioService.persistScenario(super.getModel()));
				break;
			case EDICION:
				operacion = "actualizado";
				ScenarioService.updateScenario(super.getModel());
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
		ScenariosUIWindow.getInstance().resetConsulta(TableConstants.SCENARIOS_QUERY, super.getModel().getId());

		return okStatus;
	}

	private boolean modelDirty;
}