/*
 * $Id: Agente.java,v 1.21 2009/06/30 21:29:35 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package sba.merval.garantias.agentes.domain;

import java.util.Calendar;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Range;

/**
 * @author ecalabrese
 * @version $Revision: 1.21 $ $Date: 2009/06/30 21:29:35 $
 */
public class Agente {

	public Agente() {
		super();
	}

	public Agente(String denominacion, int cantidadAcciones) {
		super();
		this.denominacion = denominacion;
		this.cantidadAcciones = cantidadAcciones;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public int getCantidadAcciones() {
		return cantidadAcciones;
	}

	public void setCantidadAcciones(int cantidadAcciones) {
		this.cantidadAcciones = cantidadAcciones;
	}

	public Integer getSubcuentaFondoCVSA() {
		return subcuentaFondoCVSA;
	}

	public void setSubcuentaFondoCVSA(Integer subcuentaFondoCVSA) {
		this.subcuentaFondoCVSA = subcuentaFondoCVSA;
	}

	public Integer getCuentaBancoValores() {
		return cuentaBancoValores;
	}

	public void setCuentaBancoValores(Integer cuentaBancoValores) {
		this.cuentaBancoValores = cuentaBancoValores;
	}

	public Integer getCuentaLiquidacionMVBA() {
		return cuentaLiquidacionMVBA;
	}

	public void setCuentaLiquidacionMVBA(Integer cuentaLiquidacionMVBA) {
		this.cuentaLiquidacionMVBA = cuentaLiquidacionMVBA;
	}

	public Integer getCuentaGarantiaCVSA() {
		return cuentaGarantiaCVSA;
	}

	public void setCuentaGarantiaCVSA(Integer cuentaGarantiaCVSA) {
		this.cuentaGarantiaCVSA = cuentaGarantiaCVSA;
	}

	public Integer getCuentaGarantiaFiduciaria() {
		return cuentaGarantiaFiduciaria;
	}

	public void setCuentaGarantiaFiduciaria(Integer cuentaGarantiaFiduciaria) {
		this.cuentaGarantiaFiduciaria = cuentaGarantiaFiduciaria;
	}

	public Calendar getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Calendar fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotNull
	@Range(min = 1, max = 250)
	private Integer codigo;

	@NotNull
	@Length(max = 255)
	private String denominacion;

	@NotNull
	@Range(min = 0, max = 250)
	private int cantidadAcciones;

	private Calendar fechaAlta;

	@Length(max = 255)
	private String nombre;
	@Length(max = 255)
	private String telefono;
	@Length(max = 255)
	private String fax;

	@Length(max = 255)
	@Email
	private String email;

	private Integer subcuentaFondoCVSA;
	private Integer cuentaBancoValores;
	private Integer cuentaLiquidacionMVBA;
	private Integer cuentaGarantiaCVSA;
	private Integer cuentaGarantiaFiduciaria;
	private boolean habilitado = true;

	private static final long serialVersionUID = 1L;

}
