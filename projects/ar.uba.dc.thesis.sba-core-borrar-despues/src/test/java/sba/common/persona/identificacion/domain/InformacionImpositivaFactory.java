/*
 * $Id: InformacionImpositivaFactory.java,v 1.2 2008/04/01 16:41:59 cvschioc Exp $
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

import sba.common.persona.identificacion.CondicionAnteIVA;
import sba.common.persona.identificacion.InformacionImpositiva;

/**
 * @version $Revision: 1.2 $ - $Date: 2008/04/01 16:41:59 $
 * @author Fernando Scanavino
 */
public abstract class InformacionImpositivaFactory {

	public static InformacionImpositiva construirInformacionImpositivaPersonaJuridica() {
		return new InformacionImpositiva(CondicionAnteIVA.RESPONSABLE_INSCRIPTO,
				IngresosBrutosFactory.construirIngresosBrutos(), IdentificadorImpositivoFactory
						.construirCUITValido());
	}

	public static InformacionImpositiva construirInformacionImpositivaPersonaFisica() {
		return new InformacionImpositiva(CondicionAnteIVA.CONSUMIDOR_FINAL, IngresosBrutosFactory
				.construirIngresosBrutos(), IdentificadorImpositivoFactory.construirCUILValido());
	}
}
