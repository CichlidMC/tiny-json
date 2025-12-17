package fish.cichlidmc.tinyjson.test.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// May be applied to a class containing tests to indicate a folder that [Resource]s should be searched for in.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Group {
	String value();
}
