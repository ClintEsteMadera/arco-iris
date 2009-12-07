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
