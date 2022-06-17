package ru.sbsoft.client.schedule;

import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;

/**
 *
 * @author Sokoloff
 */
public class MaskI18nCommand extends SyncSchedulerChainCommand {

    private final Component component;
    private final I18nResourceInfo resourceInfo;

    public MaskI18nCommand(Component component, I18nResourceInfo resourceInfo) {
        this.component = component;
        this.resourceInfo = resourceInfo;
    }

    @Override
    protected void onCommand() {
        component.mask(I18n.get(resourceInfo));
    }

}

