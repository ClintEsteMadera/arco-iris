package commons.validation.string;

/**
 * Conjunto de caracteres validos en un String.
 */
public interface CharacterSet {

	public boolean isMember(char c);

	public String getName();

	public Character getReplaceForInvalidCharacter(char c);

	public static final CharacterSet DIGIT = new CharacterSet() {
		public boolean isMember(char c) {
			return Character.isDigit(c);
		}

		public String getName() {
			return "d�gitos";
		}

		public Character getReplaceForInvalidCharacter(char c) {
			return null;
		}
	};

	public static final CharacterSet LETTER = new CharacterSet() {
		public boolean isMember(char c) {
			return Character.isLetter(c);
		}

		public String getName() {
			return "letras";
		}

		public Character getReplaceForInvalidCharacter(char c) {
			return null;
		}
	};

	public static final CharacterSet ALPHA_NUM = new CompoundCharacterSet(DIGIT, LETTER);

	public static final CharacterSet WHITE_SPACE = new CharacterSet() {
		public boolean isMember(char c) {
			return Character.isSpaceChar(c);
		}

		public String getName() {
			return "espacios";
		}

		public Character getReplaceForInvalidCharacter(char c) {
			return null;
		}
	};

	public static final CharacterSet ALL = new CharacterSet() {
		public boolean isMember(char c) {
			return true;
		}

		public String getName() {
			return "cualquier caracter";
		}

		public Character getReplaceForInvalidCharacter(char c) {
			return null;
		}
	};

	public static final CharacterSet EMPTY = new CharacterSet() {
		public boolean isMember(char c) {
			return false;
		}

		public String getName() {
			return "ning�n caracter";
		}

		public Character getReplaceForInvalidCharacter(char c) {
			return null;
		}

	};

	public static final CharacterSet BCD =

	new CompoundCharacterSet(DIGIT, new LiteralCharacterSet(" #@:>?&.](<\\^-$*);'`+/_,%=\"!%"),
			new LiteralCharacterSet("ABCDEFGHIJKMNLOPQRSTUVWXYZ")) {
		@Override
		public String getName() {
			return "caracteres v�lidos para NOVASCALE";
		}

		@Override
		public Character getReplaceForInvalidCharacter(char c) {
			for (char[] r : replace_table) {
				if (c == r[0]) {
					return new Character(r[1]);
				}
			}
			return new Character(' ');
		}

		private final char[][] replace_table = new char[][] { new char[] { '�', 'a' }, new char[] { '�', 'e' },
				new char[] { '�', 'i' }, new char[] { '�', 'o' }, new char[] { '�', 'u' }, new char[] { '�', 'A' },
				new char[] { '�', 'E' }, new char[] { '�', 'I' }, new char[] { '�', 'O' }, new char[] { '�', 'U' },
				new char[] { '�', 'n' }, new char[] { '�', 'N' }, };

	};

}
