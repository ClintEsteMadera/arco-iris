package commons.gui.widget.page;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.gui.model.CompositeModel;
import commons.gui.model.validation.ValidableComposite;
import commons.gui.model.validation.ValidationChangedListener;
import commons.gui.model.validation.ValidationSource;
import commons.gui.util.PageHelper;
import commons.gui.widget.DefaultLayoutFactory;
import commons.properties.EnumProperty;

public abstract class BasePreferencesPage<T> extends PreferencePage {

	public BasePreferencesPage(CompositeModel<T> compositeModel, EnumProperty title, String description,
			boolean readOnly) {
		super(title.toString());
		this.readOnly = readOnly;
		setDescription(description);
		noDefaultAndApplyButton();
		this.compositeModel = compositeModel;
		this.readOnly = readOnly;

		this.validationListener = new ValidationChangedListener() {
			public void validationChanged(ValidationSource source) {
				updateValidationErrors();
			}
		};
	}

	public BasePreferencesPage(CompositeModel<T> beanModel, EnumProperty title, boolean readOnly) {
		this(beanModel, title, null, readOnly);
	}

	public CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}

	public T getModel() {
		return this.compositeModel.getValue();
	}

	public void setValidationSource(ValidationSource validationSource) {
		this.validationSource = validationSource;
		if (this.validationSource != null) {
			this.validationSource.addValidationChangedListener(this.validationListener);
		}
	}

	protected void setCompositeModel(CompositeModel<T> beanModel) {
		this.compositeModel = beanModel;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);

		basicAddFields(composite);

		if (this.validationSource != null) {
			this.validableComposite = new ValidableComposite(parent);
			updateValidationErrors();
		}

		return composite;
	}

	protected void basicAddFields(Composite parent) {
		PageHelper.getMainWindow().currentPreferencePage = this;
		DefaultLayoutFactory.setDefaultGridLayout(parent, getNumColumns());
		this.addFields(parent);
	}

	protected int getNumColumns() {
		return 2;
	}

	protected abstract void addFields(Composite parent);

	protected boolean readOnly = false;

	private void updateValidationErrors() {
		if (this.validationSource.getValidationErrors() != null && this.validableComposite != null) {
			this.validableComposite.validate(this.validationSource.getValidationErrors());
		}
	}

	private CompositeModel<T> compositeModel;

	// validacion
	private ValidationSource validationSource;
	private ValidableComposite validableComposite;
	private ValidationChangedListener validationListener;
}