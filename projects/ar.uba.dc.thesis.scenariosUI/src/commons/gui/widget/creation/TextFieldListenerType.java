package commons.gui.widget.creation;

import org.eclipse.swt.widgets.Text;

import commons.gui.util.ListenerHelper;
import commons.gui.util.PageHelper;

/**
 * 
 */
public enum TextFieldListenerType {

	NOT_NULL_LISTENER {
		@Override
		public void addListener(Text textBox) {
			ListenerHelper.addNotNullValidationListener(textBox, PageHelper.getMainWindow().currentPreferencePage);
		}
	},
	EMAIL_LISTENER {
		@Override
		public void addListener(Text textBox) {
			ListenerHelper.addEmailValidationListener(textBox, PageHelper.getMainWindow().currentPreferencePage);
		}
	},
	INTEGER_FIELD_LISTENER {
		@Override
		public void addListener(Text textBox) {
			ListenerHelper.addIntegerFieldKeyListener(textBox);
			ListenerHelper.addIntegerFieldModifyListener(textBox);
			ListenerHelper.addIntegerFieldFocusListener(textBox);
		}
	},
	NUMBER_FIELD_LISTENER {
		@Override
		public void addListener(Text textBox) {
			// TODO: implementar NUMBER_FIELD_LISTENER!
			throw new RuntimeException("implementar NUMBER_FIELD_LISTENER!");
		}
	},
	NOT_NUMBER_FIELD_LISTENER {
		@Override
		public void addListener(Text textBox) {
			// TODO: implementar NOT_NUMBER_FIELD_LISTENER!
			throw new RuntimeException("implementar NOT_NUMBER_FIELD_LISTENER!");
		}
	};

	public abstract void addListener(Text textBox);

}
