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
 * $Id: DomicilioDummyFactory.java,v 1.2 2007/11/09 19:16:29 cvschioc Exp $
 */
package sba.common.domicilio.core;

import sba.common.domicilio.Domicilio;
import sba.common.pais.Country;
import sba.common.pais.CountrySubdivision;
import sba.common.pais.CountrySubdivision_AR;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.2 $ $Date: 2007/11/09 19:16:29 $
 */
public abstract class DomicilioDummyFactory {

	public static Domicilio crearDomicilioCompleto() {
		return new Domicilio(CALLE, NRO_CALLE, LOCALIDAD, COD_POSTAL, PISO, DEPTO, PAIS, PROVINCIA,
				OBSERVACIONES);
	}

	public static Domicilio crearDomicilioincompleto() {
		return new Domicilio(null, null, null, null, null, null, null, null, null);
	}

	private DomicilioDummyFactory() {
		super();
	}

	private static final String CALLE = "25 de Mayo";

	private static final String NRO_CALLE = "362";

	private static final String LOCALIDAD = "Capital Federal";

	private static final String COD_POSTAL = "1408";

	private static final String PISO = "2";

	private static final String DEPTO = "A";

	private static final Country PAIS = Country.AR;

	private static final CountrySubdivision PROVINCIA = CountrySubdivision_AR.BUENOS_AIRES;

	private static final String OBSERVACIONES = null;
}
