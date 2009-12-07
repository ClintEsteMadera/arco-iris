/*
 * $Id: Rpc.java,v 1.4 2008/01/08 14:03:34 cvschioc Exp $
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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import sba.common.pais.CountrySubdivision_AR;

/**
 * 
 * 
 * @author Miguel Díaz
 * @version $Revision: 1.4 $ - $Date: 2008/01/08 14:03:34 $
 */
public class Rpc implements Serializable {

	public Rpc() {
		super();
	}

	public Rpc(String numeroRPC, CountrySubdivision_AR lugar) {
		super();
		this.numeroRPC = numeroRPC;
		this.lugar = lugar;
	}

	public CountrySubdivision_AR getLugar() {
		return lugar;
	}

	public String getNumeroRPC() {
		return numeroRPC;
	}

	public void setLugar(CountrySubdivision_AR lugar) {
		this.lugar = lugar;
	}

	public void setNumeroRPC(String numeroRPC) {
		this.numeroRPC = numeroRPC;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if ((obj != null) && (this.getClass().isInstance(obj))) {
			equals = this.equalsRPC((Rpc) obj);
		}
		return equals;
	}

	public boolean equalsRPC(Rpc rpc) {
		boolean equals = true;
		if (this.lugar != null) {
			equals = equals && this.lugar.equals(rpc.lugar);
		} else {
			equals = equals && (rpc.lugar == null);
		}
		if (this.numeroRPC != null) {
			equals = equals && this.numeroRPC.equals(rpc.numeroRPC);
		} else {
			equals = equals && (rpc.numeroRPC == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.lugar == null) ? 0 : this.lugar.hashCode());
		result = PRIMO * result + ((this.numeroRPC == null) ? 0 : this.numeroRPC.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.numeroRPC != null ? numeroRPC : STR_NULO);
		sb.append(" ");
		sb.append(this.lugar != null ? lugar : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(this.numeroRPC) && lugar == null;
	}
	
	public void clear() {
		this.setNumeroRPC("");
		this.setLugar(null);
	}

	private String numeroRPC;

	private CountrySubdivision_AR lugar;

	private static final long serialVersionUID = 1L;
}