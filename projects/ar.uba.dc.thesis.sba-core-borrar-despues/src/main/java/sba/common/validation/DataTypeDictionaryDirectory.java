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
 * $Id: DataTypeDictionaryDirectory.java,v 1.1 2008/05/09 14:05:29 cvspasto Exp $
 */

package sba.common.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Directorio que contiene los diccionarios de datos activos
 */
public class DataTypeDictionaryDirectory {

	public DataTypeDictionaryDirectory() {
		super();
	}

	public void register(DataTypeDictionary dictionary) {
		dictionaries.add(dictionary);
	}

	@SuppressWarnings("unchecked")
	public boolean validate(Object value, String id, ValidationResult result) {
		Validator validator=null;
		for (DataTypeDictionary dict: this.dictionaries) {
			validator=dict.getTypeValidator(id);
			
			if(validator != null){
				break;
			}
		}
		
		if(validator == null){
			throw new IllegalArgumentException("No se registr� validador para el tipo '" +
					id + "'");
		}
		
		return validator.validate(value, result);
	}

	private List<DataTypeDictionary> dictionaries = new ArrayList<DataTypeDictionary>();

}