package ar.uba.dc.arcoirisui.gui.widget.composite;

import java.util.SortedMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import ar.uba.dc.arcoirisui.gui.widget.dialog.ConstraintDialog;
import ar.uba.dc.arcoirisui.gui.widget.group.ConcernWeightsGroup;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.qa.Concern;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import commons.gui.model.CompositeModel;
import commons.gui.model.bean.BeanModel;
import commons.gui.table.CrudTableComposite;
import commons.gui.table.TableMetainfo;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;

public class EnvironmentComposite extends SimpleComposite {

	private static final int VISIBLE_ROWS = 3;

	public EnvironmentComposite(Composite parent, Purpose purpose, CompositeModel<Environment> underlyingEnvironment) {
		super(parent, purpose.isReadOnly());

		if (purpose.isCreation()) {
			underlyingEnvironment.getValue().setId(ArcoIrisUIManager.getInstance().getNextId(Environment.class));
		}

		Group environmentGroup = new SimpleGroup(this, ArcoIrisUILabels.ENVIRONMENT, this.readOnly, 2, 1).getSwtGroup();
		DefaultLayoutFactory.setGrabAllExcessesAndFillBothGridData(environmentGroup, true);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(environmentGroup, ArcoIrisUILabels.ID,
				new BindingInfo(underlyingEnvironment, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(environmentGroup, ArcoIrisUILabels.NAME, new BindingInfo(
				underlyingEnvironment, "name"), this.readOnly);
		textMetainfo.visibleSize = 15;
		TextFactory.createText(textMetainfo);

		CompositeModel<SortedMap<Concern, Double>> weightsMap = new BeanModel<SortedMap<Concern, Double>>(
				underlyingEnvironment.getValue().getWeights());

		new ConcernWeightsGroup(weightsMap, this, ArcoIrisUILabels.CONCERNS_WEIGHTS, this.readOnly, 1);

		SimpleGroup conditionsGroup = new SimpleGroup(this, ArcoIrisUILabels.CONDITIONS, this.readOnly, 1, 2);
		conditionsGroup.getLayout().verticalSpacing = conditionsGroup.getLayout().verticalSpacing * 4;

		// FIXME This is a hack!!!
		Class<NumericBinaryRelationalConstraint> itemClass = NumericBinaryRelationalConstraint.class;

		TableMetainfo<NumericBinaryRelationalConstraint> tableMetaInfo = new TableMetainfo<NumericBinaryRelationalConstraint>(
				conditionsGroup.getSwtGroup(), UniqueTableIdentifier.ENVIRONMENT_CONSTRAINTS, itemClass,
				new BindingInfo(underlyingEnvironment, "conditions"), this.readOnly);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		CrudTableComposite crudTableComposite = new CrudTableComposite(tableMetaInfo, ConstraintDialog.class);
		crudTableComposite.getTable().setVisibleRows(VISIBLE_ROWS);

	}
}
