package commons.gui.model;

/**
 * Listener para ValueModel
 */
public interface ValueChangeListener<T> {

	public void valueChange(ValueChangeEvent<T> ev);
}
