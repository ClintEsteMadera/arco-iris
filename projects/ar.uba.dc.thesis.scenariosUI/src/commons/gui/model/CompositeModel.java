package commons.gui.model;

/**
 * Modelo compuesto. <br>
 * <br>
 * Este tipo de modelos son un {@link ValueModel} y además contienen instancias de
 * {@link ValueModel}
 */
public interface CompositeModel<T> extends ValueModel<T>, ComplexModel {

	public <NESTED_TYPE> CompositeModel<NESTED_TYPE> getNestedModel(String key,
			Class<NESTED_TYPE> clazz);
};
