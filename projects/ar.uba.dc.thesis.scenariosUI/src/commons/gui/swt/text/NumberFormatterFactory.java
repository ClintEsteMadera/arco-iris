package commons.gui.swt.text;

import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.types.NumberConfiguration;

public class NumberFormatterFactory implements TextFormatterFactory {

	@SuppressWarnings("unchecked")
	public TextFormatter createFormatter(EditType type) {
		EditConfiguration cfg = EditConfigurationManager.getInstance().getConfiguration(type);
		if (cfg != null && cfg instanceof NumberConfiguration) {
			NumberConfiguration nCfg = (NumberConfiguration) cfg;
			return new FilteredTextFormatter(new NumericTextFilter(nCfg));
		}
		throw new IllegalArgumentException("La clase '" + type.getValueClass().getName() + "' no extiende "
				+ Number.class.getName());
	}

	public boolean supports(EditType type) {
		return Number.class.isAssignableFrom(type.getValueClass());
	}
}
