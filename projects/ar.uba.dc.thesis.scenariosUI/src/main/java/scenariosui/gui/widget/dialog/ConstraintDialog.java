package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.widget.composite.ConstraintComposite;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.ScenariosUIMessages;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;

public class ConstraintDialog<C extends Constraint> extends BaseScenariosUIMultiPurposeDialog<C> {

	private ConstraintComposite constraintComposite;

	public ConstraintDialog(Purpose purpose) {
		super(null, ScenariosUILabels.CONSTRAINT, purpose);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected C newModel() {
		// FIXME this is a hack!!
		return (C) new NumericBinaryRelationalConstraint();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addWidgetsToDialogArea(Composite parent) {
		this.constraintComposite = new ConstraintComposite(parent,
				(CompositeModel<Constraint>) this.getCompositeModel(), false, super.readOnly);
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		this.constraintComposite.okPressed();
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		// ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().removeArtifact(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String displayableConstraint = super.getModel().getToString() != null ? super.getModel().getToString() : "";
		return ScenariosUIMessages.SUCCESSFUL_CONSTRAINT.toString(displayableConstraint, operation);
	}
}