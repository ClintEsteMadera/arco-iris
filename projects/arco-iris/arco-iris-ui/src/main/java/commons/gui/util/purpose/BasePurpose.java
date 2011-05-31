package commons.gui.util.purpose;

import commons.properties.CommonLabels;
import commons.properties.EnumProperty;

/**
 * Specifies the different kinds of purposes when opening a dialog
 */
public enum BasePurpose implements Purpose {

	CREATION("Create", false, CommonLabels.CREATION) {
		@Override
		public boolean isCreation() {
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

	EDIT("Edit", false, CommonLabels.EDIT) {
		@Override
		public boolean isEdition() {
			return true;
		}

		@Override
		public EnumProperty getAcceptButtonText() {
			return CommonLabels.SAVE;
		}
	},

	VIEW("View", true, CommonLabels.VIEW) {
		@Override
		public boolean isView() {
			return true;
		}

		@Override
		public EnumProperty getAcceptButtonText() {
			return null;
		}

		@Override
		public EnumProperty getCancelButtonText() {
			return CommonLabels.CLOSE;
		}
	},

	DELETE("Delete", true, CommonLabels.DELETE) {
		@Override
		public boolean isDeletion() {
			return true;
		}

		@Override
		public EnumProperty getAcceptButtonText() {
			return CommonLabels.DELETE;
		}
	};

	private BasePurpose(String descripcion, boolean readOnly, EnumProperty textForLauncherButton) {
		this.description = descripcion;
		this.readOnly = readOnly;
		this.textForLauncherButton = textForLauncherButton;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public EnumProperty getTextForLauncherButton() {
		return this.textForLauncherButton;
	}

	@Override
	public String toString() {
		return this.description;
	}

	/**
	 * Por defecto, los d�alogos no son de "Alta"
	 */
	public boolean isCreation() {
		return false;
	}

	/**
	 * Por defecto, los d�alogos no son de "Edici�n"
	 */
	public boolean isEdition() {
		return false;
	}

	/**
	 * Por defecto, los d�alogos no son de "View"
	 */
	public boolean isView() {
		return false;
	}

	/**
	 * Por defecto, los d�alogos no son de "Deletion"
	 */
	public boolean isDeletion() {
		return false;
	}

	/**
	 * Por defecto, el bot�n que abre el di�logo no est� habilitado inicialmente.
	 */
	public boolean getLauncherButtonInitialEnabledState() {
		return false;
	}

	/**
	 * Por defecto, el texto del bot�n "Aceptar" en un di�logo que tenga este prop�sito, ser� <b>el mismo</b> que el
	 * texto del bot�n utilizado para abrir dicho di�logo.
	 */
	public EnumProperty getAcceptButtonText() {
		return this.textForLauncherButton;
	}

	/**
	 * Por defecto, retorna <code>null</code>, lo que se interpreta como que no se desea sobreescribir el texto por
	 * defecto del bot�n.
	 */
	public EnumProperty getCancelButtonText() {
		return null;
	}

	private String description;

	private boolean readOnly;

	private EnumProperty textForLauncherButton;
}