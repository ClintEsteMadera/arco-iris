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
 * $Id: Proposito.java,v 1.2 2007/11/30 20:31:06 cvsmarco Exp $
 */

package commons.gui.util.proposito;

import sba.common.properties.EnumProperty;

/**
 * Interfaz que deben implementar los prop�sitos de uso de un di�logo particulares a cada
 * aplicaci�n.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2007/11/30 20:31:06 $
 */

public interface Proposito {

	/**
	 * Dice si el di�logo tiene prop�sito de Alta o no. 
	 */
	boolean esAlta();
	
	/**
	 * Dice si el di�logo tiene prop�sito de Edici�n o no. 
	 */
	boolean esEdicion();
	
	/**
	 * Dice si los contenidos del di�logo deben ser de s�lo lectura o no.
	 */
	boolean esReadOnly();
	
	/**
	 * Provee el texto que debe ir en el bot�n que abre el di�logo.
	 */
	EnumProperty getTextForLauncherButton();

	/**
	 * Dice si el bot�n que inicia el di�logo debe estar habilitado o no inicialmente.
	 */
	boolean getLauncherButtonInitialEnabledState();
}
