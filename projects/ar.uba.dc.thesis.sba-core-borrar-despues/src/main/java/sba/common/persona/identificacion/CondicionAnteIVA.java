/*
 * $Id: CondicionAnteIVA.java,v 1.1 2007/10/02 20:56:00 cvschioc Exp $
 *
 * Copyright (c) 2003 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, República Argentina.
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores
 * S.A. ("Información Confidencial"). Usted no divulgará tal Información
 * Confidencial y solamente la usará conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */

package sba.common.persona.identificacion;

/**
 * Representación de la condición ante el IVA (Impuesto al Valor Agregado) de una persona.
 * 
 * @author Miguel Díaz
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ - $Date: 2007/10/02 20:56:00 $
 */
public enum CondicionAnteIVA {

	/**
	 * Condición ante el IVA: Responsable Inscripto.
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
	 * Condición ante el IVA: Responsable No Inscripto.
	 */
	RESPONSABLE_NO_INSCRIPTO(4L, "Responsable No Inscripto", "ResponsableNoInscripto") {
	    @Override
		public boolean isResponsable() {
	    	return true;
	    }
	},
	
	/**
	 * Condición ante el IVA: Exento.
	 */
	IVA_EXENTO(3L, "Exento", "Exento") {
	    @Override
		public boolean isExento() {
	    	return false;
	    }
	},
	
    /**
     * Condición ante el IVA: Responsable Monotributo.
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
     * Condición ante el IVA: Consumidor final.
     */
    CONSUMIDOR_FINAL(5L, "Consumidor Final", "ConsumidorFinal") {
	    @Override
		public boolean isConsumidorFinal() {
	    	return false;
	    }
	},
	
    /**
     * Condición ante el IVA: No Responsable.
     */
	NO_RESPONSABLE(10L, "No Responsable", "NoResponsable") {
	    @Override
		public boolean isNoResponsable() {
	    	return false;
	    }
	};
	
    /**
     * Obtiene el código asociado.
     */
    public Long getCodigo() {
        return this.codigo;
    }

    /**
     * Retorna la descripción del elemento.
     * 
     * @return descripción del elemento
     */
    public String getDescripcion(){
        return this.descripcion;
    }
    
    /**
     * Retorna una representación textual normalizada que denota a este elemento (a los efectos de 
     * ser usada como referencia ante necesidades de persistencia o transporte del valor)
     * 
     * @return texto normalizado que denota a este elemento 
     */
    public String getNormalizedDescripcion(){
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
     * Verificación de si el elemento corresponde a un responsable.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condición que se desee que respondan <code>true</code>.
     */
    public boolean isResponsable() {
    	return false;
    }

    /**
     * Verificación de si el elemento corresponde a un no responsable.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condición que se desee que respondan <code>true</code>.
     */
    public boolean isNoResponsable() {
    	return false;
    }
    
    /**
     * Verificación de si el elemento corresponde a un inscripto.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condición que se desee que respondan <code>true</code>.
     */
    public boolean isInscripto() {
    	return false;
    }
	
    /**
     * Verificación de si el elemento corresponde a un exento.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condición que se desee que respondan <code>true</code>.
     */
    public boolean isExento() {
    	return false;
    }
    
    /**
     * Verificación de si el elemento corresponde a un monotributista.
     * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condición que se desee que respondan <code>true</code>.
     */
    public boolean isMonotributista() {
    	return false;
    }
    
    /**
	 * Verificación de si el elemento corresponde a un consumidor final.
	 * 
	 * @return por defecto siempre responde <code>false</code>. Sobreescribir el comportamiento
	 *         para cada uno de los tipos de condición que se desee que respondan <code>true</code>.
	 */
    public boolean isConsumidorFinal() {
    	return false;
    }
    
	/**
	 * Constructor parametrizado.
	 *
     * @param codigo Código de la condición ante el IVA
     * @param descripcion Descripción de la condición ante el IVA
     * @param normalizedDesc Descripción normalizada (a los efectos de ser usada como referencia 
     * ante necesidades de persistencia o transporte de los valores)
	 */
	private CondicionAnteIVA(Long codigo, String descripcion, String normalizedDesc) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.normalizedDescription = normalizedDesc;
	}

    private final Long codigo;

    /**
     * Almancena la descripción del elemento.
     */
    private final String descripcion;

    /**
     * Almacena la descripción normalizada del elemento.
     */
    private final String normalizedDescription;
}