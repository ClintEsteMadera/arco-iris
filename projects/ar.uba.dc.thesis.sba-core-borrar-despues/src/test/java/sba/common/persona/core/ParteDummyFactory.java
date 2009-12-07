/*
 * $Id: ParteDummyFactory.java,v 1.4 2007/12/19 18:15:14 cvschioc Exp $
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
package sba.common.persona.core;

import java.util.Calendar;

import sba.common.pais.Country;
import sba.common.pais.CountrySubdivision_AR;
import sba.common.persona.identificacion.IdentificadorDePersonaFisica;
import sba.common.persona.identificacion.IdentificadorImpositivo;
import sba.common.persona.identificacion.Rpc;
import sba.common.persona.identificacion.TipoIdentificadorPersonaFisica;
import sba.common.persona.identificacion.TipoInscripcionPersonaJuridicaNacional;
import sba.common.persona.identificacion.domain.IdentificadorImpositivoFactory;
import sba.common.persona.identificacion.domain.InformacionImpositivaFactory;

/**
 * Factory para objetos de tipo {@link sba.common.persona.core.domain.Parte} o
 * {@link sba.common.persona.core.domain.creation.ParteCreationData}.
 * 
 * @version $Revision: 1.4 $ - $Date: 2007/12/19 18:15:14 $
 * @author Fernando Scanavino
 */
public abstract class ParteDummyFactory {

	/**
	 * Crea una nueva instancia de una Parte.
	 */
	public static Parte construirParte() {
		return ParteDummyFactory.construirOrganismoValido();
	}

	/**
	 * Crea una nueva instancia de un Organismo.
	 */
	public static Organismo construirOrganismoValido() {
		return new Organismo(IdentificadorImpositivoFactory.construirCUITValido(), "CNV",
				"Comisión Nacional de Valores");
	}

	/**
	 * Crea una nueva instancia de un Organismo.
	 */
	public static Organismo construirOrganismoValidoAlternativo() {
		Organismo organismo = new Organismo(IdentificadorImpositivoFactory
				.construirCUITValidoAlternativo(), "MV", "Mercado de Valores");
		return organismo;
	}

	/**
	 * 
	 */
	public static PersonaJuridicaNacional construirPersonaJuridicaNacionalValida() {
		return new PersonaJuridicaNacional("persona juridica nacional", "123456789",
				construirConstitucionPersonaJuridica(), InformacionImpositivaFactory
						.construirInformacionImpositivaPersonaJuridica(), new Rpc("923723",
						CountrySubdivision_AR.BUENOS_AIRES),
				TipoInscripcionPersonaJuridicaNacional.IGJ);
	}

	/**
	 * Crea un identificador impositivo válido.
	 */
	public static IdentificadorImpositivo identificadorImpositivoValido() {
		return IdentificadorImpositivoFactory.construirCUITValido();
	}

	/**
	 * Crea una nueva instancia de Persona Física.
	 */
	public static Parte construirPersonaFisicaValida() {
		return new PersonaFisica("Mendez", "Carlo", Country.AR, new IdentificadorDePersonaFisica(
				28960498L, TipoIdentificadorPersonaFisica.DNI), InformacionImpositivaFactory
				.construirInformacionImpositivaPersonaFisica());
	}

	/**
	 * Crea una ConstitucionPersonaJuridica válida.
	 */
	public static ConstitucionPersonaJuridica construirConstitucionPersonaJuridica() {
		ConstitucionPersonaJuridica constitucionPersonaJuridica = new ConstitucionPersonaJuridica(
				Calendar.getInstance(),null, 3);
		return constitucionPersonaJuridica;
	}
}