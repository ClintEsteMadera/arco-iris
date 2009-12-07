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
 * $Id: ContactoCompareHelper.java,v 1.2 2008/01/07 13:44:02 cvschioc Exp $
 */

package sba.common.contacto.core;

import java.util.Set;

import sba.common.contacto.Contacto;

/**
 * Clase Helper que compara objetos de la librería de Contactos.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/01/07 13:44:02 $
 */

public abstract class ContactoCompareHelper {

	public static boolean compare(Contacto contacto1, Contacto contacto2) {
		boolean equals = true && contacto1.getDenominacion().equals(contacto2.getDenominacion())
				&& contacto1.getTipoContacto().equals(contacto2.getTipoContacto())
				&& contacto1.getTipoMedioContacto().equals(contacto2.getTipoMedioContacto());

		return equals;
	}

	public static <O extends Object> boolean containsAll(Set<O> set1, Set<O> set2) {
		boolean contains = true;
		for (O o : set2) {
			contains = contains && contains(set1, o);
		}
		return contains;
	}

	public static <O extends Object> boolean contains(Set<O> set, O obj) {
		boolean contain = false;
		for (O o : set) {
			contain = contain || o.equals(obj);
		}
		return contain;
	}

}