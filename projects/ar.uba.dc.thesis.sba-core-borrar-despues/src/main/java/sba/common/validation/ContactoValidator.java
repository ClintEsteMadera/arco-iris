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
 * <code>== null</code> � bien, a aquel objeto Domicilio no nulo que posee todas sus propiedades
 * nulas.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.6 $ $Date: 2008/04/10 18:31:53 $
 */

public abstract class ContactoValidator implements Serializable {

	/**
	 * Valida el la lista de Contactos de acuerdo a las siguientes reglas: Si la lista es vac�a y el
	 * par�metro <code>emptyListAllowed</code> es <code>true</code>, la lista de Contactos se
	 * considera v�lida. Sino, para cada uno de los contactos, invoca al m�todo
	 * {@link #isValid(Contacto, ValidationResult)} juntando todos los resultados intermedios en el
	 * par�metro <code>result</code>.
	 * 
	 * @param contactos
	 *            una lista NO NULA de Contactos
	 * @param result
	 *            el objeto al cual a�adir errores en caso de haberlos
	 * @param emptyListAllowed
	 *            especifica si se permite que la lista sea vac�a
	 * @return <code>true</code>, si� o bien la lista es vac�a y <code>emptyListAllowed</code>
	 *         es <code>true</code> o bien, la lista es no vac�a y todos los Contactos son
	 *         v�lidos, de acuerdo al m�todo {@link #isValid(Contacto, ValidationResult)}.
	 */
	public static boolean isValid(List<Contacto> contactos, ValidationResult result,
			boolean emptyListAllowed) {
		// NOTA IMPORTANTE: NO cambiar el orden de evaluaci�n de los "&&"!!
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
	 * <li> Denominaci�n - Obligatorio � 255 caracteres alfanum�ricos
	 * <li> Tipo de Contacto - Obligatorio
	 * <li> Tipo de Medio de Contacto - Obligatorio
	 * <li> Descripci�n - Obligatoria � 255 caracteres alfanum�ricos
	 * </ul>
	 * 
	 * @param contacto
	 *            el Contacto a validar
	 * @param result
	 *            el objeto al cual a�adir errores en caso de haberlos
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

	private static final String DENOMINACION = "Denominaci�n";

	private static final String DESCRIPCION = "Descripci�n";

	private static final String TIPO_CONTACTO = "Tipo de Contacto";
}