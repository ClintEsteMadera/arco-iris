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
 * $Id: Proposito.java,v 1.2 2007/11/30 20:31:06 cvsmarco Exp $
 */

package commons.gui.util.proposito;

import sba.common.properties.EnumProperty;

/**
 * Interfaz que deben implementar los propósitos de uso de un diálogo particulares a cada
 * aplicación.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2007/11/30 20:31:06 $
 */

public interface Proposito {

	/**
	 * Dice si el diálogo tiene propósito de Alta o no. 
	 */
	boolean esAlta();
	
	/**
	 * Dice si el diálogo tiene propósito de Edición o no. 
	 */
	boolean esEdicion();
	
	/**
	 * Dice si los contenidos del diálogo deben ser de sólo lectura o no.
	 */
	boolean esReadOnly();
	
	/**
	 * Provee el texto que debe ir en el botón que abre el diálogo.
	 */
	EnumProperty getTextForLauncherButton();

	/**
	 * Dice si el botón que inicia el diálogo debe estar habilitado o no inicialmente.
	 */
	boolean getLauncherButtonInitialEnabledState();
}
