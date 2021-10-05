package ru.sbsoft.generator.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ставит метку о том, что прикладная аннотация отмечает динамические обработчики форм. 
 * Прикладная аннотация должна быть вида
 * <pre>
 * &#64;Retention(RetentionPolicy.CLASS)
 * &#64;Target({ElementType.TYPE, ElementType.METHOD})
 * &#64; DynFormProcessorAnnotation
 * &#09;public &#64;interface FormProcessor {
 * &#09;FormEnum value();
 * }
 * </pre> , где DynamicFormEnum - перечисление динамических форм в вашей подсистеме.
 * @author sokolov
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DynFormProcessorAnnotation {
    
}
