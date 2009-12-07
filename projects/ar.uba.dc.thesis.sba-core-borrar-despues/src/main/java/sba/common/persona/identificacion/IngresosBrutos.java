/*
 * $Id: IngresosBrutos.java,v 1.4 2008/05/15 19:23:47 cvsmvera Exp $
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

import org.apache.commons.lang.StringUtils;

import sba.common.annotations.PropertyDescription;
import sba.common.annotations.ValidateRequired;
import sba.common.pais.CountrySubdivision_AR;
import sba.common.persistence.BasePersistentObject;

/**
 * Modela información referida al impuesto provincial "Ingresos Brutos"
 * 
 * @author Miguel Díaz
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ - $Date: 2008/05/15 19:23:47 $
 */
public class IngresosBrutos extends BasePersistentObject {

	public IngresosBrutos() {
		super();
		this.tipo = TipoIngresosBrutos.NO_ESPECIFICADO;
	}

	public IngresosBrutos(TipoIngresosBrutos tipo, String numero, CountrySubdivision_AR provincia) {
		super();
		this.tipo = tipo;
		this.numero = numero;
		this.provincia = provincia;
	}

	@PropertyDescription(value="TIPO_IB")
    @ValidateRequired
	public TipoIngresosBrutos getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoIngresosBrutos tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public CountrySubdivision_AR getProvincia() {
		return provincia;
	}

	public void setProvincia(CountrySubdivision_AR provincia) {
		this.provincia = provincia;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof IngresosBrutos) {
			equals = this.equalsTo((IngresosBrutos) obj);
		}
		return equals;
	}

	private boolean equalsTo(IngresosBrutos ingresosBrutos) {
		boolean equals = true;
		if (this.tipo != null) {
			equals = equals && this.tipo.equals(ingresosBrutos.tipo);
		} else {
			equals = equals && (ingresosBrutos.tipo == null);
		}
		if (this.numero != null) {
			equals = equals && this.numero.equals(ingresosBrutos.numero);
		} else {
			equals = equals && (ingresosBrutos.numero == null);
		}
		if (this.provincia != null) {
			equals = equals && this.provincia.equals(ingresosBrutos.provincia);
		} else {
			equals = equals && (ingresosBrutos.provincia == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.tipo == null) ? 0 : this.tipo.hashCode());
		result = PRIMO * result + ((this.numero == null) ? 0 : this.numero.hashCode());
		result = PRIMO * result + ((this.provincia == null) ? 0 : this.provincia.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.tipo != null ? tipo.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.numero != null ? numero.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.provincia != null ? provincia.toString() : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	public boolean isEmpty() {
		return (this.tipo == null || this.tipo.equals(TipoIngresosBrutos.NO_ESPECIFICADO))
				&& StringUtils.isEmpty(this.numero) && this.provincia == null;
	}

	private TipoIngresosBrutos tipo;

	private String numero;

	private CountrySubdivision_AR provincia;

	private static final long serialVersionUID = 1L;
}