package ru.sbsoft.generator.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для описания полей, в которые будет передана EJB инъекция.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Lookup {
    String resourceName() default "";
}
