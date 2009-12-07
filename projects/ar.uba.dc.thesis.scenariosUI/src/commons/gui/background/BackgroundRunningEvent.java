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
 * $Id: BackgroundRunningEvent.java,v 1.3 2008/01/29 13:57:54 cvspasto Exp $
 */

package commons.gui.background;

import java.util.EventObject;

/**
 * Evento asociado a la ejecucion de una tarea background
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/01/29 13:57:54 $
 */

public class BackgroundRunningEvent extends EventObject {

	public BackgroundRunningEvent(Object source, Object... arguments) {
		super(source);
		this.arguments = arguments;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	private Object[] arguments;

	private Object result;

	private static final long serialVersionUID = 1L;
}
