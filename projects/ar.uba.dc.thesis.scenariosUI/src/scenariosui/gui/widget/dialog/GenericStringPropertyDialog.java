package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;

import commons.properties.EnumProperty;

public class GenericStringPropertyDialog extends BaseScenariosUIMultiPurposeDialog<String> {

	public GenericStringPropertyDialog(String model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, title, purpose);
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String newModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
