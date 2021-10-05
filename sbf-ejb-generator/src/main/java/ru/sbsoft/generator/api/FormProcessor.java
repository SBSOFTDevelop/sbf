package ru.sbsoft.generator.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация помечает классы (реализующие интерфейс 
 * {@link ru.sbsoft.form.IFormProcessor}), которые обрабатываются генератором {@link ru.sbsoft.generator.FormDaoBeanGenerator} <br>
 * для формирования класса <i>ru.sbsoft.common.CommonFormDaoBean</i>.
 * 
 *<p> В качестве параметра <b>value</b> передается имя контекста клиентской формы.
 *<p> В качестве параметра <b>rights</b> передается имя таблицы СУБД.
 *  
 * <p>
 * Например:
 * <pre>
 *    
 * &#064;FormProcessor(value = NewFormEnum.INSPECTION_CHECK_VID_AMBULANCE_EDIT_FORM, rights = "DX_INSPECTION")
 * 
 * </pre>
 * 
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface FormProcessor {

    
    String value();

    String rights() default "";
}
