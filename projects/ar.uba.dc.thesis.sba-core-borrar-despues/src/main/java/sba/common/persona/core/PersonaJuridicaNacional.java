/*
 * $Id: PersonaJuridicaNacional.java,v 1.4 2008/01/04 19:13:37 cvschioc Exp $
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

import sba.common.persona.identificacion.InformacionImpositiva;
import sba.common.persona.identificacion.Rpc;
import sba.common.persona.identificacion.TipoInscripcionPersonaJuridicaNacional;

/**
 * 
 * 
 * @author Miguel Díaz
 * @version $Revision: 1.4 $ - $Date: 2008/01/04 19:13:37 $
 */
public class PersonaJuridicaNacional extends PersonaJuridica {

	public PersonaJuridicaNacional() {
		super();
		this.rpc = new Rpc();
		this.informacionImpositiva = new InformacionImpositiva();
	}
	
	public PersonaJuridicaNacional(String denominacion, String numeroInscripcion,
			ConstitucionPersonaJuridica constitucionPersonaJuridica, List<Acta> actas,
			InformacionImpositiva informacionImpositiva, Rpc rpc,
			TipoInscripcionPersonaJuridicaNacional tipoInscripcionPersonaJuridicaNacional) {
		super(denominacion, numeroInscripcion, constitucionPersonaJuridica, actas);
		this.informacionImpositiva = informacionImpositiva;
		this.rpc = rpc;
		this.tipoInscripcionPersonaJuridicaNacional = tipoInscripcionPersonaJuridicaNacional;
	}

	public PersonaJuridicaNacional(String denominacion, String numeroInscripcion,
			ConstitucionPersonaJuridica constitucionPersonaJuridica,
			InformacionImpositiva informacionImpositiva, Rpc rpc,
			TipoInscripcionPersonaJuridicaNacional tipoInscripcionPersonaJuridicaNacional) {
		super(denominacion, numeroInscripcion, constitucionPersonaJuridica);
		this.informacionImpositiva = informacionImpositiva;
		this.rpc = rpc;
		this.tipoInscripcionPersonaJuridicaNacional = tipoInscripcionPersonaJuridicaNacional;
	}

	@Override
	public boolean isNacional() {
		return true;
	}

	@Override
	public String getIdentificadorUnivoco() {
		return (this.getDenominacion() + " - " + this.informacionImpositiva.toString());
	}

	@Override
	public String getDescripcionTipoParte() {
		return "Persona Jurídica Nacional";
	}

	public InformacionImpositiva getInformacionImpositiva() {
		if(this.informacionImpositiva == null) {
			this.informacionImpositiva = new InformacionImpositiva();
		}
		return informacionImpositiva;
	}

	public void setInformacionImpositiva(InformacionImpositiva informacionImpositiva) {
		this.informacionImpositiva = informacionImpositiva;
	}

	public Rpc getRpc() {
		if(this.rpc == null) {
			this.rpc = new Rpc();
		}
		return rpc;
	}

	public void setRpc(Rpc rpc) {
		this.rpc = rpc;
	}

	public TipoInscripcionPersonaJuridicaNacional getTipoInscripcionPersonaJuridicaNacional() {
		return tipoInscripcionPersonaJuridicaNacional;
	}

	public void setTipoInscripcionPersonaJuridicaNacional(
			TipoInscripcionPersonaJuridicaNacional tipoInscripcionPersonaJuridicaNacional) {
		this.tipoInscripcionPersonaJuridicaNacional = tipoInscripcionPersonaJuridicaNacional;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if ((obj != null) && (this.getClass().isInstance(obj))) {
			equals = this.equalsTo((PersonaJuridicaNacional) obj);
		}
		return equals && super.equals(this);
	}

	public boolean equalsTo(PersonaJuridicaNacional personaJuridica) {
		boolean equals = true;
		if (this.informacionImpositiva != null) {
			equals = equals
					&& this.informacionImpositiva.equals(personaJuridica.informacionImpositiva);
		} else {
			equals = equals && (personaJuridica.informacionImpositiva == null);
		}
		if (this.rpc != null) {
			equals = equals && this.rpc.equals(personaJuridica.rpc);
		} else {
			equals = equals && (personaJuridica.rpc == null);
		}
		if (this.tipoInscripcionPersonaJuridicaNacional != null) {
			equals = equals
					&& this.tipoInscripcionPersonaJuridicaNacional
							.equals(personaJuridica.tipoInscripcionPersonaJuridicaNacional);
		} else {
			equals = equals && (personaJuridica.tipoInscripcionPersonaJuridicaNacional == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result;
		result = super.hashCode();
		result = PRIMO
				* result
				+ ((this.informacionImpositiva == null) ? 0 : this.informacionImpositiva.hashCode());
		result = PRIMO * result + ((this.rpc == null) ? 0 : this.rpc.hashCode());
		result = PRIMO
				* result
				+ ((this.tipoInscripcionPersonaJuridicaNacional == null) ? 0
						: this.tipoInscripcionPersonaJuridicaNacional.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append(super.toString());
		final String STR_NULO = "";
		sb.append(this.informacionImpositiva != null ? informacionImpositiva : STR_NULO);
		sb.append(" ");
		sb.append(this.rpc != null ? rpc.toString() : STR_NULO);
		sb.append(" ");
		sb
				.append(this.tipoInscripcionPersonaJuridicaNacional != null ? tipoInscripcionPersonaJuridicaNacional
						.toString()
						: STR_NULO);
		sb.append(" ");
		return sb.toString();
	}


	private InformacionImpositiva informacionImpositiva;

	private Rpc rpc;

	private TipoInscripcionPersonaJuridicaNacional tipoInscripcionPersonaJuridicaNacional;

	private static final long serialVersionUID = 1L;
}
