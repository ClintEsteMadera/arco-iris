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
 * $Id: EnumPropertyDirectory.java,v 1.2 2008/05/08 18:28:29 cvspasto Exp $
 */

package commons.properties;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import commons.properties.CommonLabels;


/**
 * Directorio contiene todos los enumerados en dónde buscar labels para la creación de tablas con
 * preferencias.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/05/08 18:28:29 $
 */

public class EnumPropertyDirectory {

	public EnumPropertyDirectory(){
	}

	public EnumPropertyDirectory(List<Class<? extends EnumProperty>> enumProps) {
		super();
		this.enumProps = enumProps;
	}

	public void register(Class<? extends EnumProperty>... enumPropClasses) {
		for (Class<? extends EnumProperty> aClass : enumPropClasses) {
			this.register(aClass);
		}
	}

	public void register(Class<? extends EnumProperty> enumPropClass) {
		if (enumPropClass.isEnum()) {
//			// CommonLabels tiene precedencia con respecto a cualquier otro EnumProperty.
//			if (enumPropClass.equals(CommonLabels.class)) {
//				enumProps.add(0, enumPropClass);
//			} else {
				enumProps.add(enumPropClass);
//			}
		} else {
			throw new IllegalArgumentException("La clase " + enumPropClass.getName()
					+ " no es una clase enumerada!");
		}
	}

	@SuppressWarnings("unchecked")
	public EnumProperty getEnum(String name) {
		for (Class<? extends EnumProperty> enumPropClass : enumProps) {
			try {
				Enum enumConstant = Enum.valueOf((Class) enumPropClass, name);
				return (EnumProperty) enumConstant;
			} catch (Exception e) {
				// do nothing
			}
		}
		String msg = "No se pudo encontrar la constante " + name
				+ " en los EnumProperties registrados";
		if(LOG.isErrorEnabled()) {
			LOG.error(msg);
		}
		throw new RuntimeException(msg);
	}

	private List<Class<? extends EnumProperty>> enumProps = new ArrayList<Class<? extends EnumProperty>>();

	private static final Log LOG = LogFactory.getLog(EnumPropertyDirectory.class);
}