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
 * $Id: BackgroundFinishedEvent.java,v 1.1 2008/01/16 18:56:32 cvspasto Exp $
 */

package commons.gui.background;

import java.util.EventObject;

/**
 * Evento asociado a la finalizacion de una tarea background
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/01/16 18:56:32 $
 */

public class BackgroundFinishedEvent extends EventObject {

	BackgroundFinishedEvent(Object source, Object result, Throwable error) {
		super(source);
		this.result = result;
		this.error = error;
	}

	public Throwable getError() {
		return error;
	}

	public Object getResult() {
		if(this.error != null){
			throw new IllegalStateException("La tarea termino con error",this.error);
		}
		return result;
	}

	public boolean isOk() {
		return error == null;
	}

	private Object result;

	private Throwable error;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
