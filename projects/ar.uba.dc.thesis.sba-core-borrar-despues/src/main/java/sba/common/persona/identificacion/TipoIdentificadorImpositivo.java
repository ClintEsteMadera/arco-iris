package sba.common.persona.identificacion;

import java.io.Serializable;

/**
 * 
 *
 * @author Miguel D�az
 * @version $Revision: 1.2 $ - $Date: 2008/01/07 19:55:02 $
 */
public enum TipoIdentificadorImpositivo implements Serializable {
           
    CUIT("CUIT"),
    
    CUIL("CUIL"),
    
    CDI("CDI");

    /**
     * Retorna la descripci�n del elemento.
     * 
     * @return descripci�n del elemento
     */
    public String getDescripcion(){
        return this.descripcion;
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
     * @param descripcion descripci�n del tipo de identificador de AFIP
     */
    private TipoIdentificadorImpositivo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Almacena la descripci�n del elemento.
     */
    private String descripcion;
}