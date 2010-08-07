package commons.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Directorio que contiene los diccionarios de datos activos
 */
public class DataTypeDictionaryDirectory {

	public DataTypeDictionaryDirectory() {
		super();
	}

	public void register(DataTypeDictionary dictionary) {
		dictionaries.add(dictionary);
	}

	@SuppressWarnings("unchecked")
	public boolean validate(Object value, String id, ValidationResult result) {
		Validator validator = null;
		for (DataTypeDictionary dict : this.dictionaries) {
			validator = dict.getTypeValidator(id);

			if (validator != null) {
				break;
			}
		}

		if (validator == null) {
			throw new IllegalArgumentException("No se registró validador para el tipo '" + id + "'");
		}

		return validator.validate(value, result);
	}

	private List<DataTypeDictionary> dictionaries = new ArrayList<DataTypeDictionary>();

}