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
 * $Id: EmesProposito.java,v 1.18 2008/04/25 12:52:25 cvspasto Exp $
 */

package commons.gui.util.proposito;

import sba.common.properties.EnumProperty;

import commons.properties.CommonLabels;

/**
 * Especifica los distintos propósitos de apertura de un diálogo.
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

	EDICION("Edición", false, CommonLabels.EDITAR) {
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

	ELIMINACION("Eliminación", true, CommonLabels.ELIMINAR) {
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
	 * Por defecto, los díalogos no son de "Alta"
	 */
	public boolean esAlta() {
		return false;
	}

	/**
	 * Por defecto, los díalogos no son de "Edición"
	 */
	public boolean esEdicion() {
		return false;
	}

	/**
	 * Por defecto, el botón que abre el diálogo no está habilitado inicialmente.
	 */
	public boolean getLauncherButtonInitialEnabledState() {
		return false;
	}

	/**
	 * Por defecto, el texto del botón "Aceptar" en un diálogo que tenga este propósito, será <b>el
	 * mismo</b> que el texto del botón utilizado para abrir dicho diálogo.
	 */
	public EnumProperty getAcceptButtonText() {
		return this.textForLauncherButton;
	}

	/**
	 * Por defecto, retorna <code>null</code>, lo que se interpreta como que no se desea
	 * sobreescribir el texto por defecto del botón.
	 */
	public EnumProperty getCancelButtonText() {
		return null;
	}

	private String descripcion;

	private boolean readOnly;

	private EnumProperty textForLauncherButton;
}