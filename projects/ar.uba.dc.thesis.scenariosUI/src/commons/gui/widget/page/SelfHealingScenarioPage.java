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
 * $Id: BaseEmisorPage.java,v 1.27 2008/05/16 20:18:10 cvschioc Exp $
 */

package commons.gui.widget.page;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sba.common.properties.EnumProperty;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.model.CompositeModel;
import commons.gui.util.proposito.ScenariosUIProposito;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.ScenariosUILabels;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.27 $ $Date: 2008/05/16 20:18:10 $
 */

public class SelfHealingScenarioPage extends BasePreferencesPage<SelfHealingScenario> {

	public SelfHealingScenarioPage(CompositeModel<SelfHealingScenario> model, EnumProperty title, boolean readOnly,
			ScenariosUIProposito proposito) {
		super(model, title, readOnly);
		this.proposito = proposito;
	}

	@Override
	protected void addFields(Composite parent) {
		datosBasicosGroup = new SimpleGroup(parent, ScenariosUILabels.BASIC_DATA, this.readOnly);
		Group swtGroup = datosBasicosGroup.getSwtGroup();

		CompositeModel<SelfHealingScenario> underlyingScenario = super.getCompositeModel();

		//TODO id ???

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.NAME, new BindingInfo(
				underlyingScenario, "name"), this.readOnly);
		TextFactory.createText(textMetainfo);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(swtGroup, ScenariosUILabels.CONCERN, new BindingInfo(underlyingScenario, "concern"), this.readOnly);
		ComboFactory.createCombo(comboMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.STIMULUS_SOURCE, new BindingInfo(underlyingScenario, "stimulusSource"), this.readOnly);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.STIMULUS, new BindingInfo(underlyingScenario, "stimulus"), this.readOnly);
		TextFactory.createText(textMetainfo);

		// TODO: Environments

		// TODO: Artifact

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.ARTIFACT, new BindingInfo(underlyingScenario, "artifact"), this.readOnly);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.RESPONSE, new BindingInfo(underlyingScenario, "response"), this.readOnly);
		TextFactory.createText(textMetainfo);

		// TODO: Response Measure

		// TODO: Architectural Decisions

		BooleanFieldMetainfo metainfo = BooleanFieldMetainfo.create(parent, ScenariosUILabels.ENABLED, new BindingInfo(underlyingScenario, "enabled"), readOnly);
		BooleanFactory.createBoolean(metainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.PRIORITY, new BindingInfo(underlyingScenario, "priority"), this.readOnly);
		TextFactory.createText(textMetainfo);

		// TODO: Repair Strategies
	}

	private final ScenariosUIProposito proposito;

	protected SimpleGroup datosBasicosGroup;
}