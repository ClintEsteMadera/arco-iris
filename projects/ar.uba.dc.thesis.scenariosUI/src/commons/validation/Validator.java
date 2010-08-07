package commons.validation;

/**
 * Interfaz para los validadores "simples"
 * 
 * 
 */
public interface Validator<T> {

	public boolean validate(T objectToValidate, ValidationResult result);
}
