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
* $Id: ValidationSource.java,v 1.1 2008/05/15 20:53:30 cvspasto Exp $
*/

package commons.gui.model.validation;

import commons.validation.ValidationError;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/05/15 20:53:30 $
 */

public interface ValidationSource {

	public ValidationError[] getValidationErrors();
	
	public void setValidationErrors(ValidationError[] errors);
	
	public void addValidationChangedListener(ValidationChangedListener listener);
	
	public void removeValidationChangedListener(ValidationChangedListener listener);	
}
