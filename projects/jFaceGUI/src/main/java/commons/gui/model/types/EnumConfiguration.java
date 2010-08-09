package commons.gui.model.types;

import java.text.Format;
import java.util.Properties;

/**
 * Configuracion para edicion y rendering de enums.
 * 
 * @author P.Pastorino
 */
public class EnumConfiguration implements EditConfiguration {

	public EnumConfiguration(Class enumClass, Properties map) {
		this.enumClass = enumClass;

		this.instances = this.enumClass.getEnumConstants();

		if (this.instances == null) {
			throw new IllegalArgumentException("La clase '" + enumClass.getName() + "' no es de tipo enum");
		}
		this.texts = new String[this.instances.length];

		for (int i = 0; i < this.instances.length; i++) {
			final String key = this.instances[i].toString();
			this.texts[i] = map.getProperty(key, key);

			if (this.prototypeIndex < 0 || this.texts[i].length() > this.texts[this.prototypeIndex].length()) {
				this.prototypeIndex = i;
			}
		}
	}

	/**
	 * @see sba.ui.edit.types.EditConfiguration#getFormat()
	 */
	public Format getFormat() {
		if (this.format == null) {
			format = new EnumFormat();
		}
		return format;
	}

	/**
	 * @see sba.ui.edit.types.EditConfiguration#getPrototype()
	 */
	public Object getPrototype() {
		return this.prototypeIndex >= 0 ? this.instances[this.prototypeIndex] : null;
	}

	public Object getColumnPrototype() {
		return this.getPrototype();
	}

	public boolean isRightAligned() {
		return false;
	}

	public Object[] getInstances() {
		return this.instances;
	}

	public String[] getTexts() {
		return texts;
	}

	private Class enumClass;

	private Object[] instances;

	private String[] texts;

	private EnumFormat format;

	private int prototypeIndex = -1;

	private class EnumFormat extends AbstractFormat {
		@Override
		public String valueToString(Object obj) {

			for (int i = 0; i < EnumConfiguration.this.instances.length; i++) {
				if (EnumConfiguration.this.instances[i].equals(obj)) {
					return EnumConfiguration.this.texts[i];
				}
			}
			return obj == null ? "" : obj.toString();
		}

		@Override
		public Object stringToValue(String str) {
			for (int i = 0; i < EnumConfiguration.this.texts.length; i++) {
				if (EnumConfiguration.this.texts[i].equals(str)) {
					return EnumConfiguration.this.instances[i];
				}
			}
			return null;
		}

		private static final long serialVersionUID = 1L;
	};
}
