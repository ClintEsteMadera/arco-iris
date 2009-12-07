 /*
 * $Id: PersonaJuridica.java,v 1.4 2008/05/06 18:34:00 cvsmarco Exp $
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
package sba.common.persona.core;

import java.util.ArrayList;
import java.util.List;

import sba.common.annotations.DataTypeAnnotation;
import sba.common.validation.CommonDataTypes;

/**
 * 
 *
 * @author Miguel Díaz
 * @version $Revision: 1.4 $ - $Date: 2008/05/06 18:34:00 $
 */
public abstract class PersonaJuridica extends Parte {

	protected PersonaJuridica() {
		super();
		this.constitucionPersonaJuridica = new ConstitucionPersonaJuridica();
		this.actas = new ArrayList<Acta>();
	}
	
	public PersonaJuridica(String denominacion, String numeroInscripcion, 
			ConstitucionPersonaJuridica constitucionPersonaJuridica, List<Acta> actas) {
		super();
		this.denominacion = denominacion;
		this.numeroInscripcion = numeroInscripcion;
		this.constitucionPersonaJuridica = constitucionPersonaJuridica;
		this.actas = actas;
	}
	
	public PersonaJuridica(String denominacion, String numeroInscripcion, 
			ConstitucionPersonaJuridica constitucionPersonaJuridica) {
		super();
		this.denominacion = denominacion;
		this.numeroInscripcion = numeroInscripcion;
		this.constitucionPersonaJuridica = constitucionPersonaJuridica;
		this.actas = new ArrayList<Acta>();
	}
	
	public abstract boolean isNacional();
	
	public List<Acta> getActas() {
		return actas;
	}

	public void setActas(List<Acta> actas) {
		this.actas = actas;
	}

	public void agregarActa(Acta acta) {
		if (acta != null) {
			this.actas.add(acta);			
		}
	}
	
	public ConstitucionPersonaJuridica getConstitucionPersonaJuridica() {
		if(this.constitucionPersonaJuridica == null) {
			this.constitucionPersonaJuridica = new ConstitucionPersonaJuridica();
		}
		return constitucionPersonaJuridica;
	}

	public void setConstitucionPersonaJuridica(
			ConstitucionPersonaJuridica constitucionPersonaJuridica) {
		this.constitucionPersonaJuridica = constitucionPersonaJuridica;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@DataTypeAnnotation(typeId = CommonDataTypes.PJ_NUM)
	public String getNumeroInscripcion() {
		return numeroInscripcion;
	}

	public void setNumeroInscripcion(String numeroInscripcion) {
		this.numeroInscripcion = numeroInscripcion;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if ((obj != null) && (this.getClass().isInstance(obj))) {
			equals = this.equalsTo((PersonaJuridica) obj);
		}
		return equals;
	}

	public boolean equalsTo(PersonaJuridica personaJuridica) {
		boolean equals = true;
		if (this.denominacion != null) {
			equals = equals && this.denominacion.equals(personaJuridica.denominacion);
		} else {
			equals = equals && (personaJuridica.denominacion == null);
		}
		if (this.numeroInscripcion != null) {
			equals = equals && this.numeroInscripcion.equals(personaJuridica.numeroInscripcion);
		} else {
			equals = equals && (personaJuridica.numeroInscripcion == null);
		}
		if (this.constitucionPersonaJuridica != null) {
			equals = equals && this.constitucionPersonaJuridica.equals(personaJuridica.
					constitucionPersonaJuridica);
		} else {
			equals = equals && (personaJuridica.constitucionPersonaJuridica == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.denominacion == null) ? 0 : this.denominacion.hashCode());
		result = PRIMO * result + ((this.numeroInscripcion == null) ? 0 : 
			this.numeroInscripcion.hashCode());
		result = PRIMO * result + ((this.constitucionPersonaJuridica == null) ? 0 : 
			this.constitucionPersonaJuridica.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.denominacion != null ? denominacion : STR_NULO);
		sb.append(" ");
		sb.append(this.numeroInscripcion != null ? numeroInscripcion.toString() : STR_NULO);
		sb.append(" ");			
		sb.append(this.constitucionPersonaJuridica != null ? 
				constitucionPersonaJuridica.toString() : STR_NULO);
		sb.append(" ");			
		return sb.toString();
	}
	
	private String denominacion;
	
	protected String numeroInscripcion;
	
	private ConstitucionPersonaJuridica constitucionPersonaJuridica;
	
	private List<Acta> actas;
	
}
