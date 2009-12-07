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
* $Id: NullAttribute.java,v 1.1 2008/02/11 13:53:02 cvspasto Exp $
*/

package commons.gui.model;

import commons.gui.model.types.EditType;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/02/11 13:53:02 $
 */

@SuppressWarnings("unchecked")
public class NullAttribute 
implements Attribute {

	public Object getValue(Object target) {
		return null;
	}

	public EditType getValueType() {
		return type;
	}

	public void setValue(Object target, Object value) {
	}

	private static final EditType type=new EditType(Object.class); 
}
