package ru.sbsoft.system.dao.common;

import java.io.File;
import java.io.IOException;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import ru.sbsoft.common.ResourceNotFoundException;
import ru.sbsoft.common.ServerConfig;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.EMailMessage;
import ru.sbsoft.shared.EMailResult;
import ru.sbsoft.dao.IEMailDao;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Реализует функционал отправки EMail.
 *
 * @author sokoloff
 */
@Stateless
@Remote(IEMailDao.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@PermitAll
public class EMailDaoBean implements IEMailDao {

    /*  EMailDaoBean is changed to get default mail Session from ServerConfig rather than @Resource tag. 
    Because of unability to config mail jndi-name for ear on wildfly. 
    ServerConfig need ru.sbsoft.mail.full_jndi-name environment parameter to be set.
     */
    private final Session sessionMail;

    @EJB
    private Ii18nDao i18nDao;

    public EMailDaoBean() {
        this(getSession());
    }

    public EMailDaoBean(Session sessionMail) {
        this.sessionMail = sessionMail;
    }

    public EMailDaoBean(Session sessionMail, Ii18nDao i18nDao) {
        this(sessionMail);
        this.i18nDao = i18nDao;
    }

    private static Session getSession() {
        try {
            return ServerConfig.getDefaultMailSession();
        } catch (ResourceNotFoundException ex) {
            throw new EJBException("Can't get default mail session", ex);
        }
    }

    @Override
    public EMailResult sendMessage(final EMailMessage message, File... files) {
        return sendMessage("en", message, files);
    }

    @Override
    public EMailResult sendMessage(final String errorLocale, final EMailMessage message, File... files) {
        EMailResult result = new EMailResult(EMailResult.ResultEnum.OK, null);

        try {
            if (null == Strings.clean(message.getAddressTo())) {
                result.setResult(EMailResult.ResultEnum.ERROR);
                if (i18nDao != null) {
                    result.setError(i18nDao.get(errorLocale, SBFGeneralStr.msgEMailNotRecipient));
                } else {
                    result.setError("EMail: There is no Recipient provided");
                }
                return result;
            }
            MimeMessage msg = new MimeMessage(sessionMail);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(message.getAddressTo()));
            if (null != Strings.clean(message.getAddressFrom())) {
                msg.setFrom(message.getAddressFrom());
            }
            if (message.isFlagHiPriority()) {
                msg.addHeader("X-Priority", "1");
            }
            if (message.isFlagRecept()) {
                msg.addHeader("Return-Receipt-To", "DSN=1");
            }
            if (message.isFlagRead()) {
                msg.addHeader("Disposition-Notification-To", message.getAddressFrom());
            }

            msg.setSubject(message.getSubject(), "UTF-8");
            MimeMultipart multipart = new MimeMultipart();
            msg.setContent(multipart);

            if (null != Strings.clean(message.getBody())) {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(message.getBody(), "UTF-8");
                multipart.addBodyPart(textPart);
            }

            for (File file : files) {
                MimeBodyPart filePart = new MimeBodyPart();
                filePart.attachFile(file);
                multipart.addBodyPart(filePart);
            }

            Transport.send(msg);
        } catch (MessagingException ex) {
            result.setResult(EMailResult.ResultEnum.ERROR);
            String mes = ex.getMessage();
            if (null != ex.getNextException()) {
                mes = mes + "\n" + ex.getNextException().getMessage();
            }
            result.setError(mes);

        } catch (IOException ex) {
            result.setResult(EMailResult.ResultEnum.ERROR);
            result.setError(ex.getMessage());
        }

        return result;
    }

}
