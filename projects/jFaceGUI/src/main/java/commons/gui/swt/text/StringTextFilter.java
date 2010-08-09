package commons.gui.swt.text;

import commons.gui.model.types.StringConfiguration;

public class StringTextFilter extends DefaultTextFilter {

	public StringTextFilter(StringConfiguration cfg) {
		this.setTextLimit(cfg.getParameters().maxLength);
		setStringConfiguration(cfg);
	}

	@Override
	public void insert(TextFilterContext context, int index, String text) {
		doInsert(context, index, text);
	}

	@Override
	public void replace(TextFilterContext context, int index, int length, String text) {
		// backup de los valores previos
		String currentText = context.getText();
		int currentSelStart = context.getSelectionStart();
		int currentSelLength = context.getSelectionLength();

		context.remove(index, length);

		if (insertValidText(context, index, text) == 0) {
			context.clear();
			context.insert(0, currentText);
			context.setSelectionStart(currentSelStart);
			context.setSelectionLength(currentSelLength);
		}
	}

	@Override
	public void handleChange(TextFilterContext context) {
		// backup de los valores previos
		String currentText = context.getText();
		int currentSelStart = context.getSelectionStart();
		int currentSelLength = context.getSelectionLength();

		context.remove(0, currentText.length());
		insertValidText(context, 0, currentText);

		// restauro la seleccion original
		context.setSelectionStart(currentSelStart);
		context.setSelectionLength(currentSelLength);
	}

	private void setStringConfiguration(StringConfiguration cfg) {
		this.config = cfg;
	}

	public StringConfiguration getStringConfiguration() {
		return this.config;
	}

	protected int doInsert(TextFilterContext context, int index, String text) {
		return insertValidText(context, index, text);
	}

	// / <summary>
	// / Valida e inserta el texto.
	// / </summary>
	// / <param name="context"></param>
	// / <param name="index"></param>
	// / <param name="text"></param>
	protected int insertValidText(TextFilterContext context, int index, String text) {
		int i = 0;
		int pos = index;
		int count = 0;

		text = checkMaxLength(context, index, text);

		while (i < text.length()) {
			char c = text.charAt(i);
			boolean valid = true;

			if (config.getParameters().alwaysUpperCase) {
				c = Character.toUpperCase(c);
			}

			if (config.getParameters().validCharacters != null && !config.getParameters().validCharacters.isMember(c)) {
				valid = false;
			}

			if (valid && config.getParameters().invalidCharacters != null
					&& config.getParameters().invalidCharacters.isMember(c)) {
				valid = false;
			}

			if (!valid && config.getParameters().validCharacters != null) {
				final Character r = config.getParameters().validCharacters.getReplaceForInvalidCharacter(c);
				if (r != null) {
					c = r.charValue();
				}
				valid = true;
			}

			if (valid) {
				super.insert(context, pos, new String(new char[] { c }));
				count++;
				pos++;
			}

			i++;
		}
		return count;
	}

	// / <summary>
	// / Reduce el tamaño del string en caso de que se supere la
	// / maxima cantidad de digitos
	// / </summary>
	// / <param name="context"></param>
	// / <param name="index"></param>
	// / <param name="text"></param>
	// / <returns></returns>
	private String checkMaxLength(TextFilterContext context, int index, String text) {
		return text;
	}

	private StringConfiguration config;
}
