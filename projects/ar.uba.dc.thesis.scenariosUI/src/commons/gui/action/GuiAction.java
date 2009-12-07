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
 * $Id: GuiAction.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.gui.action;

import org.eclipse.jface.action.Action;

/**
 * Interfaz que deben implementar todas las acciones de la aplicación que utilize esta librería.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public interface GuiAction<T> {

	/**
	 * Provee el identificador único de la acción. Comúnmente usado como "key" de Maps.El
	 * conocimiento de la unicidad del identificador lo posee el objeto que crea la GuiAction.
	 */
	public String getIdentificadorUnico();

	/**
	 * Provee la acción concreta de SWT, para un modelo específico.
	 */
	public Action getActionFor(T model);

	/**
	 * Provee el atajo de teclado para la acción o un String vacío en caso que dicha acción no posea
	 * un atajo de teclado asociado.
	 */
	public String getShortcut();
}
