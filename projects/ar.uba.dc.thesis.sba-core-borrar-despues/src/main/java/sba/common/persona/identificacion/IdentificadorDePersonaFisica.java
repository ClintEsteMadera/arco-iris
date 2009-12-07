/*
 * $Id: IdentificadorDePersonaFisica.java,v 1.2 2007/10/02 21:09:36 cvschioc Exp $
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

import sba.common.persistence.BasePersistentObject;

public class IdentificadorDePersonaFisica extends BasePersistentObject {

	public IdentificadorDePersonaFisica() {
		super();
	}
	
	public IdentificadorDePersonaFisica(Long numero,
			TipoIdentificadorPersonaFisica identificadorPersonaFisica) {
		super();
		this.numero = numero;
		this.tipoIdentificadorPersonaFisica = identificadorPersonaFisica;
	}

	public TipoIdentificadorPersonaFisica getTipoIdentificadorPersonaFisica() {
		return tipoIdentificadorPersonaFisica;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public void setTipoIdentificadorPersonaFisica(
			TipoIdentificadorPersonaFisica tipoIdentificadorPersonaFisica) {
		this.tipoIdentificadorPersonaFisica = tipoIdentificadorPersonaFisica;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof IdentificadorDePersonaFisica) {
			equals = this.equalsTo((IdentificadorDePersonaFisica) obj);
		}
		return equals;
	}

	private boolean equalsTo(IdentificadorDePersonaFisica identificadorDePersonaFisica) {
		boolean equals = true;
		if (this.numero != null) {
			equals = equals && this.numero.equals(identificadorDePersonaFisica.numero);
		} else {
			equals = equals && (identificadorDePersonaFisica.numero == null);
		}
		if (this.tipoIdentificadorPersonaFisica != null) {
			equals = equals
					&& this.tipoIdentificadorPersonaFisica
							.equals(identificadorDePersonaFisica.tipoIdentificadorPersonaFisica);
		} else {
			equals = equals
					&& (identificadorDePersonaFisica.tipoIdentificadorPersonaFisica == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.numero == null) ? 0 : this.numero.hashCode());
		result = PRIMO
				* result
				+ ((this.tipoIdentificadorPersonaFisica == null) ? 0
						: this.tipoIdentificadorPersonaFisica.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.numero != null ? numero.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.tipoIdentificadorPersonaFisica != null ? tipoIdentificadorPersonaFisica
				.toString() : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	private Long numero;

	private TipoIdentificadorPersonaFisica tipoIdentificadorPersonaFisica;

	private static final long serialVersionUID = 1L;
}