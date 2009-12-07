package commons.gui.model.binding;

import java.util.ArrayList;
import java.util.List;

import commons.gui.binding.ButtonBindingFactory;
import commons.gui.binding.CalendarBindingFactory;
import commons.gui.binding.ComboBindingFactory;
import commons.gui.binding.DateTimeBindingFactory;
import commons.gui.binding.ListBindingFactory;
import commons.gui.binding.RadioButtonGroupBindingFactory;
import commons.gui.binding.TableBindingFactory;
import commons.gui.binding.TextBindingFactory;
import commons.gui.model.ValueModel;

public class ValueBindingManager {

	private List<ValueBindingFactory> factories;

	public static ValueBindingManager getInstance() {
		return instance;
	}

	/**
	 * Agrega un factory a la lista
	 * 
	 * @param factory
	 *            Factory
	 */
	public void addBindingFactory(ValueBindingFactory factory) {
		factories.add(factory);
	}

	/**
	 * Obtiene un factory para un componente
	 * 
	 * @param component
	 *            Componente
	 */
	public ValueBindingFactory getBindingFactory(Object component) {
		for (ValueBindingFactory factory : factories) {
			if (factory.supports(component)) {
				return factory;
			}
		}
		return null;
	}

	public <T> ValueBinding bindValueModel(ValueModel<T> model, Object component) {
		final ValueBinding binding = createBinding(model, component);
		binding.bind();
		return binding;
	}

	public <T> ValueBinding createBinding(ValueModel<T> model, Object component) {
		if (component == null) {
			throw new IllegalArgumentException("El parámetro 'component' no puede ser nulo");
		}

		ValueBindingFactory factory = ValueBindingManager.getInstance()
				.getBindingFactory(component);

		if (factory == null) {
			throw new IllegalArgumentException("No existe un "
					+ ValueBindingFactory.class.getName() + " para el objeto de tipo "
					+ component.getClass().getName());
		}

		return factory.createBinding(model, component);
	}

	private ValueBindingManager() {
		factories = new ArrayList<ValueBindingFactory>();

		// Binding Factories de SWT
		addBindingFactory(new TextBindingFactory());
		addBindingFactory(new ComboBindingFactory());
		addBindingFactory(new ButtonBindingFactory());
		addBindingFactory(new RadioButtonGroupBindingFactory());
		addBindingFactory(new CalendarBindingFactory());
		addBindingFactory(new TableBindingFactory());
		addBindingFactory(new DateTimeBindingFactory());
		addBindingFactory(new ListBindingFactory());
		addBindingFactory(new ValueModelBindingFactory());
	}

	private static ValueBindingManager instance = new ValueBindingManager();
}
