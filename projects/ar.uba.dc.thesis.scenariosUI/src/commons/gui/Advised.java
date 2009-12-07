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
