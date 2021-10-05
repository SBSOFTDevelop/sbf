package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.shared.EMailMessage;
import ru.sbsoft.shared.EMailResult;

/**
 * @see IEmailService
 *
 * @author Sokoloff
 */
public interface IEMailServiceAsync extends ISBFServiceAsync {

    void sendMessage(final String errLocale, final EMailMessage message, AsyncCallback<EMailResult> callback);

}
