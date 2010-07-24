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
 * $Id: ActualizarJornadaDialog.java,v 1.2 2008/05/14 19:56:20 cvschioc Exp $
 */

package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenariosui.gui.widget.composite.EnvironmentComposite;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.model.bean.BeanModel;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.OpenableTrayDialog;

public class EnvironmentOpenableTrayDialog extends OpenableTrayDialog<Environment> {

	private BeanModel<Environment> environment;

	private final Purpose purpose;

	public EnvironmentOpenableTrayDialog(Purpose purpose) {
		super(PageHelper.getMainShell());
		this.purpose = purpose;
	}

	@Override
	protected void createBeanModel(Environment environment) {
		this.environment = new BeanModel<Environment>(environment);
	}

	@Override
	public Environment getElement() {
		return this.environment.getValue();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		return new EnvironmentComposite(parent, this.purpose, this.environment);
	}
}