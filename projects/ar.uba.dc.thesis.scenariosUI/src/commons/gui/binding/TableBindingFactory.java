package commons.gui.binding;

import org.eclipse.swt.SWT;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;
import commons.gui.table.GenericTable;

public class TableBindingFactory extends ValueModelAdapterBindingFactory {

	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		final GenericTable viewer = (GenericTable) component;
		
		if((viewer.getTable().getStyle() & SWT.CHECK) != 0)
		{
			return new CheckedTableValueModel(viewer, model.getValueType());
		}
		
		return new TableValueModel(viewer, model.getValueType());
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof GenericTable;
	}

}
