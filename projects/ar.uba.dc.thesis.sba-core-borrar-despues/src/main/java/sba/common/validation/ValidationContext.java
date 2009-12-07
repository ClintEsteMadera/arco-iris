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
 * $Id: ValidationContext.java,v 1.1 2008/05/09 14:05:29 cvspasto Exp $
 */

package sba.common.validation;

import java.util.Stack;

import sba.common.properties.EnumPropertyDirectory;

/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/05/09 14:05:29 $
 */

public class ValidationContext {

	public ValidationContext() {
		super();
		this.validationRegistry = new ValidationRegistry();
		this.validationStack=new Stack();
	}

	public DataTypeDictionaryDirectory getDataTypeDictionaryDirectory() {
		return dataTypeDictionaryDirectory;
	}

	public void setDataTypeDictionaryDirectory(
			DataTypeDictionaryDirectory dataTypeDictionaryDirectory) {
		this.dataTypeDictionaryDirectory = dataTypeDictionaryDirectory;
	}

	public EnumPropertyDirectory getEnumPropertiesDirectory() {
		return enumPropertiesDirectory;
	}

	public void setEnumPropertiesDirectory(EnumPropertyDirectory enumPropertiesDirectory) {
		this.enumPropertiesDirectory = enumPropertiesDirectory;
	}

	@SuppressWarnings("unchecked")
	public void addGenericValidator(Class cl) {
		this.addValidator(cl, new GenericValidator(cl, this));
	}

	@SuppressWarnings("unchecked")
	public void validate(Object value, ValidationResult result) {
		Validator validator = this.getValidatorFor(value.getClass());

		if (validator != null) {
			this.pushValidationStack(value);
			
			validator.validate(value, result);
			
			this.popValidationStack();
		}
	}

	@SuppressWarnings("unchecked")
	public void addValidator(Class cl,Validator validator){
		Validator v = this.getValidatorFor(cl);
		
		if(v == null){
			this.validationRegistry.addValidator(cl, validator);
		}else{
			this.validationRegistry.addValidator(cl, new CompositeValidator(v,validator));
		}
	}
	
	public Validator getValidatorFor(Class cl){
		try {
			return (Validator) this.validationRegistry.getValidatorFor(cl);
		} catch (IllegalArgumentException e) {
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	void pushValidationStack(Object o){
		this.validationStack.push(o);
	}

	Object popValidationStack(){
		return this.validationStack.pop();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObjectFromStack(Class<T> clazz){
		int size=this.validationStack.size();
		for(int i=size-1;i>=0;i--){
			final Object item=this.validationStack.get(i);
			if(clazz.isAssignableFrom(item.getClass())){
				return (T)item;
			}
		}
		return null;
	}

	private DataTypeDictionaryDirectory dataTypeDictionaryDirectory;

	private EnumPropertyDirectory enumPropertiesDirectory;

	private ValidationRegistry validationRegistry;
	
	private Stack validationStack;
}
