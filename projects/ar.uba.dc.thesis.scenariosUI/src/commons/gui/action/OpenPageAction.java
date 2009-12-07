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

