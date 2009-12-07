package sba.common.domicilio;

import org.apache.commons.lang.StringUtils;

import sba.common.annotations.DataTypeAnnotation;
import sba.common.pais.Country;
import sba.common.persistence.BasePersistentObject;
import sba.common.validation.CommonDataTypes;

public class DomicilioSimple extends BasePersistentObject {

	public DomicilioSimple() {
		super();
	}

	public DomicilioSimple(String domicilio, String codigoPostal, Country pais) {
		super();
		this.domicilio = domicilio;
		this.codigoPostal = codigoPostal;
		this.pais = pais;
	}

	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_COD_POSTAL)
	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	@DataTypeAnnotation(typeId = CommonDataTypes.DOMICILIO_SIMPLE)
	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Country getPais() {
		return pais;
	}

	public void setPais(Country pais) {
		this.pais = pais;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(this.domicilio) && StringUtils.isEmpty(this.codigoPostal)
				&& this.pais == null;
	}
	
	private String domicilio;

	private String codigoPostal;

	private Country pais;

	private static final long serialVersionUID = 1L;
}