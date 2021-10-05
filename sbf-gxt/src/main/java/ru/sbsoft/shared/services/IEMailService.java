package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.sbsoft.shared.EMailMessage;
import ru.sbsoft.shared.EMailResult;

/**
 *
 * @author Sokoloff
 */
@RemoteServiceRelativePath(ServiceConst.EMAIL_SERVICE_SHORT)
public interface IEMailService extends SBFRemoteService {

    EMailResult sendMessage(final String errLocale, final EMailMessage message);
    
}
