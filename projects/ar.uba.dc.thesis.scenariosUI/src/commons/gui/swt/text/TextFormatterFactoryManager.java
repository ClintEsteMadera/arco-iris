package commons.gui.swt.text;

import java.util.ArrayList;

import commons.gui.model.types.EditType;

public class TextFormatterFactoryManager {

	public static TextFormatterFactoryManager getInstance() {
		return instance;
	}

	public void addFactory(TextFormatterFactory factory) {
		factories.add(factory);
	}

	public TextFormatterFactory getFactory(EditType eType) {
		for (TextFormatterFactory f : this.factories) {
			if (f.supports(eType)) {
				return f;
			}
		}
		return null;
	}

	private TextFormatterFactoryManager() {
		factories = new ArrayList<TextFormatterFactory>();

		addFactory(new NumberFormatterFactory());
		addFactory(new StringFormatterFactory());
	}

	private ArrayList<TextFormatterFactory> factories;

	private static TextFormatterFactoryManager instance = new TextFormatterFactoryManager();
}
