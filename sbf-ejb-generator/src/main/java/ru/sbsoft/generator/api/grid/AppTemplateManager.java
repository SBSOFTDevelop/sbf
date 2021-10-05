package ru.sbsoft.generator.api.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация помечает класс (наследующий
 * {@link ru.sbsoft.dao.ITemplateManager}), который может быть использован при поиске AbstractTemplate в коде, сгенерированном {@link ru.sbsoft.generator.BrowserManagerGenerator} <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AppTemplateManager {
}
