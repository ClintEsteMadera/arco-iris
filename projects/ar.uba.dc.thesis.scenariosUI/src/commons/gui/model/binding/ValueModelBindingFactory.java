package commons.gui.model.binding;

import commons.gui.model.ValueModel;

public class ValueModelBindingFactory 
implements ValueBindingFactory {
	
    public ValueBinding createBinding(ValueModel model, Object component)
    {
        return new ValueModelBinding(model, (ValueModel)component);
    }

    public boolean supports(Object component)
    {
        return component instanceof ValueModel;
    }
}
