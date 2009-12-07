package commons.gui.model.types;

import java.text.Format;

/**
 * Información de configuración asociada a un tipo de dato.
 * 
 * <br>
 * <br>
 * La interface provee la información básica que puede ser utilizada
 * para editar, formatear y configurar componentes visuales.
 * 
 * @author P.Pastorino.
 */
public interface EditConfiguration {
	
	/**
	 * Obtiene el formato.
	 * 
	 * @return
	 */
	public Format getFormat();
	
	/**
	 * Obtiene un prototipo para editar el tipo de dato.
	 * El prototipo puede ser utilizado por ejemplo, para
	 * estimar el tamaño requerido por un componente visual "bindeado"
	 * al tipo de dato.
	 *  
	 * @return
	 */
	public Object getPrototype();

	/**
	 * Obtiene un prototipo para mostrar el tipo de dato en una columna.
	 *  
	 * @return
	 */
	public Object getColumnPrototype();

	/**
	 * Obtiene la alineación
	 * 
	 * @return <code>true</code> si el campo debe mostrarse alineado
	 * a la derecha.
	 */
	public boolean isRightAligned();
}
