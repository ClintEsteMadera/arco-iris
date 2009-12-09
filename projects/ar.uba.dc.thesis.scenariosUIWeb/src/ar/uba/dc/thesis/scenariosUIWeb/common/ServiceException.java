/*
 * $Id: ServiceException.java,v 1.3 2009/04/27 18:02:09 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common;

/**
 * @author Rodrigo A. Peinado
 * @version $Revision: 1.3 $ $Date: 2009/04/27 18:02:09 $
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String s) {
		super(s);

	}

	public ServiceException(Throwable throwable) {
		super(throwable);

	}

	public ServiceException(String s, Throwable throwable) {
		super(s, throwable);

	}
}
