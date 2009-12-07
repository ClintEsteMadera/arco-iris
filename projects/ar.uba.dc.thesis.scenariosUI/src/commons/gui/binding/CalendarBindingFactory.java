package commons.gui.binding;

import java.text.DateFormat;
import java.text.Format;
import java.util.Calendar;

import sba.common.utils.DateUtils;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueBinding;
import commons.gui.model.binding.ValueBindingUpdateEvent;
import commons.gui.model.binding.ValueBindingUpdateListener;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.widget.composite.CalendarComposite;

public class CalendarBindingFactory extends ValueModelAdapterBindingFactory {

	@Override
	public ValueBinding createBinding(final ValueModel model, Object component) {
		ValueBinding binding = super.createBinding(model, component);
		
		// para el caso en que se lee del control y se escribe en el modelo
		binding.addReadingListener(new ValueBindingUpdateListener() {
			public void updatingValueBinding(ValueBindingUpdateEvent event) {
				Calendar newValue = DateUtils.try2GetObjectAsCalendar(event.getNewValue());
				Calendar calendarModel = DateUtils.try2GetObjectAsCalendar(model.getValue());
				if(calendarModel != null && newValue != null) {
					// me quedo con el valor del Calendar del modelo y seteo los nuevos valores de fecha.
					calendarModel.set(Calendar.DAY_OF_MONTH, newValue.get(Calendar.DAY_OF_MONTH));
					calendarModel.set(Calendar.MONTH, newValue.get(Calendar.MONTH));
					calendarModel.set(Calendar.YEAR, newValue.get(Calendar.YEAR));
					event.setNewValue(calendarModel);
				}
			}
		});
		
		//si el tipo es calendar convierto a date
		if(Calendar.class.equals(model.getValueType().getValueClass())){
			addCalendarConverters(binding);
		}
		
		return binding;
	}
	
	private void addCalendarConverters(ValueBinding binding) {
		// para el caso en que se lee del control y se escribe en el modelo
		binding.addReadingListener(new ValueBindingUpdateListener() {
			public void updatingValueBinding(ValueBindingUpdateEvent event) {
				event.setNewValue(DateUtils.try2GetObjectAsCalendar(event.getNewValue()));
			}
		});

		// para el caso en que se lee del modelo y se escribe en el control
		binding.addWritingListener(new ValueBindingUpdateListener() {
			public void updatingValueBinding(ValueBindingUpdateEvent event) {
				if(event.getNewValue() instanceof Calendar && event.getNewValue() != null){
					event.setNewValue(((Calendar)event.getNewValue()).getTime());
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		CalendarComposite cal = (CalendarComposite) component;

		// seteo el formato asociado al valor (debe ser un date format)
		final Format format = EditConfigurationManager.getInstance()
				.getFormat(model.getValueType());
		if (format != null && format instanceof DateFormat) {
			cal.setDateFormat((DateFormat) format);
		}

		final TextValueModel text=new TextValueModel(cal.getCalendarControl(), cal.getDateFormat());
		text.setCheckFormat(true);
		text.setDisplayFormat(cal.getDateFormat());
		
		return text;
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof CalendarComposite;
	}

}
