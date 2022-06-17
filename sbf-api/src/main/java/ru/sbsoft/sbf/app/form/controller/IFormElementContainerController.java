package ru.sbsoft.sbf.app.form.controller;

import ru.sbsoft.sbf.app.form.IWritable;
import ru.sbsoft.sbf.app.form.IValidatable;
import ru.sbsoft.sbf.app.form.IReadable;
import ru.sbsoft.sbf.app.form.IFormElementContainer;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL>
 */
public interface IFormElementContainerController<MODEL> extends
        IFormElementContainer,
        IWritable<MODEL>,
        IReadable<MODEL>,
        IValidatable {

}
