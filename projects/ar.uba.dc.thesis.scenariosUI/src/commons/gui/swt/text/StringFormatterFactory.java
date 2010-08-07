package commons.gui.swt.text;

import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.types.StringConfiguration;

public class StringFormatterFactory implements TextFormatterFactory {

	@SuppressWarnings("unchecked")
	public TextFormatter createFormatter(EditType type) {
		final EditConfiguration cfg = EditConfigurationManager.getInstance().getConfiguration(type);

		TextFilter filter = null;

		if (cfg != null && cfg instanceof StringConfiguration) {
			StringConfiguration sCfg = (StringConfiguration) cfg;
			filter = new StringTextFilter(sCfg);
		} else {
			filter = new DefaultTextFilter();
		}
		return new FilteredTextFormatter(filter);
	}

	public boolean supports(EditType type) {
		return true;
	}
}
