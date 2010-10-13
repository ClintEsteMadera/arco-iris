package commons.gui.model.types;

import java.text.Format;

import commons.properties.CommonLabels;
import commons.utils.ConversionUtilities;

/**
 * Información para configurar tipos booleanos.
 */
public class BooleanConfiguration implements EditConfiguration {
	private static final BooleanFormat FORMAT = new BooleanFormat();

	/**
	 * Obtiene el formato. Se utilizan las constantes <code>YES</code> y <code>NO</code> para representar los valores
	 * <code>true</code> y <code>false</code> respectivamente.
	 */
	public Format getFormat() {
		return FORMAT;
	}

	/**
	 * Obtiene el prototipo.
	 * 
	 * @return <code>Boolean.FALSE</code>
	 */
	public Object getPrototype() {
		return Boolean.FALSE;
	}

	/**
	 * Obtiene el prototipo.
	 * 
	 * @return <code>Boolean.FALSE</code>
	 */
	public Object getColumnPrototype() {
		return this.getPrototype();
	}

	/**
	 * Obtiene la alineación.
	 * 
	 * @return <code>false</code>
	 */
	public boolean isRightAligned() {
		return false;
	}

	private static class BooleanFormat extends AbstractFormat {
		private static final long serialVersionUID = 1L;

		@Override
		public String valueToString(Object obj) {
			return ((Boolean) obj).booleanValue() ? CommonLabels.YES.toString() : CommonLabels.NO.toString();
		}

		@Override
		public Object stringToValue(String str) {
			return ConversionUtilities.toBoolean(str);
		}
	};
}
