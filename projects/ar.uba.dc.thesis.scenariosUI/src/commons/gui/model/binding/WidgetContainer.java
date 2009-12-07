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
* $Id: WidgetContainer.java,v 1.1 2007/10/10 20:21:51 cvspasto Exp $
*/

package commons.gui.model.binding;

import org.eclipse.swt.widgets.Widget;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2007/10/10 20:21:51 $
 */

public interface WidgetContainer {
	Widget getWidget();
}
