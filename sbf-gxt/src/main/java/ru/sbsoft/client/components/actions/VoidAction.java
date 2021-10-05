package ru.sbsoft.client.components.actions;

import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 *
 * @author vlki
 */
public class VoidAction extends AbstractAction{

    public VoidAction(ILocalizedString caption, ImageResource icon16, ImageResource icon24) {
        super(caption, icon16, icon24);
    }

    public VoidAction(ILocalizedString caption, ILocalizedString toolTip, ImageResource icon16, ImageResource icon24) {
        super(caption, toolTip, icon16, icon24);
    }

    public VoidAction(String caption, ImageResource icon16, ImageResource icon24) {
        super(caption, caption, icon16, icon24);
    }
    
    public VoidAction(String caption, String toolTip, ImageResource icon16, ImageResource icon24) {
        super(caption, toolTip, icon16, icon24);
    }

    @Override
    protected void onExecute() {
        // No default action
    }
    
}
