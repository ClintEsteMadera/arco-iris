/*
 * $Id: IdentificadorImpositivoFactory.java,v 1.2 2007/12/19 18:06:21 cvschioc Exp $
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362 Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de
 * Caja de Valores S.A. ("Información Confidencial"). Usted no
 * divulgará tal Información Confidencial y la usará solamente
 * de acuerdo a los términos del acuerdo de licencia que Ud. posee
 * con Caja de Valores S.A.
 */
package sba.common.persona.identificacion.domain;

import sba.common.persona.identificacion.IdentificadorImpositivo;
import sba.common.persona.identificacion.TipoIdentificadorImpositivo;

/**
 * @version $Revision: 1.2 $ - $Date: 2007/12/19 18:06:21 $
 * @author Fernando Scanavino
 */
public abstract class IdentificadorImpositivoFactory {

	public static IdentificadorImpositivo construirIdentificadorImpositivoValido() {
		return IdentificadorImpositivoFactory.construirCUITValido();
	}

	public static IdentificadorImpositivo construirCDIValido() {
		return new IdentificadorImpositivo(IdentificadorImpositivoFactory.numeroValido(),
				TipoIdentificadorImpositivo.CDI);
	}

	public static IdentificadorImpositivo construirCUILValido() {
		return new IdentificadorImpositivo(IdentificadorImpositivoFactory.numeroValido(),
				TipoIdentificadorImpositivo.CUIL);
	}

	public static IdentificadorImpositivo construirCUITValido() {
		return new IdentificadorImpositivo(IdentificadorImpositivoFactory.numeroValido(),
				TipoIdentificadorImpositivo.CUIL);
	}

	public static IdentificadorImpositivo construirCUITValidoAlternativo() {
		return new IdentificadorImpositivo(
				IdentificadorImpositivoFactory.numeroValidoAlternativo(),
				TipoIdentificadorImpositivo.CUIT);
	}

	public static Long numeroValido() {
		return 20123456786L;
	}

	public static Long numeroValidoAlternativo() {
		return 30500003193L;
	}
}