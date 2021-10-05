package ru.sbsoft.generator.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ставит метку о том, что прикладная аннотация отмечает обработчика дерева.
 * Прикладная аннотация должна быть вида
 * <pre>
 * &#64;Retention(RetentionPolicy.CLASS)
 * &#64;Target({ElementType.TYPE})
 * &#64;TreeProcessorAnnotation
 * &#09;public &#64;interface TreeProcessor {
 * &#09;TreeEnum value();
 * }
 * </pre> , где TreeEnum - перечисление форм в вашей подсистеме.
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface TreeProcessorAnnotation {
}
