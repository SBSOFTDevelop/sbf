package ru.sbsoft.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.client.components.dialog.ChangePasswdDialog;
import ru.sbsoft.client.schedule.SchedulerChainCommand;
import ru.sbsoft.shared.model.user.UserEnv;

/**
 *
 * @author sychugin
 */
public class GetUserEnvCommand extends SchedulerChainCommand {

    private final Component component;

    public GetUserEnvCommand(Component component) {
        this.component = component;
    }

    private void next() {
        getChainManager().next();
    }

    @Override
    public void execute() {
        RoleCheker.reload(new AsyncCallback<UserEnv>() {
            @Override
            public void onFailure(Throwable caught) {
                ClientUtils.alertException(caught);
            }

            @Override
            public void onSuccess(UserEnv result) {
                if (result.getPpolicy().isExpired()) {
                    component.unmask();
                    new ChangePasswdDialog(result.getPpolicy(), false, () -> {
                        next();
                    }).show();
                } else {
                    next();
                }
            }
        });
    }
}
