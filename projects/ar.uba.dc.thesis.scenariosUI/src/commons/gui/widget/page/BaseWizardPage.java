/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
 * Página básica para poner en un Wizard.
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
