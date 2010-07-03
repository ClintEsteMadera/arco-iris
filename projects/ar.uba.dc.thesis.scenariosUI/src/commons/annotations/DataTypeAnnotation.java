package commons.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Anotacion de tipo de dato
 *  
 * @author ppastorino
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.METHOD}) 
public @interface DataTypeAnnotation {
	String typeId();
}
