package commons.gui.widget.composite;

/**
 * This composite provides the skeleton for selecting an object.
 */
public abstract class SingleObjectSelectionComposite<T> extends ObjectSelectionComposite<T> {

	public SingleObjectSelectionComposite(ObjectSelectionMetainfo info) {
		super(info);
	}

	protected abstract T selectObject();

	@Override
	protected void launchSelection() {
		final T value = selectObject();

		if (value == null) {
			return;
		}
		this.setSelectionModel(value);
	}
}