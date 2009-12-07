/*
 * $$Id: TipoContacto.java,v 1.1 2008/01/07 13:38:31 cvschioc Exp $$ Licencia de Caja de Valores S.A., Versión 1.0 Copyright (c) 2006 Caja de Valores S.A. 25
 * de Mayo 362 Buenos Aires, República Argentina Todos los derechos reservados. Este software es
 * información confidencial y propietaria de Caja de Valores S.A. ("Información Confidencial").
 * Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a los términos
 * del acuerdo de licencia que posee con Caja de Valores S.A.
 */

package sba.common.contacto;

import java.io.Serializable;

/**
 * Tipos válidos de contacto.
 * 
 * @author Miguel Díaz
 * @author Juan José Zapico
 * @version $Revision: 1.1 $ - $Date: 2008/01/07 13:38:31 $
 */
public enum TipoContacto implements Serializable {
	   
    LEGAL("Legal"), 
    PARTICULAR("Particular"), 
    OPERATIVO("Operativo"),
    NO_ESPECIFICADO("No Especificado");

    /**
     * Retorna la descripción del elemento.
     * 
     * @return descripción del elemento
     */
    public String descripcion(){
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
     * Constructor parametrizado con la descripción.
     * 
     * @param descripcion descripción del tipo de contacto
     */
    private TipoContacto(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Almacena la descripción del elemento.
     */
    private String descripcion;
}