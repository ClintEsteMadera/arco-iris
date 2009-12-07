package sba.common.persona.identificacion;

import java.io.Serializable;

/**
 * 
 *
 * @author Miguel D�az
 * @version $Revision: 1.3 $ - $Date: 2008/05/16 20:18:45 $
 */
public enum TipoInscripcionPersonaJuridicaNacional implements Serializable {
	
	IGJ("Inspecci�n General de Justicia", "IGJ"),
	
	RPC("RPC", "RPC"),
	
	OTROS("Otros", "Otros");

	/**
	 * Retorna la descripci�n del elemento.
	 * 
	 * @return descripci�n del elemento
	 */
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * Retorna una representaci�n textual normalizada que denota a este elemento (a los efectos de
	 * ser usada como referencia ante necesidades de persistencia o transporte del valor)
	 * 
	 * @return texto normalizado que denota a este elemento
	 */
	public String getNormalizedDescripcion() {
		return this.normalizedDescription;
	}

	/**
	 * Retorna una representaci�n textual de la instancia.
	 * 
	 * @return texto que denota el estado de la instancia
	 */
	@Override
	public String toString() {
		return this.descripcion;
	}

	/**
	 * Constructor parametrizado con la descripci�n del elemento.
	 * 
	 * @param descripcion
	 *            descripci�n del tipo de inscripci�n
	 * @param normalizedDesc
	 *            descripci�n normalizada (a los efectos de ser usada como referencia ante
	 *            necesidades de persistencia o transporte de los valores)
	 */
	private TipoInscripcionPersonaJuridicaNacional(String descripcion, String normalizedDesc) {
		this.descripcion = descripcion;
		this.normalizedDescription = normalizedDesc;
	}

	/**
	 * Almancena la descripci�n del elemento.
	 */
	private String descripcion;

	/**
	 * Almacena la descripci�n normalizada del elemento.
	 */
	private String normalizedDescription;

}
