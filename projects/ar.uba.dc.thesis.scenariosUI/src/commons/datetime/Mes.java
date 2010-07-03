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
* $Id: Mes.java,v 1.1 2008/03/25 19:51:30 cvsmarco Exp $
*/

package commons.datetime;

import java.io.Serializable;


/**
 * Modela los meses del año.
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
