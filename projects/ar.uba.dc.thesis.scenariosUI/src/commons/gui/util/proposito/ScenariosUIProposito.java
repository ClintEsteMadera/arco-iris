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
 * $Id: EmesProposito.java,v 1.18 2008/04/25 12:52:25 cvspasto Exp $
 */

package commons.gui.util.proposito;

import sba.common.properties.EnumProperty;

import commons.properties.CommonLabels;

/**
 * Especifica los distintos prop�sitos de apertura de un di�logo.
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.18 $ $Date: 2008/04/25 12:52:25 $
 */
public enum ScenariosUIProposito implements Proposito {

	ALTA("Alta", false, CommonLabels.ALTA) {
		@Override
		public boolean esAlta() {
			return true;
		}

		@Override
		public boolean getLauncherButtonInitialEnabledState() {
			return true;
		}

		@Override
		public EnumProperty getAcceptButtonText() {
			return null;
		}
	},

	EDICION("Edici�n", false, CommonLabels.EDITAR) {
		@Override
		public boolean esEdicion() {
			return true;
		}

		@Override
		public EnumProperty getAcceptButtonText() {
			return CommonLabels.GUARDAR;
		}
	},

	VER("Ver", true, CommonLabels.VER) {
		@Override
		public EnumProperty getAcceptButtonText() {
			return null;
		}

		@Override
		public EnumProperty getCancelButtonText() {
			return CommonLabels.CERRAR;
		}
	},

	ELIMINACION("Eliminaci�n", true, CommonLabels.ELIMINAR) {
		@Override
		public EnumProperty getAcceptButtonText() {
			return CommonLabels.ELIMINAR;
		}
	};

	private ScenariosUIProposito(String descripcion, boolean readOnly, EnumProperty textForLauncherButton) {
		this.descripcion = descripcion;
		this.readOnly = readOnly;
		this.textForLauncherButton = textForLauncherButton;
	}

	public boolean esReadOnly() {
		return this.readOnly;
	}

	public EnumProperty getTextForLauncherButton() {
		return this.textForLauncherButton;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}

	/**
	 * Por defecto, los d�alogos no son de "Alta"
	 */
	public boolean esAlta() {
		return false;
	}

	/**
	 * Por defecto, los d�alogos no son de "Edici�n"
	 */
	public boolean esEdicion() {
		return false;
	}

	/**
	 * Por defecto, el bot�n que abre el di�logo no est� habilitado inicialmente.
	 */
	public boolean getLauncherButtonInitialEnabledState() {
		return false;
	}

	/**
	 * Por defecto, el texto del bot�n "Aceptar" en un di�logo que tenga este prop�sito, ser� <b>el
	 * mismo</b> que el texto del bot�n utilizado para abrir dicho di�logo.
	 */
	public EnumProperty getAcceptButtonText() {
		return this.textForLauncherButton;
	}

	/**
	 * Por defecto, retorna <code>null</code>, lo que se interpreta como que no se desea
	 * sobreescribir el texto por defecto del bot�n.
	 */
	public EnumProperty getCancelButtonText() {
		return null;
	}

	private String descripcion;

	private boolean readOnly;

	private EnumProperty textForLauncherButton;
}