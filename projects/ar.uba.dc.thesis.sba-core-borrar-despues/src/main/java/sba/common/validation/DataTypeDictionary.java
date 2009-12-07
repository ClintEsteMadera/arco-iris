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
			throw new IllegalArgumentException("No se registró validador para el tipo '" +
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
