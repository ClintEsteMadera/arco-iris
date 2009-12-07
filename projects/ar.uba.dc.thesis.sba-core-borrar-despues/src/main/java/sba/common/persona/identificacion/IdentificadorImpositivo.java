/*
 * $Id: IdentificadorImpositivo.java,v 1.5 2008/05/15 19:23:47 cvsmvera Exp $
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

import sba.common.annotations.PropertyDescription;
import sba.common.annotations.ValidateCuit;
import sba.common.persistence.BasePersistentObject;

/**
 * 
 * 
 * @author Miguel Díaz
 * @version $Revision: 1.5 $ - $Date: 2008/05/15 19:23:47 $
 */
public class IdentificadorImpositivo extends BasePersistentObject {

	public IdentificadorImpositivo() {
		super();
	}
	
	public IdentificadorImpositivo(Long numero,
			TipoIdentificadorImpositivo tipoIdentificadorImpositivo) {
		super();
		this.numero = numero;
		this.tipoIdentificadorImpositivo = tipoIdentificadorImpositivo;
	}

	/* CUIT */
	@PropertyDescription(value="CUIT")
	@ValidateCuit
	public Long getNumero() {
		return numero;
	}

	public TipoIdentificadorImpositivo getTipoIdentificadorImpositivo() {
		return tipoIdentificadorImpositivo;
	}
	
	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public void setTipoIdentificadorImpositivo(TipoIdentificadorImpositivo tipoIdentificadorImpositivo) {
		this.tipoIdentificadorImpositivo = tipoIdentificadorImpositivo;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof IdentificadorImpositivo) {
			equals = this.equalsTo((IdentificadorImpositivo) obj);
		}
		return equals;
	}

	private boolean equalsTo(IdentificadorImpositivo identificadorImpositivo) {
		boolean equals = true;
		if (this.numero != null) {
			equals = equals && this.numero.equals(identificadorImpositivo.numero);
		} else {
			equals = equals && (identificadorImpositivo.numero == null);
		}
		if (this.tipoIdentificadorImpositivo != null) {
			equals = equals
					&& this.tipoIdentificadorImpositivo
							.equals(identificadorImpositivo.tipoIdentificadorImpositivo);
		} else {
			equals = equals && (identificadorImpositivo.tipoIdentificadorImpositivo == null);
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
				+ ((this.tipoIdentificadorImpositivo == null) ? 0
						: this.tipoIdentificadorImpositivo.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.numero != null ? numero.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.tipoIdentificadorImpositivo != null ? tipoIdentificadorImpositivo.toString()
				: STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	public boolean isEmpty(boolean ignoreTipoIdentificadorImpositivo) {
		boolean isEmpty = this.numero == null;
		if (!ignoreTipoIdentificadorImpositivo) {
			isEmpty = isEmpty && this.tipoIdentificadorImpositivo == null;
		}
		return isEmpty;
	}
	
	public boolean isEmpty() {
		return isEmpty(false);
	}
	
	private Long numero;

	private TipoIdentificadorImpositivo tipoIdentificadorImpositivo;

	private static final long serialVersionUID = 1L;
}