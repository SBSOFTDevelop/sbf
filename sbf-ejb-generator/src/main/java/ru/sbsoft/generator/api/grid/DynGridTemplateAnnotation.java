package ru.sbsoft.generator.api.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ставит метку о том, что прикладная аннотация отмечает динамический шаблон грида.
 * @author sokolov
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DynGridTemplateAnnotation {
    
}
