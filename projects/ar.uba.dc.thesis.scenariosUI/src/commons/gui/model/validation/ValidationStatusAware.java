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
* $Id: ValidationStatusAware.java,v 1.1 2008/05/14 21:13:22 cvspasto Exp $
*/

package commons.gui.model.validation;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/05/14 21:13:22 $
 */

public interface ValidationStatusAware {
	
	public void setOkStatus();
	
	public void setErrorStatus(String message);
	
	public boolean isOkStatus();
}
