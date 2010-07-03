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
 * $Id: CopyConstructible.java,v 1.1 2007/10/02 20:56:02 cvschioc Exp $
 */
package commons.core;

/**
 *	Similar a clonable pero eliminando la informaci�n de persistencia utilizada por Hibernate
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.1 $ $Date: 2007/10/02 20:56:02 $
 */
public interface CopyConstructible<T> {

	/*
	 * Nota: esta clase fue creada para ser utilizada en el despliegue de Depositantes y Cuentas,
	 * donde un objeto de un sistema es pasado como par�metro a un servicio de otro sistema,
	 * pero al convivir las aplicaciones se necesita un nuevo objeto igual pero sin la 
	 * informaci{on de persistencia.  
	 */
	
	/**
	 * Similar a clone pero sin clonar la informaci�n de persistencia
	 */
	T copyConstructor();
	
}

