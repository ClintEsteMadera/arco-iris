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