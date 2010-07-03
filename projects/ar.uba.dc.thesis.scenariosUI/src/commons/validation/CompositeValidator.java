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
* $Id: CompositeValidator.java,v 1.1 2008/05/09 14:05:29 cvspasto Exp $
*/

package commons.validation;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/05/09 14:05:29 $
 */

public class CompositeValidator 
implements Validator {

	public CompositeValidator(Validator validator1, Validator validator2){
		this.validator1=validator1;
		this.validator2=validator2;
	}
	
	@SuppressWarnings("unchecked")
	public boolean validate(Object objectToValidate, ValidationResult result) {
		return validator1.validate(objectToValidate, result) &
			validator2.validate(objectToValidate, result);
	}

	private Validator validator1;
	private Validator validator2;
}
