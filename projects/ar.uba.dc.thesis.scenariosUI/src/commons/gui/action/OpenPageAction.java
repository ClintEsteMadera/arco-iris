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
 * $Id: OpenPageAction.java,v 1.3 2007/08/16 19:17:48 cvsalons Exp $
 */
package commons.gui.action;

import org.eclipse.jface.action.Action;

/**
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.3 $ $Date: 2007/08/16 19:17:48 $
 */
public abstract class OpenPageAction extends Action {

	
	public OpenPageAction(){
		super();
	}

	public OpenPageAction(String text){
		super(text);
	}

    @Override
	public void run() {
    	openPage();
    }
    
	protected abstract void openPage();

}

