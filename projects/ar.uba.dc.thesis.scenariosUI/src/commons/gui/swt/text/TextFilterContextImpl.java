package commons.gui.swt.text;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

class TextFilterContextImpl implements TextFilterContext {

	int selectionStart;

	int selectionLength;

	StringBuffer text;

	public TextFilterContextImpl() {
		text = new StringBuffer();
	}

	public void insert(int index, String txt) {
		this.text.insert(index, txt);
	}

	public void remove(int index, int length) {
		this.text.delete(index, index + length);
	}

	public void clear() {
		this.text.setLength(0);
	}

	public void replace(int index, int length, String txt) {
		this.text.delete(index, index + length);
		this.text.insert(index, txt);
	}

	public String getText() {
		return text.toString();
	}

	public int getTextLength() {
		return text.length();
	}

	public int getSelectionStart() {
		return this.selectionStart;
	}

	public void setSelectionStart(int i) {
		this.selectionStart = i;
	}

	public int getSelectionLength() {
		return this.selectionLength;
	}

	public void setSelectionLength(int i) {
		this.selectionLength = i;
	}

	void readTextBox(Text textBox) {
		Point sel = textBox.getSelection();

		this.text.setLength(0);
		this.text.append(textBox.getText());
		this.selectionStart = sel.x;
		this.selectionLength = (sel.y - sel.x) + 1;
	}

	void writeTextBox(Text textBox) {
		String newText = text.toString();

		if (!newText.equals(textBox.getText())) {
			textBox.setText(newText);
		}
		textBox.setSelection(new Point(this.selectionStart, (this.selectionStart + this.selectionLength) - 1));
	}

}
