/*
 * $Id: CondicionAnteIVA.java,v 1.1 2007/10/02 20:56:00 cvschioc Exp $
 *
 * Copyright (c) 2003 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, Rep�blica Argentina.
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores
 * S.A. ("Informaci�n Confidencial"). Usted no divulgar� tal Informaci�n
 * Confidencial y solamente la usar� conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */

package sba.common.persona.identificacion;

/**
 * Representaci�n de la condici�n ante el IVA (Impuesto al Valor Agregado) de una persona.
 * 
 * @author Miguel D�az
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ - $Date: 2007/10/02 20:56:00 $
 */
public enum CondicionAnteIVA {

	/**
	 * Condici�n ante el IVA: Responsable Inscripto.
	 */
	RESPONSABLE_INSCRIPTO(1L, "Responsable Inscripto", "ResponsableInscripto") {
	    @Override
		public boolean isResponsable() {
	    	return true;
	    }

	    @Override
		public boolean isInscripto() {
	    	return true;
	    }
	},

	/**
	 * Condici�n ante el IVA: Responsable No Inscripto.
	 */
	RESPONSABLE_NO_INSCRIPTO(4L, "Responsable No Inscripto", "ResponsableNoInscripto") {
	    @Override
		public boolean isResponsable() {
	    	return true;
	    }
	},
	
	/**
	 * Condici�n ante el IVA: Exento.
	 */
	IVA_EXENTO(3L, "Exento", "Exento") {
	    @Override
		public boolean isExento() {
	    	return false;
	    }
	},
	
    /**
     * Condici�n ante el IVA: Responsable Monotributo.
     */
    RESPONSABLE_MONOTRIBUTO(9L, "Responsable Monotributo", "ResponsableMonotributo") {
	    @Override
		public boolean isResponsable() {
	    	return false;
	    }
		
		@Override
		public boolean isMonotributista() {
	    	return false;
	    }
	},

    /**
     * Condici�n ante el IVA: Consumidor final.
     */
    CONSUMIDOR_FINAL(5L, "Consumidor Final", "ConsumidorFinal") {
	    @Override
		public boolean isConsumidorFinal() {
	    	return false;
	    }
	},
	
    /**
     * Condici�n ante el IVA: No Responsable.
     */
	NO_RESPONSABLE(10L, "No Responsable", "NoResponsable") {
	    @Override
		public boolean isNoResponsable() {
	    	return false;
	    }
	};
	
    /**
     * Obtiene el c�digo asociado.
     */
    public Long getCodigo() {
        return this.codigo;
    }

    /**
     * Retorna la descripci�n del elemento.
     * 
     * @return descripci�n del elemento
     */
    public String getDescripcion(){
        return this.descripcion;
    }
    
    /**
     * Retorna una representaci�n textual normalizada que denota a este elemento (a los efectos de 
     * ser usada como referencia ante necesidades de persistencia o transporte del valor)
     * 
     * @return texto normalizado que denota a este elemento 
     */
    public String getNormalizedDescripcion(){
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
     * Verificaci�n de si el elemento corresponde a un responsable.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condici�n que se desee que respondan <code>true</code>.
     */
    public boolean isResponsable() {
    	return false;
    }

    /**
     * Verificaci�n de si el elemento corresponde a un no responsable.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condici�n que se desee que respondan <code>true</code>.
     */
    public boolean isNoResponsable() {
    	return false;
    }
    
    /**
     * Verificaci�n de si el elemento corresponde a un inscripto.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condici�n que se desee que respondan <code>true</code>.
     */
    public boolean isInscripto() {
    	return false;
    }
	
    /**
     * Verificaci�n de si el elemento corresponde a un exento.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condici�n que se desee que respondan <code>true</code>.
     */
    public boolean isExento() {
    	return false;
    }
    
    /**
     * Verificaci�n de si el elemento corresponde a un monotributista.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condici�n que se desee que respondan <code>true</code>.
     */
    public boolean isMonotributista() {
    	return false;
    }
    
    /**
	 * Verificaci�n de si el elemento corresponde a un consumidor final.
	 * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condici�n que se desee que respondan <code>true</code>.
	 */
    public boolean isConsumidorFinal() {
    	return false;
    }
    
	/**
	 * Constructor parametrizado.
	 *
     * @param codigo C�digo de la condici�n ante el IVA
     * @param descripcion Descripci�n de la condici�n ante el IVA
     * @param normalizedDesc Descripci�n normalizada (a los efectos de ser usada como referencia 
     * ante necesidades de persistencia o transporte de los valores)
	 */
	private CondicionAnteIVA(Long codigo, String descripcion, String normalizedDesc) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.normalizedDescription = normalizedDesc;
	}

    private final Long codigo;

    /**
     * Almancena la descripci�n del elemento.
     */
    private final String descripcion;

    /**
     * Almacena la descripci�n normalizada del elemento.
     */
    private final String normalizedDescription;
}