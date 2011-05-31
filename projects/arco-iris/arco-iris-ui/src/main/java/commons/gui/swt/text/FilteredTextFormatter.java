package commons.gui.swt.text;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class FilteredTextFormatter implements TextFormatter {
	public FilteredTextFormatter(TextFilter filter) {
		this.filterContext = new TextFilterContextImpl();
		this.filter = filter;

		this.verifyListener = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				onVerifyText(e);
			}
		};
	}

	public void install(Text txtBox) {
		this.textBox = txtBox;

		if (this.filter.getTextLimit() > 0) {
			txtBox.setTextLimit(this.filter.getTextLimit());
		}
		addEventHandlers(txtBox);
	}

	public void unInstall(Text txtBox) {
		removeEventHandlers(txtBox);
		this.textBox = null;
	}

	private void addEventHandlers(Text text) {
		text.addVerifyListener(this.verifyListener);
	}

	private void removeEventHandlers(Text text) {
		text.removeVerifyListener(this.verifyListener);
	}

	private void onVerifyText(VerifyEvent e) {

		if (this.bypassFilter) {
			return;
		}

		if (e.text.length() == 0) {
			return;
		}

		this.filterContext.readTextBox(this.textBox);

		final int l = e.end - e.start;
		final int newL = this.filterContext.getText().length() - l;

		this.filter.replace(this.filterContext, e.start, l, e.text);

		final String filteredText = this.filterContext.getText();
		final int insertL = filteredText.length() - newL;

		if (insertL > 0) {
			e.text = filteredText.substring(e.start, e.start + insertL);
		} else {
			e.doit = false;
		}
	}

	protected void bypassFilter(boolean b) {
		this.bypassFilter = b;
	}

	private VerifyListener verifyListener;

	private Text textBox;

	private TextFilterContextImpl filterContext;

	private TextFilter filter;

	private boolean bypassFilter = false;
}
