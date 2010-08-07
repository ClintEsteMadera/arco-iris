package commons.gui.model.types;

import commons.validation.StringConstraints;
import commons.validation.string.CharacterSet;
import commons.validation.string.LiteralCharacterSet;

/**
 * Parametros par la edicion de Strings.
 * 
 * @author P.Pastorino
 */
public class StringEditParameters implements Cloneable {

	/**
	 * Set de caracteres validos
	 */
	public CharacterSet validCharacters = CharacterSet.ALL;

	/**
	 * Set de caracteres invalidos
	 */
	public CharacterSet invalidCharacters = CharacterSet.EMPTY;

	/**
	 * Flag para indicar que los caracteres ingresados por el usuario se transformen a mayusculas.
	 */
	public boolean alwaysUpperCase = false;

	/**
	 * Mascara
	 */
	public String mask;

	/**
	 * Longitud máxima (un valor menor que 0 indica longitud ilimitada)
	 */
	public int maxLength = -1;

	/**
	 * Longitud del prototipo
	 */
	public int prototypeLength = -1;

	/**
	 * Longitud del prototipo para columna
	 */
	public int columnPrototypeLength = -1;

	/**
	 * Flag para indicar que el usuario no puede ingresar un string vacio (si ingresa un string vacio el valor se
	 * transorma en <code>null</code>)
	 */
	public boolean allowEmpty = false;

	/**
	 * Flag para indicar que se debe hacer un trim del string
	 */
	public boolean trim = true;

	public StringEditParameters() {
	}

	public StringEditParameters(StringConstraints constraints) {
		this.maxLength = constraints.getMaxLength();
		this.validCharacters = constraints.getValidChars();
		this.invalidCharacters = constraints.getInvalidChars();
		this.alwaysUpperCase = constraints.isAlwaysUppercase();
	}

	public StringEditParameters(int maxLength, String validCharacters, String invalidCharacters, boolean alwaysUpperCase) {
		if (validCharacters != null) {
			this.validCharacters = new LiteralCharacterSet(validCharacters);
		}
		if (invalidCharacters != null) {
			this.invalidCharacters = new LiteralCharacterSet(invalidCharacters);
		}
		this.alwaysUpperCase = alwaysUpperCase;
		this.maxLength = maxLength;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e.getClass().getName());
		}
	}

	private String getPrototype(int l) {
		final int length = mask != null ? mask.length() : l > 0 ? l : maxLength;

		if (length > 0) {
			StringBuffer sb = new StringBuffer(length);
			for (int i = 0; i < length; i++) {
				sb.append("O");
			}
			return sb.toString();
		}
		return null;

	}

	public String getPrototype() {
		return getPrototype(this.prototypeLength);
	}

	public String getColumnPrototype() {
		return getPrototype(this.columnPrototypeLength > 0 ? this.columnPrototypeLength : this.prototypeLength);
	}

	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(" [").append("maxLength=").append(maxLength).append("]")
				.toString();
	}
}
