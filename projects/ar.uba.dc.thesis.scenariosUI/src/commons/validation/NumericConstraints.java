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
 * $Id: NumericConstraints.java,v 1.3 2008/03/18 20:29:24 cvspasto Exp $
 */

package commons.validation;

import java.math.BigDecimal;

/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/03/18 20:29:24 $
 */
public class NumericConstraints implements Validator<Number> {
		
	public NumericConstraints(int intDigits, int precision, BigDecimal max, BigDecimal min) {
		super();
		this.intDigits = intDigits;
		this.precision = precision;
		this.max = max;
		this.min = min;
	}

	public NumericConstraints(int intDigits, int precision, BigDecimal max) {
		this(intDigits, precision, max, BigDecimal.ZERO);
	}

	public NumericConstraints(int intDigits, int precision) {
		this(intDigits, precision, buildMax(intDigits, precision));
	}

	public NumericConstraints(int intDigits) {
		this(intDigits, 0);
	}

	public boolean validate(Number objectToValidate, ValidationResult result) {
		
		boolean valid=true;
		
		if(this.max != null && 
				this.max.compareTo(new BigDecimal(objectToValidate.toString())) < 0){
			result.addError(CommonValidationMessages.LESS_THAN_OR_EQUAL, result
					.getValueDescription(), this.max);
			valid=false;
		}else if(this.min != null && 
				this.min.compareTo(new BigDecimal(objectToValidate.toString())) > 0){
			result.addError(CommonValidationMessages.GREATER_THAN_OR_EQUAL, result
					.getValueDescription(), this.max);
			valid=false;
		}
		
		//TODO: validar precision
		
		return valid;
	}

	public int getIntDigits() {
		return intDigits;
	}

	public Number getMax() {
		return max;
	}

	public Number getMin() {
		return min;
	}

	public int getPrecision() {
		return precision;
	}

	private static BigDecimal buildMax(int intDigits, int precision){
		
		String s="0";
		
		if(intDigits > 0){
			s=getDigitString(intDigits);
		}
		
		if(precision > 0){
			s = s + "." +  getDigitString(precision);
		}
		
		return new BigDecimal(s);
	}
	
	private static String getDigitString(int n){
		final StringBuilder str=new StringBuilder();
		for(;n > 0;n--){
			str.append("9");
		}
		return str.toString();
	}
	
	private BigDecimal min;

	private BigDecimal max;

	private int intDigits;

	private int precision;
}
