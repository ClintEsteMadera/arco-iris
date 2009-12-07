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
* $Id: BaseCriterio.java,v 1.1 2008/03/28 20:46:06 cvschioc Exp $
*/

package sba.common.query;

import java.io.Serializable;


/**
 * Clase de la cual debe heredar todo criterio de b�squeda.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/03/28 20:46:06 $
 */

public abstract class BaseCriterio implements Serializable {

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	private Long id;
	
	private static final long serialVersionUID = 1L;
}