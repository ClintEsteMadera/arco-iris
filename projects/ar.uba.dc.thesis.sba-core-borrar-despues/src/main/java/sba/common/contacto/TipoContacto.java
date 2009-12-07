/*
 * $$Id: TipoContacto.java,v 1.1 2008/01/07 13:38:31 cvschioc Exp $$ Licencia de Caja de Valores S.A., Versi�n 1.0 Copyright (c) 2006 Caja de Valores S.A. 25
 * de Mayo 362 Buenos Aires, Rep�blica Argentina Todos los derechos reservados. Este software es
 * informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n Confidencial").
 * Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a los t�rminos
 * del acuerdo de licencia que posee con Caja de Valores S.A.
 */

package sba.common.contacto;

import java.io.Serializable;

/**
 * Tipos v�lidos de contacto.
 * 
 * @author Miguel D�az
 * @author Juan Jos� Zapico
 * @version $Revision: 1.1 $ - $Date: 2008/01/07 13:38:31 $
 */
public enum TipoContacto implements Serializable {
	   
    LEGAL("Legal"), 
    PARTICULAR("Particular"), 
    OPERATIVO("Operativo"),
    NO_ESPECIFICADO("No Especificado");

    /**
     * Retorna la descripci�n del elemento.
     * 
     * @return descripci�n del elemento
     */
    public String descripcion(){
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
     * Constructor parametrizado con la descripci�n.
     * 
     * @param descripcion descripci�n del tipo de contacto
     */
    private TipoContacto(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Almacena la descripci�n del elemento.
     */
    private String descripcion;
}