package fish.cichlidmc.tinyjson.test.framework;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a String parameter should be read from a file.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ResourceExtension.class)
public @interface Resource {
	/**
	 * @return the name of the file to read. If empty, it will be inferred from the test name.
	 */
	String value() default "";

	String extension() default ".json";
}
