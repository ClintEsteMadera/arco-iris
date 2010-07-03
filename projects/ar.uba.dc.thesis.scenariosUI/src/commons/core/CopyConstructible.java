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
 * $Id: CopyConstructible.java,v 1.1 2007/10/02 20:56:02 cvschioc Exp $
 */
package commons.core;

/**
 *	Similar a clonable pero eliminando la información de persistencia utilizada por Hibernate
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.1 $ $Date: 2007/10/02 20:56:02 $
 */
public interface CopyConstructible<T> {

	/*
	 * Nota: esta clase fue creada para ser utilizada en el despliegue de Depositantes y Cuentas,
	 * donde un objeto de un sistema es pasado como parámetro a un servicio de otro sistema,
	 * pero al convivir las aplicaciones se necesita un nuevo objeto igual pero sin la 
	 * informaci{on de persistencia.  
	 */
	
	/**
	 * Similar a clone pero sin clonar la información de persistencia
	 */
	T copyConstructor();
	
}

