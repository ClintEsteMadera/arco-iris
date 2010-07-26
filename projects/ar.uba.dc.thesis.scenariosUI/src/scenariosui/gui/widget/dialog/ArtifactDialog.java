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
 * $Id: ActualizarJornadaDialog.java,v 1.2 2008/05/14 19:56:20 cvschioc Exp $
 */

package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.composite.ArtifactComposite;
import scenariosui.service.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;
import commons.properties.Messages;

public class ArtifactDialog extends BaseScenariosUIMultiPurposeDialog<Artifact> {

	public ArtifactDialog(ScenariosUIPurpose purpose) {
		super(null, new FakeEnumProperty(""), purpose);
	}

	public ArtifactDialog(Artifact model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected Artifact newModel() {
		return new Artifact();
	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		new ArtifactComposite(parent, this.purpose, this.getCompositeModel());
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		SelfHealingConfigurationManager.getInstance().getCurrentSelfHealingConfiguration().addArtifact(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		SelfHealingConfigurationManager.getInstance().getCurrentSelfHealingConfiguration().removeArtifact(
				this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String artifactName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return Messages.SUCCESSFUL_ARTIFACT.toString(artifactName, operation);
	}
}