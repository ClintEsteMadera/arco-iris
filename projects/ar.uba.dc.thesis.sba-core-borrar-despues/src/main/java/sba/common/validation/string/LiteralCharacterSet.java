package sba.common.validation.string;


/**
 * Set de caracteres identificado mediante un String.
 * 
 * @author P.Pastorino
 */
public class LiteralCharacterSet 
implements CharacterSet {
	
	public LiteralCharacterSet(String characters){
		m_characters=characters;
	}
	
	public boolean isMember(char c){
		return m_characters.indexOf(c) >= 0;
	}
	
	public String getChars(){
		return m_characters;
	}
		
	public String getName() {
		return " alguno de los siguientes caracteres: '" + m_characters + "'";
	}
	
	public Character getReplaceForInvalidCharacter(char c) {
		return null;
	}
	
	private String m_characters;
}

