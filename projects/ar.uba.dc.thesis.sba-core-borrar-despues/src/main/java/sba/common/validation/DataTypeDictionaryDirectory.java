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
			throw new IllegalArgumentException("No se registró validador para el tipo '" +
					id + "'");
		}
		
		return validator.validate(value, result);
	}

	private List<DataTypeDictionary> dictionaries = new ArrayList<DataTypeDictionary>();

}