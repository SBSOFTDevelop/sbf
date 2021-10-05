package ru.sbsoft.dao;

import java.io.File;
import ru.sbsoft.shared.EMailMessage;
import ru.sbsoft.shared.EMailResult;

/**
 * @see EMailDaoBean
 * @author sokoloff
 */
public interface IEMailDao {
    
    EMailResult sendMessage(EMailMessage message, File... files);
    
    EMailResult sendMessage(String errorLocale, EMailMessage message, File... files);
    
}
