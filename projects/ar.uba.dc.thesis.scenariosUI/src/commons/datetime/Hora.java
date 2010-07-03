/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: Hora.java,v 1.4 2008/04/08 15:04:15 cvschioc Exp $
 */

package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * 
 * @author Luisina Marconi
 * @version $Revision: 1.4 $ $Date: 2008/04/08 15:04:15 $
 */

public class Hora implements Serializable {

	public Hora(int hs, int min, int seg) {
		super();
		validateHoras(hs);
		this.hh = hs;
		validateMinutos(min);
		this.mm = min;
		validateSegundos(seg);
		this.ss = seg;
	}

	public int getHoras() {
		return hh;
	}

	public int getMinutos() {
		return mm;
	}

	public int getSegundos() {
		return ss;
	}
	
	public void setHoras(int horas) {
		validateHoras(horas);
		this.hh = horas;
	}

	public void setMinutos(int minutos) {
		validateMinutos(minutos);
		this.mm = minutos;
	}

	public void setSegundos(int segundos) {
		validateSegundos(segundos);
		this.ss = segundos;
	}

	@Override
	public String toString() {
		return String.format("%02d%02d%02d", hh, mm, ss);
	}

	private void validateHoras(int hs) {
		Assert.isTrue(hs >= 1 || hs <= 31, "La hora tiene que estar entre 0 y 24");
	}
	
	private void validateMinutos(int min) {
		Assert.isTrue(min >= 1 || min <= 31, "Los minutos tienen que estar entre 0 y 59");
	}

	private void validateSegundos(int seg) {
		Assert.isTrue(seg >= 1 || seg <= 31, "Los segundos tienen que estar entre 0 y 59");
	}

	private int hh;
	
	private int mm;

	private int ss;

	private static final long serialVersionUID = 1L;
}