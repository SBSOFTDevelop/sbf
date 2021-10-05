package ru.sbsoft.generator.api.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация помечает классы (наследующие 
 * {@link ru.sbsoft.meta.sql.SQLBuilder}), которые обрабатываются генератором {@link ru.sbsoft.generator.BrowserManagerGenerator} <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AppSQLBuilder {
}
