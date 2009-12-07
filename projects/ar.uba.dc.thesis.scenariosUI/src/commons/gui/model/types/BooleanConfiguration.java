package commons.gui.model.types;

import java.text.Format;

import sba.common.utils.ConversionUtilities;


/**
 * Información para configurar tipos booleanos.
 * @author P.Pastorino
 */
public class BooleanConfiguration implements EditConfiguration {

	/**
	 * Obtiene el formato. Se utilizan las constantes <code>"Si"</code> y <code>"No"</code> para
	 * representar los valores <code>true</code> y <code>false</code> respectivamente.
	 */
	public Format getFormat() {
		return s_format;
	}

	/**
	 * Obtiene el prototipo.
	 * @return <code>Boolean.FALSE</code>
	 */
	public Object getPrototype() {
		return Boolean.FALSE;
	}

	/**
	 * Obtiene el prototipo.
	 * @return <code>Boolean.FALSE</code>
	 */
	public Object getColumnPrototype() {
		return this.getPrototype();
	}

	/**
	 * Obtiene la alineación.
	 * @return <code>false</code>
	 */
	public boolean isRightAligned() {
		return false;
	}

	private static class BooleanFormat extends AbstractFormat {
		@Override
		public String valueToString(Object obj) {
			return ((Boolean) obj).booleanValue() ? "Si" : "No";
		}

		@Override
		public Object stringToValue(String str) {
			return ConversionUtilities.toBoolean(str);
		}
		private static final long serialVersionUID = 1L;
	};

	private static final BooleanFormat s_format = new BooleanFormat();
}
