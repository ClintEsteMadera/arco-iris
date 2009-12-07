/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: BaseWizardPage.java,v 1.2 2007/09/26 21:02:52 cvschioc Exp $
 */

package commons.gui.widget.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import commons.gui.model.CompositeModel;
import commons.gui.widget.composite.SimpleComposite;

/**
 * P�gina b�sica para poner en un Wizard.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2007/09/26 21:02:52 $
 */

public abstract class BaseWizardPage<T> extends WizardPage {

	protected BaseWizardPage(String title, CompositeModel<T> beanModel) {
		super(title, title, null);
		this.compositeModel = beanModel;
	}

	public void createControl(Composite parent) {
		SimpleComposite composite = new SimpleComposite(parent, false, 1);
		addFields(composite);
		super.setControl(composite);
	}

	public T getModel() {
		return this.compositeModel.getValue();
	}

	public CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}

	protected abstract void addFields(Composite composite);

	private CompositeModel<T> compositeModel;
}
