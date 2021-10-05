package ru.sbsoft.generator.api.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация помечает класс (наследующий
 * {@link ru.sbsoft.dao.DefaultTemplateBuilder}), который обрабатывается генератором {@link ru.sbsoft.generator.BrowserManagerGenerator} <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AppTemplateBuilder {
}
