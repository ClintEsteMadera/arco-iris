/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: Domicilio.java,v 1.10 2008/05/15 19:23:38 cvsmvera Exp $
 */

package sba.common.domicilio;

import org.apache.commons.lang.StringUtils;

import sba.common.annotations.DataTypeAnnotation;
import sba.common.annotations.PropertyDescription;
import sba.common.annotations.ValidateRequired;
import sba.common.core.CopyConstructible;
import sba.common.pais.Country;
import sba.common.pais.CountrySubdivision;
import sba.common.persistence.BasePersistentObject;
import sba.common.validation.CommonDataTypes;

/**
 * Modela un Domicilio.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.10 $ $Date: 2008/05/15 19:23:38 $
 */

public class Domicilio extends BasePersistentObject implements Cloneable,
		CopyConstructible<Domicilio> {

	public Domicilio() {
		super();
	}

	public Domicilio(String calle, String nroCalle, String localidad, String codPostal,
			String piso, String depto, Country pais, CountrySubdivision provincia,
			String observaciones) {
		super();
		this.calle = calle;
		this.nroCalle = nroCalle;
		this.localidad = localidad;
		this.codigoPostal = codPostal;
		this.piso = piso;
		this.depto = depto;
		this.pais = pais;
		this.provincia = provincia;
		this.observaciones = observaciones;
	}

	public Domicilio copyConstructor() {
		return new Domicilio(this.getCalle(), this.getNroCalle(), this.getLocalidad(), this
				.getCodigoPostal(), this.getPiso(), this.getDepto(), this.getPais(), this
				.getProvincia(), this.getObservaciones());
	}

	@PropertyDescription(value="DOMICILIO_CALLE")
    @ValidateRequired
    @DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_CALLE)
	public String getCalle() {
		return this.calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	@PropertyDescription(value="DOMICILIO_CODIGO_POSTAL")
    @ValidateRequired
	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_COD_POSTAL)
	public String getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(String codPostal) {
		this.codigoPostal = codPostal;
	}

	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_DEPTO)
	public String getDepto() {
		return this.depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	@PropertyDescription(value="DOMICILIO_LOCALIDAD")
    @ValidateRequired
	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_LOCALIDAD)
	public String getLocalidad() {
		return this.localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@PropertyDescription(value="DOMICILIO_NUMERO")
    @ValidateRequired
	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_NUMERO)
	public String getNroCalle() {
		return this.nroCalle;
	}

	public void setNroCalle(String nroCalle) {
		this.nroCalle = nroCalle;
	}

	@PropertyDescription(value="DOMICILIO_PAIS")
    @ValidateRequired
	public Country getPais() {
		return this.pais;
	}

	public void setPais(Country pais) {
		this.pais = pais;
	}

	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_PISO)
	public String getPiso() {
		return this.piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	@PropertyDescription(value="DOMICILIO_PROVINCIA")
    @ValidateRequired
	public CountrySubdivision getProvincia() {
		return this.provincia;
	}

	public void setProvincia(CountrySubdivision provincia) {
		this.provincia = provincia;
	}

	@DataTypeAnnotation(typeId = CommonDataTypes.OBSERVACIONES)
	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/** Limpia los valores del domicilio */
	public void clean() {
		setCalle("");
		setCodigoPostal("");
		setDepto("");
		setLocalidad("");
		setNroCalle("");
		setObservaciones("");
		setPais(null);
		setPiso("");
		setProvincia(null);
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof Domicilio) {
			equals = this.equalsTo((Domicilio) obj);
		}
		return equals;
	}

	/**
	 * Retorna el código de hash del objeto.
	 * @return valor de hash code del objeto
	 */
	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.calle == null) ? 0 : this.calle.hashCode());
		result = PRIMO * result + ((this.nroCalle == null) ? 0 : this.nroCalle.hashCode());
		result = PRIMO * result + ((this.piso == null) ? 0 : this.piso.hashCode());
		result = PRIMO * result + ((this.depto == null) ? 0 : this.depto.hashCode());
		result = PRIMO * result + ((this.localidad == null) ? 0 : this.localidad.hashCode());
		result = PRIMO * result + ((this.codigoPostal == null) ? 0 : this.codigoPostal.hashCode());
		result = PRIMO * result + ((this.provincia == null) ? 0 : this.provincia.hashCode());
		result = PRIMO * result + ((this.pais == null) ? 0 : this.pais.hashCode());
		result = PRIMO * result
				+ ((this.observaciones == null) ? 0 : this.observaciones.hashCode());

		return result;
	}

	private boolean equalsTo(Domicilio domicilio) {
		boolean equals = true;
		if (this.calle != null) {
			equals = equals && this.calle.equals(domicilio.calle);
		} else {
			equals = equals && (domicilio.calle == null);
		}

		if (equals && this.nroCalle != null) {
			equals = equals && this.nroCalle.equals(domicilio.nroCalle);
		} else {
			equals = equals && (domicilio.nroCalle == null);
		}

		if (equals && this.piso != null) {
			equals = equals && this.piso.equals(domicilio.piso);
		} else {
			equals = equals && (domicilio.piso == null);
		}

		if (equals && this.depto != null) {
			equals = equals && this.depto.equals(domicilio.depto);
		} else {
			equals = equals && (domicilio.depto == null);
		}

		if (equals && this.localidad != null) {
			equals = equals && this.localidad.equals(domicilio.localidad);
		} else {
			equals = equals && (domicilio.localidad == null);
		}

		if (equals && this.codigoPostal != null) {
			equals = equals && this.codigoPostal.equals(domicilio.codigoPostal);
		} else {
			equals = equals && (domicilio.codigoPostal == null);
		}

		if (equals && this.provincia != null) {
			equals = equals && this.provincia.equals(domicilio.provincia);
		} else {
			equals = equals && (domicilio.provincia == null);
		}

		if (equals && this.pais != null) {
			equals = equals && this.pais.equals(domicilio.pais);
		} else {
			equals = equals && (domicilio.pais == null);
		}

		if (equals && this.observaciones != null) {
			equals = equals && this.observaciones.equals(domicilio.observaciones);
		} else {
			equals = equals && (domicilio.observaciones == null);
		}

		return equals;
	}

	@Override
	public String toString() {
		final String STR_NULO = "";
		final String DASH = "-";
		if (this.calle == null && this.nroCalle == null && this.piso == null && this.depto == null
				&& this.localidad == null && this.codigoPostal == null && this.provincia == null
				& this.pais == null) {
			return STR_NULO;
		}
		StringBuilder sb = new StringBuilder();

		sb.append(this.calle != null ? calle.toString() : "Calle N/D");
		sb.append(" ");
		sb.append(this.nroCalle != null ? nroCalle.toString() : "S/N");
		sb.append(" ");
		sb.append(this.piso != null ? piso.toString() : DASH);
		sb.append(" ");
		sb.append(this.depto != null ? depto.toString() : DASH);
		sb.append(" - ");
		sb.append(this.localidad != null ? localidad.toString() : DASH);
		sb.append(" (");
		sb.append(this.codigoPostal != null ? codigoPostal.toString() : DASH);
		sb.append(") ");
		sb.append(this.provincia != null ? provincia.toString() : DASH);
		sb.append(", ");
		sb.append(this.pais != null ? pais.toString() : DASH);
		sb.append(" Observaciones: ");
		sb.append(this.observaciones != null ? observaciones.toString() : DASH);
		return sb.toString();
	}

	@Override
	public Domicilio clone() throws CloneNotSupportedException {
		return (Domicilio) super.clone();
	}

	/**
	 * Especifica si el Domicilio está vacío o no.
	 * @param considerCountry
	 *            especifica si debe considerarse en el análisis de nulidad el colaborador
	 *            <code>pais</code>.
	 * @param considerCountrySubdivision
	 *            especifica si debe considerarse en el análisis de nulidad el colaborador
	 *            <code>provincia</code>.
	 * @return
	 */
	public boolean isEmpty(boolean considerCountry, boolean considerCountrySubdivision) {
		boolean isEmpty = StringUtils.isEmpty(this.calle) && StringUtils.isEmpty(this.nroCalle)
				&& StringUtils.isEmpty(this.piso) && StringUtils.isEmpty(this.depto)
				&& StringUtils.isEmpty(this.localidad) && StringUtils.isEmpty(this.codigoPostal)
				&& StringUtils.isEmpty(this.observaciones);
		if (considerCountry) {
			isEmpty = isEmpty && this.pais == null;
		}
		if (considerCountrySubdivision) {
			isEmpty = isEmpty && this.provincia == null;
		}
		return isEmpty;
	}

	public boolean isEmpty()
	{
		return isEmpty(true, true);
	}
	
	private static final long serialVersionUID = -1244981715899957281L;

	private String calle;

	private String nroCalle;

	private String piso;

	private String depto;

	private String localidad;

	private String codigoPostal;

	private Country pais;

	private CountrySubdivision provincia;

	private String observaciones;
}