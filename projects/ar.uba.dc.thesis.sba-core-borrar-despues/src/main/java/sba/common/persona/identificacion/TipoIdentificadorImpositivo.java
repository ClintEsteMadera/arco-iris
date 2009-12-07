package sba.common.persona.identificacion;

import java.io.Serializable;

/**
 * 
 *
 * @author Miguel Díaz
 * @version $Revision: 1.2 $ - $Date: 2008/01/07 19:55:02 $
 */
public enum TipoIdentificadorImpositivo implements Serializable {
           
    CUIT("CUIT"),
    
    CUIL("CUIL"),
    
    CDI("CDI");

    /**
     * Retorna la descripción del elemento.
     * 
     * @return descripción del elemento
     */
    public String getDescripcion(){
        return this.descripcion;
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
     * @param descripcion descripción del tipo de identificador de AFIP
     */
    private TipoIdentificadorImpositivo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Almacena la descripción del elemento.
     */
    private String descripcion;
}