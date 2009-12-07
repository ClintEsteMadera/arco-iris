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
* $Id: BaseCriterio.java,v 1.1 2008/03/28 20:46:06 cvschioc Exp $
*/

package sba.common.query;

import java.io.Serializable;


/**
 * Clase de la cual debe heredar todo criterio de búsqueda.
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