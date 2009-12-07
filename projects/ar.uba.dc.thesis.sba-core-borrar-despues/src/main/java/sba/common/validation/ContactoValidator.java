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
 * $Id: ContactoValidator.java,v 1.6 2008/04/10 18:31:53 cvschioc Exp $
 */

package sba.common.validation;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import sba.common.contacto.Contacto;
import sba.common.contacto.TipoMedioContacto;

/**
 * Validador para el concepto {@link Contacto}. Permite especificar si se admite un Domicilio
 * <i>nulo</i> o no.<br>
 * Se entiende por Domicilio nulo, a aquella referencia del tipo Domicilio que sea
 * <code>== null</code> ó bien, a aquel objeto Domicilio no nulo que posee todas sus propiedades
 * nulas.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.6 $ $Date: 2008/04/10 18:31:53 $
 */

public abstract class ContactoValidator implements Serializable {

	/**
	 * Valida el la lista de Contactos de acuerdo a las siguientes reglas: Si la lista es vacía y el
	 * parámetro <code>emptyListAllowed</code> es <code>true</code>, la lista de Contactos se
	 * considera válida. Sino, para cada uno de los contactos, invoca al método
	 * {@link #isValid(Contacto, ValidationResult)} juntando todos los resultados intermedios en el
	 * parámetro <code>result</code>.
	 * 
	 * @param contactos
	 *            una lista NO NULA de Contactos
	 * @param result
	 *            el objeto al cual añadir errores en caso de haberlos
	 * @param emptyListAllowed
	 *            especifica si se permite que la lista sea vacía
	 * @return <code>true</code>, sií o bien la lista es vacía y <code>emptyListAllowed</code>
	 *         es <code>true</code> o bien, la lista es no vacía y todos los Contactos son
	 *         válidos, de acuerdo al método {@link #isValid(Contacto, ValidationResult)}.
	 */
	public static boolean isValid(List<Contacto> contactos, ValidationResult result,
			boolean emptyListAllowed) {
		// NOTA IMPORTANTE: NO cambiar el orden de evaluación de los "&&"!!
		boolean isValid = true;
		if (contactos.isEmpty()) {
			if (!emptyListAllowed) {
				result.addError(CONTACTO, CommonValidationMessages.NOT_NULL, StringUtils
						.capitalize(CONTACTO));
				isValid = false;
			}
		} else {
			for (Contacto contacto : contactos) {
				isValid = isValid(contacto, result) && isValid;
			}
		}
		return isValid;
	}

	/**
	 * Decide si se cumplen todas estas condiciones:
	 * <ul>
	 * <li> Denominación - Obligatorio – 255 caracteres alfanuméricos
	 * <li> Tipo de Contacto - Obligatorio
	 * <li> Tipo de Medio de Contacto - Obligatorio
	 * <li> Descripción - Obligatoria – 255 caracteres alfanuméricos
	 * </ul>
	 * 
	 * @param contacto
	 *            el Contacto a validar
	 * @param result
	 *            el objeto al cual añadir errores en caso de haberlos
	 * @return <code>true</code>, si TODAS las condiciones mencionadas arriba se cumplen.
	 *         <code>false</code>, en otro caso.
	 */
	public static boolean isValid(Contacto contacto, ValidationResult result) {
		boolean isValid = ValidationUtils.isMandatory(contacto.getDenominacion(), result,
				CommonDataTypes.getCommonTypes(), CommonDataTypes.CONTACTO_DENOMINACION, CONTACTO,
				DENOMINACION);

		isValid = ValidationUtils.isNotNull(result, contacto.getTipoContacto(), CONTACTO,
				TIPO_CONTACTO)
				&& isValid;

		isValid = ValidationUtils.isNotNull(result, contacto.getTipoMedioContacto(), CONTACTO,
				TIPO_CONTACTO)
				&& isValid;

		String valueDescription = DESCRIPCION;
		if (contacto.getTipoMedioContacto() != null) {
			valueDescription = contacto.getTipoMedioContacto().toString();
		}
		isValid = ValidationUtils
				.isMandatory(contacto.getDescripcion(), result, CommonDataTypes.getCommonTypes(),
						CommonDataTypes.CONTACTO_DESCRIPCION, CONTACTO, valueDescription)
				&& isValid;

		if (isValid && TipoMedioContacto.EMAIL.equals(contacto.getTipoMedioContacto())
				&& contacto.getDescripcion() != null) {
			isValid = EMailValidator.validate(contacto.getDescripcion(), result);
		}
		return isValid;
	}

	private static final String CONTACTO = "contacto";

	private static final String DENOMINACION = "Denominación";

	private static final String DESCRIPCION = "Descripción";

	private static final String TIPO_CONTACTO = "Tipo de Contacto";
}