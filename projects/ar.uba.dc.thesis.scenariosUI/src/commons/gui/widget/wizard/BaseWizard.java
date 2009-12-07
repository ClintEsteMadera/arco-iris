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
* $Id: BaseWizard.java,v 1.1 2007/09/26 21:02:52 cvschioc Exp $
*/

package commons.gui.widget.wizard;

import org.eclipse.jface.wizard.Wizard;

import commons.gui.model.CompositeModel;
import commons.gui.model.bean.BeanModel;


/**
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2007/09/26 21:02:52 $
 */

public abstract class BaseWizard<T> extends Wizard {

	public BaseWizard(String title) {
		super();
		setWindowTitle(title);
		setNeedsProgressMonitor(true);
		this.compositeModel = new BeanModel<T>(newInstance());
	}
	
	protected CompositeModel<T> getCompositeModel() {
		return this.compositeModel;
	}
	
	protected T getModel() {
		return this.compositeModel.getValue();
	}
	
	/**
	 * Provee una nueva instancia del modelo a usar en el Wizard
	 */
	protected abstract T newInstance();
	
	private CompositeModel<T> compositeModel;
}
