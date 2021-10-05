package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.ISelectBrowserMaker;
import ru.sbsoft.client.components.form.handler.CommonLookupHandler;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class LookupHandler extends CommonLookupHandler<LookupHandler> {

    public LookupHandler(String label, NamedItem param, NamedGridType browserType, String context) {
        this(param.getCode(), label, browserType, context);
        setToolTip(I18n.get(param.getItemName()));
    }
    
    public LookupHandler(NamedItem param, NamedGridType browserType, String context) {
        this(param.getCode(), I18n.get(param.getItemName()), browserType, context);
    }
    
    public LookupHandler(String name, String label, NamedGridType browserType, String context) {
        super(name, label);
        setBrowser(browserType, context);
    }
   
    public LookupHandler(NamedItem param, ISelectBrowserMaker browserMaker) {
        this(param.getCode(), I18n.get(param.getItemName()), browserMaker);
    }
    
    public LookupHandler(String name, String label, ISelectBrowserMaker browserMaker) {
        super(name, label);
        if (browserMaker != null) {
            setBrowserMaker(browserMaker);
        }
    }
}
