/*
* Licencia de Caja de Valores S.A., Versi�n 1.0
*
* Copyright (c) 2006 Caja de Valores S.A.
* 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
* Todos los derechos reservados.
*
* Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
* Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
* los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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
