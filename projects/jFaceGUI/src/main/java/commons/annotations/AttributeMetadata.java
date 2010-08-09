package commons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD })
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface AttributeMetadata {

	FieldType fieldType() default FieldType.DEFAULT;

	int length() default 255;

	boolean nullable();

}