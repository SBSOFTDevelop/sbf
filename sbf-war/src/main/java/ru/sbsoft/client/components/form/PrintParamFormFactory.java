package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.form.handler.ReportFormatHandler;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.FileFormat;

/**
 *
 * @author Kiselev
 */
public abstract class PrintParamFormFactory extends ParamFormFactory {

    public static PrintParamFormFactory getTrivial() {
        return new PrintParamFormFactory() {
            @Override
            protected void fillHandlers(ParamHandlerCollector h, ParamForm f) {
                //no additional handlers
            }
        };
    }

    private final FileFormat[] formats;

    public PrintParamFormFactory() {
        this((OperationType) null);
    }

    public PrintParamFormFactory(OperationType operationType) {
        this(operationType, (FileFormat[]) null);
    }

    public PrintParamFormFactory(FileFormat... formats) {
        this(null, formats);
    }

    public PrintParamFormFactory(OperationType operationType, FileFormat... formats) {
        super(operationType);
        this.formats = formats;
    }

    @Override
    protected ParamForm createParamFormInstance() {
        return new PrintParamForm();
    }

    protected class PrintParamForm extends ParamForm {

        @Override
        protected void afterAddHandlers(ParamHandlerCollector hc) {
            if (formats != null && formats.length > 0) {
                hc.add(new ReportFormatHandler(formats));
            } else {
                hc.add(new ReportFormatHandler());
            }
        }

    }
}
