package commons.gui.swt.text;

import commons.gui.model.types.EditType;

public interface TextFormatterFactory {

	TextFormatter createFormatter(EditType type);

	boolean supports(EditType type);
}
