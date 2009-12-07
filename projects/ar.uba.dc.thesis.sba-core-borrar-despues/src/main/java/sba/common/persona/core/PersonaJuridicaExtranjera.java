/*
 * $Id: PersonaJuridicaExtranjera.java,v 1.2 2008/01/04 19:13:37 cvschioc Exp $
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

import java.util.List;

import sba.common.pais.Country;

/**
 * 
 * 
 * @author Miguel Díaz
 * @version $Revision: 1.2 $ - $Date: 2008/01/04 19:13:37 $
 */
public class PersonaJuridicaExtranjera extends PersonaJuridica {

	public PersonaJuridicaExtranjera() {
		super();
		this.representanteLegal = new RepresentanteLegal();
	}
	
	public PersonaJuridicaExtranjera(String denominacion, String numeroInscripcion,
			ConstitucionPersonaJuridica constitucionPersonaJuridica, List<Acta> actas,
			Country nacionalidad, RepresentanteLegal representanteLegal, String zona) {
		super(denominacion, numeroInscripcion, constitucionPersonaJuridica, actas);
		this.nacionalidad = nacionalidad;
		this.representanteLegal = representanteLegal;
		this.zona = zona;
	}

	public PersonaJuridicaExtranjera(String denominacion, String numeroInscripcion,
			ConstitucionPersonaJuridica constitucionPersonaJuridica, Country nacionalidad,
			RepresentanteLegal representanteLegal, String zona) {
		super(denominacion, numeroInscripcion, constitucionPersonaJuridica);

		this.nacionalidad = nacionalidad;
		this.representanteLegal = representanteLegal;
		this.zona = zona;
	}

	@Override
	public boolean isNacional() {
		return false;
	}

	@Override
	public String getIdentificadorUnivoco() {
		return (this.getDenominacion() + " - INSC " + this.getNumeroInscripcion().toString());
	}

	@Override
	public String getDescripcionTipoParte() {
		return "Persona Jurídica Extranjera";
	}

	public Country getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Country nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public RepresentanteLegal getRepresentanteLegal() {
		if(this.representanteLegal == null) {
			this.representanteLegal = new RepresentanteLegal();
		}
		return representanteLegal;
	}

	public void setRepresentanteLegal(RepresentanteLegal representanteLegal) {
		this.representanteLegal = representanteLegal;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if ((obj != null) && (this.getClass().isInstance(obj))) {
			equals = this.equalsTo((PersonaJuridicaExtranjera) obj);
		}
		return equals && super.equals(this);
	}

	public boolean equalsTo(PersonaJuridicaExtranjera personaJuridica) {
		boolean equals = true;
		if (this.nacionalidad != null) {
			equals = equals && this.nacionalidad.equals(personaJuridica.nacionalidad);
		} else {
			equals = equals && (personaJuridica.nacionalidad == null);
		}
		if (this.representanteLegal != null) {
			equals = equals && this.representanteLegal.equals(personaJuridica.representanteLegal);
		} else {
			equals = equals && (personaJuridica.representanteLegal == null);
		}
		if (this.zona != null) {
			equals = equals && this.zona.equals(personaJuridica.zona);
		} else {
			equals = equals && (personaJuridica.zona == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result;
		result = super.hashCode();
		result = PRIMO * result + ((this.nacionalidad == null) ? 0 : this.nacionalidad.hashCode());
		result = PRIMO * result
				+ ((this.representanteLegal == null) ? 0 : this.representanteLegal.hashCode());
		result = PRIMO * result + ((this.zona == null) ? 0 : this.zona.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append(super.toString());
		final String STR_NULO = "";
		sb.append(this.nacionalidad != null ? nacionalidad.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.representanteLegal != null ? representanteLegal.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.zona != null ? zona : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	private Country nacionalidad;

	private RepresentanteLegal representanteLegal;

	private String zona;
	
	private static final long serialVersionUID = 1L;
}
