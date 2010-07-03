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
* $Id: Mes.java,v 1.1 2008/03/25 19:51:30 cvsmarco Exp $
*/

package commons.datetime;

import java.io.Serializable;


/**
 * Modela los meses del a�o.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/03/25 19:51:30 $
 */

public enum Mes implements Serializable {

	ENERO("Enero"),
	FEBRERO("Febrero"),
	MARZO("Marzo"),
	ABRIL("Abril"),
	MAYO("Mayo"),
	JUNIO("Junio"),
	JULIO("Julio"),
	AGOSTO("Agosto"),
	SEPTIEMBRE("Septiembre"),
	OCTUBRE("Octubre"),
	NOVIEMBRE("Noviembre"),
	DICIEMBRE("Diciembre");
	
	private Mes(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return this.descripcion;
	}

	private String descripcion;
}
