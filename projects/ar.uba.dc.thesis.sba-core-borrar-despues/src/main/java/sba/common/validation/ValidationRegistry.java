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
 * $Id: ValidationRegistry.java,v 1.3 2008/05/08 19:30:37 cvspasto Exp $
 */

package sba.common.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase base para los directorios de validadores.
 * Permiten validar "polimorficamente"
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/05/08 19:30:37 $
 */
public class ValidationRegistry<VALIDATOR> {

	public ValidationRegistry(Map<String, VALIDATOR> map) {
		this.map = map;
	}

	public ValidationRegistry() {
		super();
		this.map = new HashMap<String, VALIDATOR>();
	}

	public void addValidator(Class objectClass, VALIDATOR validator) {
		this.map.put(objectClass.getName(), validator);
	}

	public VALIDATOR getValidatorFor(Class objectClass) {
		VALIDATOR ret = null;
		Class clazz = objectClass;

		while (ret == null && clazz != null) {
			String s = clazz.getName();
			ret = map.get(s);

			if (ret == null) {
				if (!clazz.equals(Object.class)) {
					clazz = clazz.getSuperclass();
				} else {
					clazz = null;
				}
			}
		}

		if (ret == null) {
			throw new IllegalArgumentException("No se definio validador para la clase "
					+ objectClass.getName());
		}

		return ret;
	}

	private Map<String, VALIDATOR> map;
}
