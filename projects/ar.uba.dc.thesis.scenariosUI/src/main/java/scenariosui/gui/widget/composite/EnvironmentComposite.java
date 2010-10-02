package scenariosui.gui.widget.composite;

import java.util.SortedMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.gui.widget.dialog.ConstraintDialog;
import scenariosui.gui.widget.group.ConcernWeightsGroup;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import commons.gui.model.CompositeModel;
import commons.gui.model.bean.BeanModel;
import commons.gui.table.CrudTableComposite;
import commons.gui.table.TableMetainfo;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.LabelFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;

public class EnvironmentComposite extends SimpleComposite {

	private static final int VISIBLE_ROWS = 3;

	public EnvironmentComposite(Composite parent, Purpose purpose, CompositeModel<Environment> underlyingEnvironment) {
		super(parent, purpose.isReadOnly());

		if (purpose.isCreation()) {
			underlyingEnvironment.getValue().setId(ScenariosUIManager.getInstance().getNextId(Environment.class));
		}

		Group swtGroup = new SimpleGroup(parent, ScenariosUILabels.ENVIRONMENT, this.readOnly).getSwtGroup();

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.ID, new BindingInfo(
				underlyingEnvironment, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.NAME, new BindingInfo(
				underlyingEnvironment, "name"), this.readOnly);
		textMetainfo.visibleSize = 15;
		TextFactory.createText(textMetainfo);

		LabelFactory.createLabel(swtGroup, ScenariosUILabels.CONDITIONS, false, true);

		// FIXME This is a hack!!!
		Class<NumericBinaryRelationalConstraint> itemClass = NumericBinaryRelationalConstraint.class;

		TableMetainfo<NumericBinaryRelationalConstraint> tableMetaInfo = new TableMetainfo<NumericBinaryRelationalConstraint>(
				swtGroup, UniqueTableIdentifier.ENVIRONMENT_CONSTRAINTS, itemClass, new BindingInfo(
						underlyingEnvironment, "conditions"), this.readOnly);

		CrudTableComposite crudTableComposite = new CrudTableComposite(tableMetaInfo, ConstraintDialog.class);
		crudTableComposite.getTable().setVisibleRows(VISIBLE_ROWS);

		CompositeModel<SortedMap<Concern, Double>> weightsMap = new BeanModel<SortedMap<Concern, Double>>(
				underlyingEnvironment.getValue().getWeights());

		new ConcernWeightsGroup(weightsMap, swtGroup, ScenariosUILabels.CONCERNS_WEIGHTS, this.readOnly, 2);

		/*
		 * private Heuristic heuristic;
		 */
	}
}
