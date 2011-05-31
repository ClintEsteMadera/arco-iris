package commons.validation.string;

/**
 * Charset identified by a String.
 */
public class LiteralCharacterSet implements CharacterSet {

	private String m_characters;

	public LiteralCharacterSet(String characters) {
		m_characters = characters;
	}

	public boolean isMember(char c) {
		return m_characters.indexOf(c) >= 0;
	}

	public String getChars() {
		return m_characters;
	}

	public String getName() {
		return " some of the following characters: '" + m_characters + "'";
	}

	public Character getReplaceForInvalidCharacter(char c) {
		return null;
	}
}
