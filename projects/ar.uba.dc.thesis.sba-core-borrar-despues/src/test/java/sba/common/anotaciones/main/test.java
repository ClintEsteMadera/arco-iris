package sba.common.anotaciones.main;

import sba.common.annotations.AnnotationManager;
import sba.common.anotaciones.domain.Domain;
import sba.common.anotaciones.domain.Empleado;
import sba.common.properties.EnumPropertyDirectory;
import sba.common.validation.CommonDataTypes;
import sba.common.validation.DataTypeDictionaryDirectory;
import sba.common.validation.ValidationContext;
import sba.common.validation.ValidationResult;

public class test {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		Domain domain = new Domain();

		ValidationContext context = new ValidationContext();

		// recursos para describir las propiedades
		EnumPropertyDirectory propDir = new EnumPropertyDirectory();
		propDir.register(Constants.class);

		// diccionarios de tipos de datos para validar
		DataTypeDictionaryDirectory typeDir = new DataTypeDictionaryDirectory();
		typeDir.register(CommonDataTypes.getCommonTypes());

		context.setDataTypeDictionaryDirectory(typeDir);
		context.setEnumPropertiesDirectory(propDir);

		// Registro de Validacion custom
		AnnotationManager.getInstance().registerValidationHandler(ValidateTipoEmpleado.class,
				new ValidateTipoEmpleadoHandler());

		// validadores
		// TODO: se podrían "descubrir" por reflection los validadores anidados en base a
		// la clase raiz
		context.addGenericValidator(Empleado.class);
		// se pueden combinar el validador generico/declarativo y uno "programatico"
		context.addValidator(Domain.class, new DomainValidator(context));

		ValidationResult result1 = new ValidationResult();
		domain.setDescription(null);
		context.validate(domain, result1);
		System.out.println("result 1 " + result1.getErrorMessage());

		final StringBuilder descripcionLarga = new StringBuilder();
		for (int i = 0; i < 2000; i++) {
			descripcionLarga.append("A");
		}
		domain.setDescription(descripcionLarga.toString());
		domain.getEmpleado().setApellido("apellido");
		domain.setTiposDeEmpleadoValidos(new String[]{"A","B"});
		domain.getEmpleado().setTipoEmpleado("C");
		ValidationResult result2 = new ValidationResult();
		context.validate(domain, result2);
		System.out.println("result 2 " + result2.getErrorMessage());

		ValidationResult result3 = new ValidationResult();
		domain.setDescription("ABC");
		context.validate(domain, result3);
		System.out.println("result 3 " + result3.getErrorMessage());

	}

}
