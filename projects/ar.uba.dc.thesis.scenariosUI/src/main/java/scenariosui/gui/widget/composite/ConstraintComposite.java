package scenariosui.gui.widget.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.util.ConstraintType;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

import commons.gui.model.CompositeModel;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.bean.BeanModel;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.composite.InvisibleTabFolder;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.composite.Tab;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.LabelFactory;
import commons.gui.widget.factory.TextFactory;
import commons.properties.CommonLabels;

public class ConstraintComposite extends SimpleComposite {

	private InvisibleTabFolder tabFolder;

	private CompositeModel<Constraint> activeModel;

	private CompositeModel<Constraint> numericBinaryRelationalModel;

	private CompositeModel<ConstraintType> constraintType;

	private final CompositeModel<Constraint> initialModel;

	public ConstraintComposite(Composite composite, CompositeModel<Constraint> model, boolean addEmptyOption,
			boolean readOnly) {
		super(composite, readOnly, 1);
		this.initialModel = model;
		this.initTabModels(model);

		this.createConstraintTypeSelection(this, addEmptyOption);

		tabFolder = new InvisibleTabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.createEmptyTab();
		this.createTabForNumericBinaryRelationalConstraint();

		this.setCurrentActiveTab();
	}

	private void initTabModels(CompositeModel<Constraint> model) {
		Constraint underlyingConstraint = model.getValue();
		if (underlyingConstraint == null) {
			this.constraintType = new BeanModel<ConstraintType>(ConstraintType.class);
			this.activeModel = new BeanModel<Constraint>(Constraint.class);
			this.numericBinaryRelationalModel = new BeanModel<Constraint>(
					ConstraintType.NUMERIC_BINARY_RELATIONAL_CONSTRAINT.getNewConstraintInstance());
		} else {
			ConstraintType currentConstraintType = ConstraintType.getConstraintType(underlyingConstraint.getClass());

			switch (currentConstraintType) {
			case NUMERIC_BINARY_RELATIONAL_CONSTRAINT:
				this.numericBinaryRelationalModel = model;
				this.activeModel = this.numericBinaryRelationalModel;
				break;
			default:
				throw new RuntimeException("Cannot initialize the contraint "
						+ underlyingConstraint.getClass().getName());
			}
			this.constraintType = new BeanModel<ConstraintType>(currentConstraintType);
		}
	}

	private void createConstraintTypeSelection(Composite parent, boolean addEmptyOption) {
		SimpleComposite composite = new SimpleComposite(parent, super.readOnly, 2, 1);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(composite, ScenariosUILabels.CONSTRAINT_TYPE,
				new BindingInfo(this.constraintType), readOnly);
		comboMetainfo.items = ConstraintType.values();
		comboMetainfo.addEmptyOption = addEmptyOption;
		ComboFactory.createCombo(comboMetainfo);

		this.constraintType.addValueChangeListener(new ValueChangeListener<ConstraintType>() {
			@Override
			public void valueChange(ValueChangeEvent<ConstraintType> ev) {
				ConstraintType newValue = ev.getNewValue();
				if (newValue == null) {
					activeModel = new BeanModel<Constraint>(Constraint.class);
				} else {
					Constraint currentModel = activeModel.getValue();
					Class<? extends Constraint> selectedConstraintClass = newValue.getMappedClass();

					if (currentModel == null || !selectedConstraintClass.equals(currentModel.getClass())) {
						if (ConstraintType.NUMERIC_BINARY_RELATIONAL_CONSTRAINT.equals(newValue)) {
							activeModel = numericBinaryRelationalModel;
						}
					}
				}
				setCurrentActiveTab();
			}
		});
	}

	/**
	 * Just creates an empty tab composite to display when there is no selection
	 */
	private void createEmptyTab() {
		new Tab(this.tabFolder, super.readOnly);
	}

	private void createTabForNumericBinaryRelationalConstraint() {
		Tab tab = new Tab(this.tabFolder, super.readOnly);

		DefaultLayoutFactory.setDefaultGridLayout(tab, 8);

		LabelFactory.createLabel(tab);

		LabelFactory.createLabel(tab, ScenariosUILabels.SYSTEM_NAME, false, false);

		LabelFactory.createLabel(tab);

		LabelFactory.createLabel(tab, ScenariosUILabels.ARTIFACT, false, false);

		LabelFactory.createLabel(tab);

		LabelFactory.createLabel(tab, ScenariosUILabels.PROPERTY, false, false);

		LabelFactory.createLabel(tab);

		LabelFactory.createLabel(tab);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(tab, CommonLabels.NO_LABEL, new BindingInfo(
				this.numericBinaryRelationalModel, "quantifier"), super.readOnly);
		comboMetainfo.addEmptyOption = false;
		ComboFactory.createCombo(comboMetainfo);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(tab, CommonLabels.NO_LABEL, new BindingInfo(
				this.numericBinaryRelationalModel, "artifact.systemName"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		LabelFactory.createLabel(tab, CommonLabels.DOT, true, false);

		textMetainfo = TextFieldMetainfo.create(tab, CommonLabels.NO_LABEL, new BindingInfo(
				this.numericBinaryRelationalModel, "artifact.name"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		LabelFactory.createLabel(tab, CommonLabels.DOT, true, false);

		textMetainfo = TextFieldMetainfo.create(tab, CommonLabels.NO_LABEL, new BindingInfo(
				this.numericBinaryRelationalModel, "property"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		comboMetainfo = ComboMetainfo.create(tab, CommonLabels.NO_LABEL, new BindingInfo(
				this.numericBinaryRelationalModel, "binaryOperator"), super.readOnly);
		ComboFactory.createCombo(comboMetainfo);

		textMetainfo = TextFieldMetainfo.create(tab, CommonLabels.NO_LABEL, new BindingInfo(
				this.numericBinaryRelationalModel, "constantToCompareThePropertyWith"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);
	}

	private void setCurrentActiveTab() {
		tabFolder.setSelection(this.getSelectedTabIndex());
	}

	private int getSelectedTabIndex() {
		int result = 0;
		ConstraintType selectedContraintType = this.constraintType.getValue();

		if (ConstraintType.NUMERIC_BINARY_RELATIONAL_CONSTRAINT.equals(selectedContraintType)) {
			result = 1;
		}
		return result;
	}

	public void okPressed() {
		this.initialModel.setValue(this.activeModel.getValue());
	}
}
