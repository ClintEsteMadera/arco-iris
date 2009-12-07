package commons.gui.model;

/**
 * Helper para propiedades anidadas.
 * 
 * @author ppastorino
 */
class PropertyName 
{

	public PropertyName(Object name) {
		this.propertyName = name;
	}

	
	public Object getPropertyName() {
		return propertyName;
	}

	/**
	 * Determina si la propiedad está "incluida" o es igual a otra propiedad 
	 * 
	 * Ejemplos
	 * 
	 * "emisor.denominacion" está incluida en "emisor"
	 * "emisor" está incluida en "emisor"
	 * 
	 * @param parentProperty
	 * @return 
	 */
	public boolean isChildOf(Object parentProperty){
		if(!(this.propertyName instanceof String)){
			return false;
		}
		String fullName=parentProperty instanceof String ? (String)parentProperty : null;
		final String s=getChildProperty((String)this.propertyName,fullName);
		return s != null;
	}
	
	/**
	 * Determina si la propiedad está "incluida" en otra propiedad
	 * 
	 * Ejemplos
	 * 
	 * "emisor.denominacion" está incluida en "emisor"
	 * "titulo.persona.denominacion" está incluida en "titulo" o en "titulo.persona"
	 * 
	 * @param parentProperty
	 * @return 
	 */
	public boolean isContainedIn(Object parentProperty){
		if(!(this.propertyName instanceof String)){
			return false;
		}
		String fullName=parentProperty instanceof String ? (String)parentProperty : null;
		final String s=getChildProperty((String)this.propertyName,fullName);
		return s != null && s.length() > 0;
	}
	
	
	/**
	 * Obtiene la propiedad tal como se representa en el modelo "anidado"
	 *  
	 * @param fullPropertyKey
	 * @return
	 */
	public String convertToChildProperty(Object fullPropertyKey) {
		String fullPropertyName=fullPropertyKey instanceof String ? (String)fullPropertyKey : null;
		return getChildProperty(fullPropertyName,(String)this.propertyName);
	}

	/**
	 * Obtiene la propiedad tal como se identifica en el modelo "padre" que contiene 
	 * a la propiedad
	 * 
	 * @param key
	 * @return
	 */
	public String convertToParentProperty(Object key) {
		if(("").equals(key)){
			return this.propertyName.toString();
		}
		return this.propertyName + "." + key;
	}


	private String getChildProperty(String fullPropertyName,String nestedPropertyName) {
		final String emptyString = "";

		if (fullPropertyName == null) {
			return emptyString;
		}

		if (emptyString.equals(fullPropertyName)) {
			return emptyString;
		}

		//
		// si la propiedad del padre no comienza con la propiedad anidada
		// entonces la propiedad no esta incluida en el modelo anidado
		//
		if (!fullPropertyName.startsWith(nestedPropertyName)) {
			return null;
		}

		final int rootL = nestedPropertyName.length();
		final int parentL = fullPropertyName.length();

		if (parentL == rootL) {
			return emptyString;
		}

		if (parentL < rootL + 2 || fullPropertyName.charAt(rootL) != '.') {
			return null;
		}

		return fullPropertyName.substring(rootL + 1);
	}

	
	private Object propertyName;
}
