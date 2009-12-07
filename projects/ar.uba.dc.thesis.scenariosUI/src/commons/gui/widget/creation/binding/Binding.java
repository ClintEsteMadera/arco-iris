/*
 * $Id: Binding.java,v 1.13 2008/01/28 17:00:03 cvschioc Exp $
 *
 * Copyright (c) 2003 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, República Argentina.
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores
 * S.A. ("Información Confidencial"). Usted no divulgará tal Información
 * Confidencial y solamente la usará conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */

package commons.gui.widget.creation.binding;

import commons.gui.model.CompositeModel;
import commons.gui.model.binding.ValueBinding;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.13 $
 */

public interface Binding {

	String getValue();

	CompositeModel getCompositeModel();

	Object getModel();
	
	String getPropertyName();
	
	boolean isFakeBinding();
	
	ValueBinding bind(final Object component);
}