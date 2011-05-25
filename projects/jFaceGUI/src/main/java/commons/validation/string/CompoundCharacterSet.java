package commons.validation.string;

import java.util.LinkedList;
import java.util.List;

/**
 * Character set, composed of other charsets.
 */
public class CompoundCharacterSet implements CharacterSet, Cloneable {

	private List<CharacterSet> m_list = new LinkedList<CharacterSet>();

	public CompoundCharacterSet() {
		super();
	}

	public CompoundCharacterSet(CharacterSet set) {
		add(set);
	}

	public CompoundCharacterSet(CharacterSet first, CharacterSet second) {
		m_list.add(first);
		m_list.add(second);
	}

	public Character getReplaceForInvalidCharacter(char c) {
		for (CharacterSet cs : this.m_list) {
			Character r = cs.getReplaceForInvalidCharacter(c);
			if (r != null) {
				return r;
			}
		}
		return null;
	}

	public CompoundCharacterSet(CharacterSet... sets) {

		for (CharacterSet s : sets) {
			m_list.add(s);
		}
	}

	public void add(CharacterSet set) {
		m_list.add(set);
	}

	public void add(String literals) {
		add(new LiteralCharacterSet(literals));
	}

	public boolean isMember(char c) {
		for (int i = 0; i < m_list.size(); i++) {
			if (m_list.get(i).isMember(c)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		final StringBuilder str = new StringBuilder();

		final int n = m_list.size();

		for (int i = 0; i < n; i++) {
			if (i != 0) {
				if (i == n - 1) {
					str.append(" o ");
				} else {
					str.append(", ");
				}
			}
			str.append(m_list.get(i).getName());
		}

		return str.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
