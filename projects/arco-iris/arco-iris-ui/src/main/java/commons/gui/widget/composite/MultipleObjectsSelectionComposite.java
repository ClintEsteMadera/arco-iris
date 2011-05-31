package commons.gui.widget.composite;

import java.util.List;

/**
 * This composite provides the skeleton for selecting multiple objects of the same type.
 */
public abstract class MultipleObjectsSelectionComposite<T> extends ObjectSelectionComposite<List<T>> {

	public MultipleObjectsSelectionComposite(ObjectSelectionMetainfo info) {
		super(info);
	}

	protected abstract List<T> selectObjects();

	@Override
	protected void launchSelection() {
		final List<T> value = selectObjects();

		if (value == null) {
			return;
		}
		this.setSelectionModel(value);
	}

}
