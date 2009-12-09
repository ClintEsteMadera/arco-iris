/*
 * $Id: EnumUtils.java,v 1.5 2009/06/23 20:12:57 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Adrian
 * @version $Revision: 1.5 $ $Date: 2009/06/23 20:12:57 $
 */
public final class EnumUtils {

	@SuppressWarnings("unchecked")
	public static <T extends java.lang.Enum<T>> T[] getEnumsOrderedByToString(Class<T> enumType) {
		T[] enumsOrdered = (T[]) maps.get(enumType);
		if (enumsOrdered == null) {
			T[] values = enumType.getEnumConstants();
			Arrays.sort(values, 0, values.length, comparator);
			enumsOrdered = values;
			maps.putIfAbsent(enumType, values);
		}
		return enumsOrdered;
	}

	public static <T extends java.lang.Enum<T>> boolean containsToString(Class<T> enumType,
	        String toString) {
		T[] enumConstants = enumType.getEnumConstants();
		for (java.lang.Enum<?> value : enumConstants) {
			if (value.toString().equals(toString)) {
				return true;
			}
		}
		return false;
	}

	public static <T extends java.lang.Enum<T>> T getEnumFromToString(Class<T> enumType,
	        String toString) {
		T[] enumConstants = enumType.getEnumConstants();
		for (T value : enumConstants) {
			if (value.toString().equals(toString)) {
				return value;
			}
		}
		throw new IllegalArgumentException("No enum const " + enumType + "." + toString);
	}

	private EnumUtils() {
		super();
	}

	private static final Comparator<java.lang.Enum<?>> comparator =
	        new Comparator<java.lang.Enum<?>>() {
		        public int compare(java.lang.Enum<?> enum1, java.lang.Enum<?> enum2) {
			        return enum1.toString().compareTo(enum2.toString());
		        }
	        };

	private static final ConcurrentMap<Class<?>,java.lang.Enum<?>[]> maps =
	        new ConcurrentHashMap<Class<?>,java.lang.Enum<?>[]>();
}
