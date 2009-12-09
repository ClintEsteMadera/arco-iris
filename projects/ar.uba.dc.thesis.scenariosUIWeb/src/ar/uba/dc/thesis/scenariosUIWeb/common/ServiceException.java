/*
 * $Id: ServiceException.java,v 1.3 2009/04/27 18:02:09 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
