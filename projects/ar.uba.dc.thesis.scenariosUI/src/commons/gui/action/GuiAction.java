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
 * $Id: GuiAction.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.gui.action;

import org.eclipse.jface.action.Action;

/**
 * Interfaz que deben implementar todas las acciones de la aplicaci�n que utilize esta librer�a.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public interface GuiAction<T> {

	/**
	 * Provee el identificador �nico de la acci�n. Com�nmente usado como "key" de Maps.El
	 * conocimiento de la unicidad del identificador lo posee el objeto que crea la GuiAction.
	 */
	public String getIdentificadorUnico();

	/**
	 * Provee la acci�n concreta de SWT, para un modelo espec�fico.
	 */
	public Action getActionFor(T model);

	/**
	 * Provee el atajo de teclado para la acci�n o un String vac�o en caso que dicha acci�n no posea
	 * un atajo de teclado asociado.
	 */
	public String getShortcut();
}
