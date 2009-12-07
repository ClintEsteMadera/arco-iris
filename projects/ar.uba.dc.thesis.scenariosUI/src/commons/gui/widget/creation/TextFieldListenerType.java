/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: TextFieldListenerType.java,v 1.2 2007/08/09 20:27:29 cvschioc Exp $
 */
package commons.gui.widget.creation;

import org.eclipse.swt.widgets.Text;

import commons.gui.util.ListenerHelper;
import commons.gui.util.PageHelper;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.2 $ $Date: 2007/08/09 20:27:29 $
 */
public enum TextFieldListenerType {

	NOT_NULL_LISTENER {
		@Override
		public void addListener(Text textBox) {
			ListenerHelper.addNotNullValidationListener(textBox,
					PageHelper.getMainWindow().currentPreferencePage);
		}
	},
	EMAIL_LISTENER {
		@Override
		public void addListener(Text textBox) {
			ListenerHelper.addEmailValidationListener(textBox,
					PageHelper.getMainWindow().currentPreferencePage);
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
