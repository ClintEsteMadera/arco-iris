package commons.gui.widget.composite;

/**
 * Composite para seleccion de un objeto
 * 
 * @author ppastorino
 */
public abstract class SimpleObjectSelectionComposite<T> extends ObjectSelectionComposite<T> {

	public SimpleObjectSelectionComposite(ObjectSelectionMetainfo info) {
		super(info);
	}

	protected abstract T selectObject();

	@Override
	protected void launchSelection() {
		final T value = selectObject();

		if (value == null) {
			return;
		}
		setSelectionModel(value);
	}
}