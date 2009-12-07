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
* $Id: DataTypeDictionary.java,v 1.3 2008/03/18 20:29:24 cvspasto Exp $
*/

package sba.common.validation;

import java.util.HashMap;


/**
 * Diccionario para 'tipos de datos'
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/03/18 20:29:24 $
 */
public class DataTypeDictionary {

	public DataTypeDictionary(){
		this.validators = new HashMap<String, Validator>();
	}
	
	public void putStringType(String id, StringConstraints c){
		validators.put(id, c);
	}

	public void putNumericType(String id, NumericConstraints c){
		validators.put(id, c);
	}
	
	public StringConstraints getStringType(String id){
		return (StringConstraints)validators.get(id);
	}

	public NumericConstraints getNumericType(String id){
		return (NumericConstraints)validators.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public boolean validate(Object o, String typeId,ValidationResult result, String location,
			String valueDescription){
		
		final Validator validator=getTypeValidator(typeId);
		
		if(validator == null){
			throw new IllegalArgumentException("No se registr� validador para el tipo '" +
					typeId + "'");
		}
		
		if(location != null){
			result.pushNestedPath(location);
		}
		
		if(valueDescription != null){
			result.pushValueDescription(valueDescription);
		}
		
		boolean isValid=validator.validate(o, result);
		
		if(location != null){
			result.popNestedPath();
		}

		if(valueDescription != null){
			result.popValueDescription();
		}
		
		return isValid;
	}

	Validator getTypeValidator(String typeId){
		return validators.get(typeId);
	}

	private HashMap<String,Validator> validators;
}
