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
