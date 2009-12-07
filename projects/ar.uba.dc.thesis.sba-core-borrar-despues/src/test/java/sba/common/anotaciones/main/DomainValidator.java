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
* $Id: DomainValidator.java,v 1.3 2008/05/14 19:48:18 cvschioc Exp $
*/

package sba.common.anotaciones.main;

import sba.common.anotaciones.domain.Domain;
import sba.common.validation.GenericValidator;
import sba.common.validation.ValidationContext;
import sba.common.validation.ValidationResult;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/05/14 19:48:18 $
 */

public class DomainValidator 
extends GenericValidator{

	public DomainValidator(ValidationContext context) {
		super(Domain.class, context);
	}

	@Override
	public boolean validate(Object o, ValidationResult result) {
		return super.validate(o, result) &
			validateDomain((Domain)o,result);
	}

	private boolean validateDomain(Domain d, ValidationResult result) {
		
		if(d.getDescripcion() != null){
			this.validateRequired(d, "empleado.documento", result);
		}
		
		if(d.getDescripcion() != null && d.getDescripcion().length() < 10){
			result.addError(Constants.DESCRIPCION_CORTA);
			return false;
		}
		return true;
	}

}
