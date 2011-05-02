package ar.uba.dc.arcoirisui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.gui.widget.composite.ConstraintComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUIMessages;
import ar.uba.dc.arcoiris.rainbow.constraint.Constraint;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;

public class ConstraintDialog<C extends Constraint> extends BaseArcoIrisUIMultiPurposeDialog<C> {

	private ConstraintComposite constraintComposite;

	/**
	 * FIXME This is a hack!!!
	 * 
	 * @param model
	 *            an instance of {@link NumericBinaryRelationalConstraint}
	 * @param purpose
	 *            the purpose this dialog is being opened for.
	 */
	@SuppressWarnings("unchecked")
	public ConstraintDialog(NumericBinaryRelationalConstraint model, Purpose purpose) {
		this((C) model, purpose);
	}

	public ConstraintDialog(C model, Purpose purpose) {
		super(model, ArcoIrisUILabels.CONSTRAINT, purpose);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected C newModel() {
		// FIXME This is a hack!!!
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
		// ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().removeArtifact(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String displayableConstraint = super.getModel().getToString() != null ? super.getModel().getToString() : "";
		return ArcoIrisUIMessages.SUCCESSFUL_CONSTRAINT.toString(displayableConstraint, operation);
	}
}