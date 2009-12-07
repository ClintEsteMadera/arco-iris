package sba.common.persona.identificacion;

import java.io.Serializable;

/**
 * 
 *
 * @author Miguel Díaz
 * @version $Revision: 1.3 $ - $Date: 2008/05/16 20:18:45 $
 */
public enum TipoInscripcionPersonaJuridicaNacional implements Serializable {
	
	IGJ("Inspección General de Justicia", "IGJ"),
	
	RPC("RPC", "RPC"),
	
	OTROS("Otros", "Otros");

	/**
	 * Retorna la descripción del elemento.
	 * 
	 * @return descripción del elemento
	 */
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * Retorna una representación textual normalizada que denota a este elemento (a los efectos de
	 * ser usada como referencia ante necesidades de persistencia o transporte del valor)
	 * 
	 * @return texto normalizado que denota a este elemento
	 */
	public String getNormalizedDescripcion() {
		return this.normalizedDescription;
	}

	/**
	 * Retorna una representación textual de la instancia.
	 * 
	 * @return texto que denota el estado de la instancia
	 */
	@Override
	public String toString() {
		return this.descripcion;
	}

	/**
	 * Constructor parametrizado con la descripción del elemento.
	 * 
	 * @param descripcion
	 *            descripción del tipo de inscripción
	 * @param normalizedDesc
	 *            descripción normalizada (a los efectos de ser usada como referencia ante
	 *            necesidades de persistencia o transporte de los valores)
	 */
	private TipoInscripcionPersonaJuridicaNacional(String descripcion, String normalizedDesc) {
		this.descripcion = descripcion;
		this.normalizedDescription = normalizedDesc;
	}

	/**
	 * Almancena la descripción del elemento.
	 */
	private String descripcion;

	/**
	 * Almacena la descripción normalizada del elemento.
	 */
	private String normalizedDescription;

}
