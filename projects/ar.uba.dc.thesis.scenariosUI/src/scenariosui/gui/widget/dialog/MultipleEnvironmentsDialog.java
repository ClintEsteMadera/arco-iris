package scenariosui.gui.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.model.bean.BeanModel;
import commons.gui.model.collection.DefaultListValueModel;
import commons.gui.table.CrudTableComposite;
import commons.gui.table.GenericEditHandler;
import commons.gui.table.TableMetainfo;
import commons.gui.util.PageHelper;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;

/**
 * A Dialog providing CRUD functionality for multiple environments.<br>
 * For now, this class is due to be deleted since it is not being used.
 * 
 * @deprecated
 */
@Deprecated
public class MultipleEnvironmentsDialog extends BaseCompositeModelBoundedDialog<List<Environment>> {

	private DefaultListValueModel<Environment> compositeModel;

	public MultipleEnvironmentsDialog(List<Environment> environments) {
		super(environments, ScenariosUILabels.ENVIRONMENTS, false);
		this.compositeModel = new DefaultListValueModel<Environment>(new BeanModel<List<Environment>>(environments));
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ScenariosUILabels.ENVIRONMENTS.toString());
		Rectangle centerLocation = PageHelper.getCenterLocation(550, 250);
		shell.setBounds(centerLocation);
	}

	@Override
	protected void okPressed() {
		// TODO Implement validation using this mechanism within all Thesis Pojos?
		// ValidationResult errors = new ValidationResult();
		// try {
		// this.getElement().validate(errors);
		// } catch (Exception e) {
		// throw new ValidationException(errors.getErrors());
		// }
		super.okPressed();
	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		final Group group = (new SimpleGroup(parent, CommonLabels.NO_LABEL, false, 1)).getSwtGroup();
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		TableMetainfo<Environment> tableMetaInfo = new TableMetainfo<Environment>(group,
				UniqueTableIdentifier.ENVIRONMENTS, Environment.class, new BindingInfo(compositeModel), super.readOnly);
		new CrudTableComposite(tableMetaInfo, new GenericEditHandler<Environment, EnvironmentDialog>(group.getShell(),
				EnvironmentDialog.class, Environment.class));

		// SimpleComposite c = new SimpleComposite(group, false, 2, 1);
		// Binding binding = new BindingInfo(environmentsCompositeModel, "nroConvocatoria");
		// ComboMetainfo comboMetainfo = ComboMetainfo.create(c, LabelsConvocatoria.NUMERO_CONVOCATORIA, binding,
		// false);
		// ComboFactory.createCombo(comboMetainfo);
		//
		// binding = new BindingInfo(environmentsCompositeModel, "cuartoIntermedio");
		// BooleanFieldMetainfo metaInfo = BooleanFieldMetainfo.create(group, LabelsConvocatoria.CUARTO_INTERMEDIO,
		// binding, false);
		// BooleanFactory.createBoolean(metaInfo);
		//
		// SimpleComposite composite = new SimpleComposite(group, false, 1, 2);
		//
		// new TipoAsambleaGroup(composite, environmentsCompositeModel.getValue().getTiposAsamblea(),
		// LabelsConvocatoria.TIPO_ASAMBLEA, false);
	}

	@Override
	protected List<Environment> newModel() {
		return new ArrayList<Environment>();
	}

	@Override
	protected boolean performOK() {
		// FIXME ver bien que hacer aca
		ScenariosUIController.getInstance().saveSelfHealingConfiguration();
		return true;
	}
}
