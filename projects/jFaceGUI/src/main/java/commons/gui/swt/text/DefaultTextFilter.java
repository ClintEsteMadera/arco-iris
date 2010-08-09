package commons.gui.swt.text;

public class DefaultTextFilter implements TextFilter {

	public void insert(TextFilterContext context, int index, String text) {
		context.insert(index, text);
		context.setSelectionStart(index + text.length());
		context.setSelectionLength(0);
	}

	public void remove(TextFilterContext context, int index, int length) {
		context.remove(index, length);
	}

	public void replace(TextFilterContext context, int index, int length, String text) {
		context.replace(index, length, text);
		context.setSelectionStart(index + text.length());
		context.setSelectionLength(0);
	}

	public void handleChange(TextFilterContext context) {
	}

	public int getTextLimit() {
		return textLimit;
	}

	public void setTextLimit(int textLimit) {
		this.textLimit = textLimit;
	}

	private int textLimit = -1;
}
