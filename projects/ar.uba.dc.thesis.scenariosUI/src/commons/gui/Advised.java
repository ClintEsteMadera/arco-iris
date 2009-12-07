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
* $Id: Advised.java,v 1.3 2008/01/08 20:00:07 cvschioc Exp $
*/

package commons.gui;

import commons.gui.widget.composite.QueryComposite;


/**
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.3 $ $Date: 2008/01/08 20:00:07 $
 */

public interface Advised<T extends QueryComposite> {

	void rowSelected(T queryComposite);
	
	void rowDoubleClicked(T queryComposite);
}
