package sba.common.validation;

import java.lang.annotation.Annotation;

/**
 * Handler para una validacion basada en anotaciones.
 * 
 * @author ppastorino
 */
public interface ValidationHandler<T extends Annotation> {
    public void validate(Object value, T a, ValidationResult result, ValidationContext context);
}
