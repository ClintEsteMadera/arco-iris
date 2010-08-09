package commons.core;

/**
 * Similar a clonable pero eliminando la informaci�n de persistencia utilizada por Hibernate
 * 
 * 
 */
public interface CopyConstructible<T> {

	/*
	 * Nota: esta clase fue creada para ser utilizada en el despliegue de Depositantes y Cuentas, donde un objeto de un
	 * sistema es pasado como par�metro a un servicio de otro sistema, pero al convivir las aplicaciones se necesita un
	 * nuevo objeto igual pero sin la informaci{on de persistencia.
	 */

	/**
	 * Similar a clone pero sin clonar la informaci�n de persistencia
	 */
	T copyConstructor();

}
