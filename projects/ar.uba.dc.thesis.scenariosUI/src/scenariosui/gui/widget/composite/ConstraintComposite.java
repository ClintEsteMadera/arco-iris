package scenariosui.gui.widget.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.LabelFactory;
import commons.gui.widget.factory.TextFactory;
import commons.properties.CommonLabels;

public class ConstraintComposite extends SimpleComposite {

	private static final int VERTICAL_SPACING = 10;

	private InvisibleTabFolder tabFolder;

	private final CompositeModel<Constraint> model;

	private CompositeModel<ConstraintType> constraintType;

	public ConstraintComposite(Composite composite, CompositeModel<Constraint> model, boolean readOnly) {
		super(composite, readOnly, 1);
		this.model = model;
		this.initConstraintType();

		this.createConstraintTypeSelection(this);

		tabFolder = new InvisibleTabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.createEmptyTab();
		this.createTabForNumericBinaryRelationalConstraint();

		this.setCurrentActiveTab();
	}

	private void initConstraintType() {
		Constraint underlyingConstraint = this.model.getValue();
		if (underlyingConstraint == null) {
			this.constraintType = new BeanModel<ConstraintType>(ConstraintType.class);
		} else {
			ConstraintType constraintType = ConstraintType.getConstraintType(underlyingConstraint.getClass());
			this.constraintType = new BeanModel<ConstraintType>(constraintType);
		}
	}

	private void createConstraintTypeSelection(Composite parent) {
		SimpleComposite composite = new SimpleComposite(parent, super.readOnly, 2, 1);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(composite, ScenariosUILabels.CONSTRAINT_TYPE,
				new BindingInfo(this.constraintType), readOnly);
		comboMetainfo.items = ConstraintType.values();
		ComboFactory.createCombo(comboMetainfo);

		this.constraintType.addValueChangeListener(new ValueChangeListener<ConstraintType>() {
			// TODO No setear este valor ahora sino guardarlo y ofrecerlo con un getter al que pueda controlar el
			// okPressed del usuario (para no pegar un valor en el modelo si el usuario cancela)
			@Override
			public void valueChange(ValueChangeEvent<ConstraintType> ev) {
				ConstraintType newValue = ev.getNewValue();
				if (newValue == null) {
					model.getValue().restoreToDefaultValues();
				} else {
					Constraint currentModel = model.getValue();
					Class<? extends Constraint> selectedConstraintClass = newValue.getMappedClass();

					if (currentModel == null || !selectedConstraintClass.equals(currentModel.getClass())) {
						model.setValue(newValue.getNewConstraintInstance());
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
		this.createTabComposite();
	}

	private void createTabForNumericBinaryRelationalConstraint() {
		Composite parent = this.createTabComposite();

		DefaultLayoutFactory.setDefaultGridLayout(parent, 9);

		LabelFactory.createLabel(parent, ScenariosUILabels.SYSTEM_NAME, false, false);

		LabelFactory.createLabel(parent);

		LabelFactory.createLabel(parent, ScenariosUILabels.ARTIFACT, false, false);

		LabelFactory.createLabel(parent);

		LabelFactory.createLabel(parent, ScenariosUILabels.PROPERTY, false, false);

		LabelFactory.createLabel(parent);

		LabelFactory.createLabel(parent);

		LabelFactory.createLabel(parent);

		LabelFactory.createLabel(parent);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(parent, CommonLabels.NO_LABEL, new BindingInfo(
				this.model, "artifact.systemName"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		LabelFactory.createLabel(parent, CommonLabels.DOT, true, false);

		textMetainfo = TextFieldMetainfo.create(parent, CommonLabels.NO_LABEL, new BindingInfo(this.model,
				"artifact.name"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		LabelFactory.createLabel(parent, CommonLabels.DOT, true, false);

		textMetainfo = TextFieldMetainfo.create(parent, CommonLabels.NO_LABEL, new BindingInfo(this.model, "property"),
				super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(parent, CommonLabels.NO_LABEL, new BindingInfo(this.model,
				"binaryOperator"), super.readOnly);
		ComboFactory.createCombo(comboMetainfo);
		textMetainfo = TextFieldMetainfo.create(parent, CommonLabels.NO_LABEL, new BindingInfo(this.model,
				"constantToCompareThePropertyWith"), super.readOnly);
		textMetainfo.visibleSize = 10;
		TextFactory.createText(textMetainfo);

		BooleanFieldMetainfo booleanMetainfo = BooleanFieldMetainfo.create(parent, ScenariosUILabels.SUM,
				new BindingInfo(this.model, "sum"), super.readOnly);
		BooleanFactory.createBoolean(booleanMetainfo);
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

	private Composite createTabComposite() {
		Composite tabComposite = new SimpleComposite(this.tabFolder, this.readOnly, 1);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = VERTICAL_SPACING;
		gridLayout.marginWidth = 15;
		tabComposite.setLayout(gridLayout);
		return tabComposite;
	}
}
