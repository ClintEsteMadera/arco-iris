package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Atributo o propiedad de un objeto.
 * 
 * <br>
 * <br>
 * Esta interface representa una determinada propiedad o "atributo" de un objeto,
 * y puede ser utilizada para consultar o modificar el valor de la propiedad
 * o para acceder a meta-información asociada a la propiedad. 
 *  
 * @author P.Pastorino
 */
public interface Attribute {

	/**
	 * Modifica el valor del atributo de un objeto.
	 * 
	 * @param target Objeto que se quiere modificar 
	 * @param value  Nuevo valor
	 */
	public void setValue(Object target, Object value);

	/**
	* Obtiene el valor del atributo de un objeto.
	* 
	* @param target Objeto que se quiere consultar
	* 
	* @return valor del atributo.
	*/
	public Object getValue(Object target);

	/**
	* Obtiene el tipo de dato del atributo.
	*/
	public EditType getValueType();
}
