package commons.validation;

import java.lang.annotation.Annotation;

/**
 * Handler para una validacion basada en anotaciones.
 * 
 * 
 */
public interface ValidationHandler<T extends Annotation> {
	public void validate(Object value, T a, ValidationResult result, ValidationContext context);
}
