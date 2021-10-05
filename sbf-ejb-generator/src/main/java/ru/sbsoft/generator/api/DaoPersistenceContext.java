package ru.sbsoft.generator.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author sychugin
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)

public @interface DaoPersistenceContext {

    String value();
}
