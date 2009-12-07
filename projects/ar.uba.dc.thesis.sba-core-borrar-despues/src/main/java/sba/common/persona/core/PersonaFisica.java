/*
 * $Id: PersonaFisica.java,v 1.4 2008/01/04 19:13:37 cvschioc Exp $
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

import sba.common.pais.Country;
import sba.common.persona.identificacion.IdentificadorDePersonaFisica;
import sba.common.persona.identificacion.InformacionImpositiva;

/**
 * 
 *
 * @author Miguel Díaz
 * @version $Revision: 1.4 $ - $Date: 2008/01/04 19:13:37 $
 */
public class PersonaFisica extends Parte {

	public PersonaFisica() {
		super();
		this.informacionImpositiva = new InformacionImpositiva();
		this.identificadorDePersonaFisica = new IdentificadorDePersonaFisica();
	}
	
	/**
	 * @param apellido
	 * @param nombre
	 * @param nacionalidad
	 * @param identificadorDePersonaFisica
	 * @param informacionImpositiva
	 */
	public PersonaFisica(String apellido, String nombre, Country nacionalidad,
			IdentificadorDePersonaFisica identificador,
			InformacionImpositiva informacionImpositiva) {
		this();
		this.doSetApellido(apellido);
		this.doSetNombre(nombre);
		this.doSetNacionalidad(nacionalidad);
		this.doSetIdentificador(identificador);
		this.doSetInformacionImpositiva(informacionImpositiva);
	}

	/**
	 * @return Returns the apellido.
	 */
	public String getApellido() {
		return this.apellido;
	}

	/**
	 * @return Returns the identificadorDePersonaFisica.
	 */
	public IdentificadorDePersonaFisica getIdentificadorDePersonaFisica() {
		if(this.identificadorDePersonaFisica == null) {
			this.identificadorDePersonaFisica = new IdentificadorDePersonaFisica();
		}
		return this.identificadorDePersonaFisica;
	}

	/**
	 * @return Returns the informacionImpositiva.
	 */
	public InformacionImpositiva getInformacionImpositiva() {
		if(this.informacionImpositiva == null) {
			this.informacionImpositiva = new InformacionImpositiva();
		}
		return this.informacionImpositiva;
	}

	/**
	 * @return Returns the nacionalidad.
	 */
	public Country getNacionalidad() {
		return this.nacionalidad;
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public String getDescripcionTipoParte() {
		return "Persona Física";
	}

	
	/**
	 * @see sba.common.persona.core.domain.Parte#identificadorUnivoco()
	 */
	@Override
	public String getIdentificadorUnivoco() {
		return this.getApellido().toString() + " " + this.getNombre().toString() + " - "
				+ this.getIdentificadorDePersonaFisica().toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof PersonaFisica) {
			equals = this.equalsTo((PersonaFisica) obj);
		}
		return equals;
	}

	private boolean equalsTo(PersonaFisica personaFisica) {
		boolean equals = true;
		if (this.apellido != null) {
			equals = equals && this.apellido.equals(personaFisica.apellido);
		} else {
			equals = equals && (personaFisica.apellido == null);
		}
		if (this.nombre != null) {
			equals = equals && this.nombre.equals(personaFisica.nombre);
		} else {
			equals = equals && (personaFisica.nombre == null);
		}
		if (this.identificadorDePersonaFisica != null) {
			equals = equals && this.identificadorDePersonaFisica.equals(
					personaFisica.identificadorDePersonaFisica);
		} else {
			equals = equals && (personaFisica.identificadorDePersonaFisica == null);
		}
		if (this.informacionImpositiva != null) {
			equals = equals && this.informacionImpositiva.equals(
					personaFisica.informacionImpositiva);
		} else {
			equals = equals && (personaFisica.informacionImpositiva == null);
		}
		if (this.nacionalidad != null) {
			equals = equals && this.nacionalidad.equals(personaFisica.nacionalidad);
		} else {
			equals = equals && (personaFisica.nacionalidad == null);
		}
		return equals;
	}
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.apellido == null) ? 0 : this.apellido.hashCode());
		result = PRIMO * result + ((this.nombre == null) ? 0 : this.nombre.hashCode());
		result = PRIMO * result + ((this.identificadorDePersonaFisica == null) ? 0 : 
			this.identificadorDePersonaFisica.hashCode());
		result = PRIMO * result + ((this.informacionImpositiva == null) ? 0 : 
			this.informacionImpositiva.hashCode());
		result = PRIMO * result + ((this.nacionalidad == null) ? 0 : this.nacionalidad.hashCode());


		return result;	
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.apellido != null ? apellido : STR_NULO);
		sb.append(" ");
		sb.append(this.nombre != null ? nombre : STR_NULO);
		sb.append(" ");		
		sb.append(this.identificadorDePersonaFisica.toString() != null ? 
				identificadorDePersonaFisica.toString() : STR_NULO);
		sb.append(" ");		
		sb.append(this.informacionImpositiva.toString() != null ? 
				informacionImpositiva.toString() : STR_NULO);
		sb.append(" ");		
		sb.append(this.nacionalidad.toString() != null ? nacionalidad.toString() : STR_NULO);
		sb.append(" ");		

		return sb.toString();
	}

	/**
	 * @param unApellido
	 *            The apellido to set.
	 */
	private void doSetApellido(String unApellido) {
		if (unApellido == null) {
			throw new IllegalArgumentException("Apellido nulo");
		}
		this.apellido = unApellido;
	}

	/**
	 * @param identificadorDePersonaFisica
	 *            The identificadorDePersonaFisica to set.
	 */
	private void doSetIdentificador(IdentificadorDePersonaFisica identificador) {
		if (identificador == null) {
			throw new IllegalArgumentException("Identificador de persona nulo");
		}
		this.identificadorDePersonaFisica = identificador;
	}

	/**
	 * @param infoImpositiva
	 *            The informacionImpositiva to set.
	 */
	private void doSetInformacionImpositiva(InformacionImpositiva infoImpositiva) {
		if (infoImpositiva == null) {
			throw new IllegalArgumentException("Información impositiva nula");
		}
		this.informacionImpositiva = infoImpositiva;
	}

	/**
	 * @param nac
	 *            The nacionalidad to set.
	 */
	private void doSetNacionalidad(Country nac) {
		if (nac == null) {
			throw new IllegalArgumentException("Nacionalidad nula");
		}
		this.nacionalidad = nac;
	}

	/**
	 * @param nom
	 *            The nombre to set.
	 */
	private void doSetNombre(String nom) {
		if (nom == null) {
			throw new IllegalArgumentException("Nombre nulo");
		}
		this.nombre = nom;
	}

	/**
	 * 
	 * @param apellido
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * 
	 * @param identificadorDePersonaFisica
	 */
	public void setIdentificadorDePersonaFisica(
			IdentificadorDePersonaFisica identificadorDePersonaFisica) {
		this.identificadorDePersonaFisica = identificadorDePersonaFisica;
	}

	/**
	 * 
	 * @param informacionImpositiva
	 */
	public void setInformacionImpositiva(InformacionImpositiva informacionImpositiva) {
		this.informacionImpositiva = informacionImpositiva;
	}

	/**
	 * 
	 * @param nacionalidad
	 */
	public void setNacionalidad(Country nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	/***
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	private String apellido;

	private String nombre;

	private Country nacionalidad;

	private IdentificadorDePersonaFisica identificadorDePersonaFisica;

	private InformacionImpositiva informacionImpositiva;

	public static final long serialVersionUID = 1L;
}
