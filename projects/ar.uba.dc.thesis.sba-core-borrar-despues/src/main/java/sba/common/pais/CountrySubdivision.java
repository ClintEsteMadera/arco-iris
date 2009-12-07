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
 * $Id: CountrySubdivision.java,v 1.1 2007/10/02 20:56:01 cvschioc Exp $
 */

package sba.common.pais;

/**
 * Interfaz que deben implementar todos las subdivisiones de países.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2007/10/02 20:56:01 $
 */

public interface CountrySubdivision {

	public String name();
	
	public String descripcion();
}
