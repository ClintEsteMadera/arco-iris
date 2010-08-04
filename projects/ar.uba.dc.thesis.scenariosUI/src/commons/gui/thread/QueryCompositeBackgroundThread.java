package commons.gui.thread;

import java.util.List;

import org.eclipse.swt.widgets.Display;

import commons.gui.util.PageHelper;
import commons.gui.widget.composite.QueryComposite;

public abstract class QueryCompositeBackgroundThread<T> extends BackgroundThread {

	private List<T> items;

	private QueryComposite<T> queryComposite;

	public QueryCompositeBackgroundThread(Display display, QueryComposite<T> queryComposite) {
		super(display);
		this.queryComposite = queryComposite;
	}

	protected abstract List<T> doQuery();

	@Override
	protected void performBackgroundOperation() {
		items = doQuery();
	}

	@Override
	protected void updateUI() {
		queryComposite.getTable().setInput(items);
		queryComposite.getTable().getTable().setEnabled(true);
		PageHelper.getMainWindow().setDefaultStatusMessage();
	}
}
