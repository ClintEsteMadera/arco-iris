package commons.gui.swt.text;

import commons.gui.model.types.NumberConfiguration;
import commons.gui.model.types.NumberEditParameters;

public class NumericTextFilter extends DefaultTextFilter {
	public NumericTextFilter(NumberConfiguration cfg) {
		setNumberConfiguration(cfg);
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

		if (text.equals(this.alternativeSeparator)) {
			insertDecimalSeparator(context, index, this.separator);
			return;
		}

		if (text.equals(this.separator)) {
			insertDecimalSeparator(context, index, text);
			return;
		}

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

	private void setNumberConfiguration(NumberConfiguration cfg) {
		this.numConfig = cfg;

		if (NumberEditParameters.DEFAULT_DECIMAL_SEPARATOR_COMMA) {
			this.alternativeSeparator = SEPARATOR_DOT;
			this.separator = SEPARATOR_COMMA;
		} else {
			this.alternativeSeparator = SEPARATOR_COMMA;
			this.separator = SEPARATOR_DOT;
		}
	}

	public NumberConfiguration getNumberConfiguration() {
		return this.numConfig;
	}

	protected int doInsert(TextFilterContext context, int index, String text) {
		if (text.equals(this.alternativeSeparator)) {
			return insertDecimalSeparator(context, index, separator);
		}

		if (text.equals(separator)) {
			return insertDecimalSeparator(context, index, text);
		}

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

		text = checkMaxDigits(context, index, text);

		while (i < text.length()) {
			if (Character.isDigit(text.charAt(i))) {
				super.insert(context, pos, text.substring(i, i + 1));
				i++;
				pos++;
				count++;
			} else {
				String sep = this.separator;

				if (text.length() - i >= sep.length()
						&& text.substring(i, i + sep.length()).equals(sep)) {
					if (context.getText().indexOf(sep) < 0) {
						super.insert(context, pos, sep);
						count += sep.length();
					}
					i += sep.length();
					pos += sep.length();
				} else {
					i++;
				}
			}
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
	private String checkMaxDigits(TextFilterContext context, int index, String text) {
		final int maxDecimalDigits = this.numConfig.getParameters().maxFractionDigits;
		final int maxIntDigits = this.numConfig.getParameters().maxIntDigits;

		if (maxDecimalDigits < 0 && maxIntDigits < 0) {
			return text;
		}

		if (maxDecimalDigits == 0 && maxIntDigits >= 0) {
			if (text.length() + context.getText().length() > maxIntDigits) {
				int l = maxIntDigits - context.getText().length();
				return l > 0 ? text.substring(0, l) : "";
			}
		}
		
		return text;
	}
	
	// / <summary>
	// / Inserta el separador decimal.
	// / Si ya se habia ingresado, mueve la seleccion a la posicion del separador
	// / </summary>
	// / <param name="context"></param>
	// / <param name="index"></param>
	// / <param name="text"></param>
	private int insertDecimalSeparator(TextFilterContext context, int index, String text) {
		int sepIndex;
		final int maxDecimalDigits = this.numConfig.getParameters().maxFractionDigits;

		if (maxDecimalDigits == 0) {
			return 0;
		}

		if ((sepIndex = context.getText().indexOf(text)) >= 0) {
			context.setSelectionStart(sepIndex + 1);
			context.setSelectionLength(0);
			return 0;
		}

		super.insert(context, index, text);
		return text.length();
	}

	private static final String SEPARATOR_COMMA = ",";

	private static final String SEPARATOR_DOT = ".";

	private String alternativeSeparator;

	private String separator;

	private NumberConfiguration numConfig;

}
