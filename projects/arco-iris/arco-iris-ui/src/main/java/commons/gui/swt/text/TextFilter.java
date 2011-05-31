package commons.gui.swt.text;

/**
 * Filtro para un componente de tipo text.
 * 
 * 
 */
public interface TextFilter {
	void insert(TextFilterContext context, int index, String text);

	void remove(TextFilterContext context, int index, int length);

	void replace(TextFilterContext context, int index, int length, String text);

	void handleChange(TextFilterContext context);

	int getTextLimit();

}
