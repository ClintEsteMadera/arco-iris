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
* $Id: ValidationManager.java,v 1.2 2008/05/15 20:53:30 cvspasto Exp $
*/

package commons.gui.model.validation;

import org.eclipse.swt.widgets.Control;

import commons.gui.model.CompositeModel;
import commons.gui.model.NestedModel;
import commons.gui.widget.creation.binding.Binding;
import commons.gui.widget.creation.binding.FakeBinding;

/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.2 $ $Date: 2008/05/15 20:53:30 $
 */

public class ValidationManager {
	
	public static void setValidationStatus(Control c, ValidationStatusAware v){
		c.setData(VALIDATION_STATUS_KEY, v);
	}

	public static ValidationStatusAware getValidationStatus(Control c){
		return (ValidationStatusAware)c.getData(VALIDATION_STATUS_KEY);
	}

	public static void setValidationProperty(Control c, String p){
		c.setData(VALIDATION_PROPERTY_KEY, p);
	}

	public static void setValidationProperty(Control c, Binding bI){
		if(bI instanceof FakeBinding){
			return;
		}
		final CompositeModel m=bI.getCompositeModel();
		
		if(m != null){
			setValidationProperty(c,getProperty(m,bI.getPropertyName()));
		}
	}

	public static void setValidationProperty(Control c,CompositeModel m,String property){
		setValidationProperty(c,getProperty(m,property));
	}

	private static String getProperty(CompositeModel m,String p){
		if(m instanceof NestedModel){
			NestedModel nm=(NestedModel)m;
			if(p.length() > 0){
				return getProperty(nm.getParentModel(),nm.getRootProperty()) + "." + p;
			}
			return getProperty(nm.getParentModel(),nm.getRootProperty());
		}
		return p;
	}
	
	public static String getValidationProperty(Control c){
		return (String)c.getData(VALIDATION_PROPERTY_KEY);
	}

	private static final String VALIDATION_STATUS_KEY=ValidationManager.class.getName() + ".validationInfo";
	
	private static final String VALIDATION_PROPERTY_KEY=ValidationManager.class.getName() + ".validationProperty";

	public static void setValidationHandler(ValidableComposite v) {
		validable=v;
	}
	
	public static ValidableComposite getValidationHandler() {
		return validable;
	}
	
	private static ValidableComposite validable;

}
