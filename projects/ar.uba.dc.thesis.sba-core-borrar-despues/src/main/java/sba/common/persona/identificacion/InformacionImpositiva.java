/*
 * $Id: InformacionImpositiva.java,v 1.10 2008/05/15 19:23:47 cvsmvera Exp $
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
import sba.common.annotations.ValidateComponent;
import sba.common.annotations.ValidateRequired;
import sba.common.annotations.ValidationCondition;
import sba.common.persistence.BasePersistentObject;

/**
 * Modela la información impositiva (normalmente relacionada a una Persona (Física o Jurídica)
 * 
 * @author Miguel Díaz
 * @author Jonathan Chiocchio
 * @version $Revision: 1.10 $ - $Date: 2008/05/15 19:23:47 $
 */
public class InformacionImpositiva extends BasePersistentObject {

	public InformacionImpositiva() {
		super();
		this.ingresosBrutos = new IngresosBrutos();
		this.identificadorImpositivo = new IdentificadorImpositivo();
	}

	public InformacionImpositiva(CondicionAnteIVA condicionAnteIVA, IngresosBrutos ingresosBrutos,
			IdentificadorImpositivo identificadorImpositivo) {
		super();
		this.condicionAnteIVA = condicionAnteIVA;
		this.ingresosBrutos = ingresosBrutos;
		this.identificadorImpositivo = identificadorImpositivo;
	}

	@PropertyDescription(value="CONDICIONES_ANTE_IVA")
    @ValidateRequired
	public CondicionAnteIVA getCondicionAnteIVA() {
		return condicionAnteIVA;
	}

	public void setCondicionAnteIVA(CondicionAnteIVA condicionAnteIVA) {
		this.condicionAnteIVA = condicionAnteIVA;
	}

	@PropertyDescription(value="IDENTIFICADOR_IMPOSITIVO")
	@ValidateComponent(condition=ValidationCondition.IF_NOT_EMPTY )
	public IdentificadorImpositivo getIdentificadorImpositivo() {
		if (this.identificadorImpositivo == null) {
			this.identificadorImpositivo = new IdentificadorImpositivo();
		}
		return identificadorImpositivo;
	}

	public void setIdentificadorImpositivo(IdentificadorImpositivo identificadorImpositivo) {
		this.identificadorImpositivo = identificadorImpositivo;
	}

	@PropertyDescription(value="INGRESOS_BRUTOS")
	@ValidateComponent(condition=ValidationCondition.IF_NOT_EMPTY )
	public IngresosBrutos getIngresosBrutos() {
		if (this.ingresosBrutos == null) {
			this.ingresosBrutos = new IngresosBrutos();
		}
		return ingresosBrutos;
	}

	public void setIngresosBrutos(IngresosBrutos ingresosBrutos) {
		this.ingresosBrutos = ingresosBrutos;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof InformacionImpositiva) {
			equals = this.equalsTo((InformacionImpositiva) obj);
		}
		return equals;
	}

	private boolean equalsTo(InformacionImpositiva informacionImpositiva) {
		boolean equals = true;
		if (this.condicionAnteIVA != null) {
			equals = equals && this.condicionAnteIVA.equals(informacionImpositiva.condicionAnteIVA);
		} else {
			equals = equals && (informacionImpositiva.condicionAnteIVA == null);
		}
		if (this.identificadorImpositivo != null) {
			equals = equals
					&& this.identificadorImpositivo
							.equals(informacionImpositiva.identificadorImpositivo);
		} else {
			equals = equals && (informacionImpositiva.identificadorImpositivo == null);
		}
		if (this.ingresosBrutos != null) {
			equals = equals && this.ingresosBrutos.equals(informacionImpositiva.ingresosBrutos);
		} else {
			equals = equals && (informacionImpositiva.ingresosBrutos == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result
				+ ((this.condicionAnteIVA == null) ? 0 : this.condicionAnteIVA.hashCode());
		result = PRIMO
				* result
				+ ((this.identificadorImpositivo == null) ? 0 : this.identificadorImpositivo
						.hashCode());
		result = PRIMO * result
				+ ((this.ingresosBrutos == null) ? 0 : this.ingresosBrutos.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.condicionAnteIVA != null ? condicionAnteIVA.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.identificadorImpositivo != null
				? identificadorImpositivo.toString()
				: STR_NULO);
		sb.append(" ");
		sb.append(this.ingresosBrutos != null ? ingresosBrutos.toString() : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	public boolean isEmpty(boolean ignoreTipoIdentificadorImpositivo) {
		return this.condicionAnteIVA == null && this.getIngresosBrutos().isEmpty()
				&& this.getIdentificadorImpositivo().isEmpty(ignoreTipoIdentificadorImpositivo);
	}

	public boolean isEmpty()
	{
		return isEmpty(true);
	}
	
	private CondicionAnteIVA condicionAnteIVA;

	private IngresosBrutos ingresosBrutos;

	private IdentificadorImpositivo identificadorImpositivo;

	private static final long serialVersionUID = 1L;
}