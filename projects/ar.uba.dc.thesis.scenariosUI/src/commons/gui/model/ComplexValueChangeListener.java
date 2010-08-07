package commons.gui.model;

/**
 * Listener para {@link sba.ui.modek.ComplexValueChangeEvent}
 */
public interface ComplexValueChangeListener {
	/**
	 * Se invoca depués de la modificación del modelo.
	 * 
	 * @param ev
	 */
	public void complexValueChange(ComplexValueChangeEvent ev);
};
