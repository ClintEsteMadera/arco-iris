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
 * $Id: Fecha.java,v 1.4 2008/04/08 15:04:15 cvschioc Exp $
 */

package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * 
 * @author Luisina Marconi
 * @version $Revision: 1.4 $ $Date: 2008/04/08 15:04:15 $
 */

public class Fecha implements Serializable {

	// TODO Mejorar las validaciones (se puede usar el Enum "Mes")
	public Fecha(int dia, int mes, int año) {
		super();
		validateDia(dia);
		this.dd = dia;
		validateMes(mes);
		this.mm = mes;
		validateAño(año);
		this.aaaa = año;
	}

	public int getDia() {
		return dd;
	}

	public int getMes() {
		return mm;
	}

	public int getAño() {
		return aaaa;
	}
	
	public void setDia(int dia) {
		validateDia(dia);
		this.dd = dia;
	}

	public void setMes(int mes) {
		validateMes(mes);
		this.mm = mes;
	}

	public void setAño(int año) {
		validateAño(año);
		this.aaaa = año;
	}
	
	@Override
	public String toString() {
		return String.format("%04d%02d%02d", aaaa, mm, dd);
	}
	
	private void validateDia(int dia) {
		Assert.isTrue(dia >= 1 || dia <= 31, "El día tiene que estar entre 1 y 31");
	}
	
	private void validateMes(int mes) {
		Assert.isTrue(mes >= 1 || mes <= 12, "El mes tiene que estar entre 1 y 12");
	}
	
	private void validateAño(int año) {
		Assert.isTrue(año >= 1 || año <= 9999, "El año tiene que estar entre 1 y 9999");
	}
	
	private int dd;

	private int mm;

	private int aaaa;

	private static final long serialVersionUID = 1L;
}