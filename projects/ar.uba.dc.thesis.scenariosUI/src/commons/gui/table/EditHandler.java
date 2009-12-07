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
* $Id: EditHandler.java,v 1.3 2008/02/19 14:42:50 cvspasto Exp $
*/

package commons.gui.table;


/**
 * Callbacks para edicion sobre una tabal.
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/02/19 14:42:50 $
 */

public interface EditHandler<T> {

	public T handleCreation(int index);
	
	public boolean handleUpdate(T e,int index);
	
	public void handleView(T e,int index);
	
	public boolean handleDelete(T e,int index);
}
