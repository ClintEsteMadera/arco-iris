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