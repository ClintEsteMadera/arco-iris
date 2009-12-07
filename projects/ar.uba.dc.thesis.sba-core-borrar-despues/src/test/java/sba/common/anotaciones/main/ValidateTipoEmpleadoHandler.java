package sba.common.anotaciones.main;

import sba.common.anotaciones.domain.Domain;
import sba.common.utils.SbaStringUtils;
import sba.common.validation.ValidationContext;
import sba.common.validation.ValidationHandler;
import sba.common.validation.ValidationResult;

public class ValidateTipoEmpleadoHandler implements ValidationHandler<ValidateTipoEmpleado> {

	public void validate(Object value, ValidateTipoEmpleado a, ValidationResult result,
			ValidationContext context) {
		// obtengo el objeto "padre" que se esta validando
		final Domain domain = context.getObjectFromStack(Domain.class);

		if (domain.getTiposDeEmpleadoValidos() != null) {
			for (String t : domain.getTiposDeEmpleadoValidos()) {
				if (t.equals(value)) {
					return;
				}
			}
			result.addError(Constants.TIPO_EMPLEADO_INVALIDO, value, SbaStringUtils.toString(domain
					.getTiposDeEmpleadoValidos()));
		}
	}

}
