package commons.gui.binding;

import java.util.Calendar;

import org.eclipse.swt.widgets.DateTime;


import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueBinding;
import commons.gui.model.binding.ValueBindingUpdateEvent;
import commons.gui.model.binding.ValueBindingUpdateListener;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;
import commons.utils.DateUtils;

public class DateTimeBindingFactory extends ValueModelAdapterBindingFactory {

	@Override
	public ValueBinding createBinding(final ValueModel model, Object component) {
		ValueBinding binding = super.createBinding(model, component);
		
		// para el caso en que se lee del control y se escribe en el modelo
		binding.addReadingListener(new ValueBindingUpdateListener() {
			public void updatingValueBinding(ValueBindingUpdateEvent event) {
				Calendar newValue = DateUtils.try2GetObjectAsCalendar(event.getNewValue());
				Calendar calendarModel = DateUtils.try2GetObjectAsCalendar(model.getValue());
				if(calendarModel != null && newValue != null) {
					// me quedo con el valor del Calendar del modelo y seteo los nuevos valores de hora.
					calendarModel.set(Calendar.HOUR_OF_DAY, newValue.get(Calendar.HOUR_OF_DAY));
					calendarModel.set(Calendar.MINUTE, newValue.get(Calendar.MINUTE));
					calendarModel.set(Calendar.SECOND, newValue.get(Calendar.SECOND));
					event.setNewValue(calendarModel);
				}
			}
		});
		return binding;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return new DateTimeValueModel((DateTime) component);
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof DateTime
				&& DateTimeValueModel.supportsControl((DateTime) component);
	}

}
