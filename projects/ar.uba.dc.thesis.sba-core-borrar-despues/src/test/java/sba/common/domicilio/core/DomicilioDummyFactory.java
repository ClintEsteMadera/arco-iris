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
