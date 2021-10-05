package ru.sbsoft.generator.api.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Ставит метку о том, что прикладная аннотация отмечает обработчики операций.
 Прикладная аннотация должна быть вида 
 <pre>
 &#64;Retention(RetentionPolicy.CLASS)
 &#64;Target({ElementType.TYPE, ElementType.METHOD})
 &#64;OperationProcessorAnnotation
 &#09;public &#64;interface OperationProcessor {
 &#09;OperationEnum value();
 }
 </pre>
 , где OperationEnum - перечисление операций в вашей подсистеме.
 @author Fedor Resnyanskiy, SBSOFT
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GridTemplateAnnotation {

}
