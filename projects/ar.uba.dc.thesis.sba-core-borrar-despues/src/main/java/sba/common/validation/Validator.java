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
* $Id: Validator.java,v 1.2 2008/03/18 20:29:24 cvspasto Exp $
*/

package sba.common.validation;


/**
 * Interfaz para los validadores "simples"
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.2 $ $Date: 2008/03/18 20:29:24 $
 */
public interface Validator<T> {

	public boolean validate(T objectToValidate,ValidationResult result);
}
