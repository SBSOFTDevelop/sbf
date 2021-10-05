package ru.sbsoft.server.services;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import ru.sbsoft.common.IO;
import ru.sbsoft.common.Strings;
import ru.sbsoft.dao.IStorageDao;
import ru.sbsoft.shared.services.ServiceConst;

/**
 * Сервлет загрузки файла (ов) на сервер в ситемное хранилище через google gwtupload компонент 
 * 
 */



@WebServlet(
        urlPatterns = {"/" + ServiceConst.UPLOAD_OPERATION_SERVICE + "/*"},
        initParams = {
            @WebInitParam(name = "maxSize", value = "1073741824"),
            @WebInitParam(name = "slowUploads", value = "1")
        })
public class UploadOperationService extends UploadAction {

    @EJB
    private IStorageDao storageDao;

    @Override
    public String executeAction(final HttpServletRequest request, final List<FileItem> sessionFiles) throws UploadActionException {
        if (null == sessionFiles || sessionFiles.isEmpty()) {
            throw new UploadActionException("Отсутствуют файлы");
        }
        List<String> results = new ArrayList();
        try {
            for (final FileItem item : sessionFiles) {
                DiskFileItem diskFileItem = (DiskFileItem) item;
                File file = null;
                try {
                    Long id = storageDao.save(request.getRemoteUser(), extractAlias(item),  new File(diskFileItem.getName()).getName(), file = getFile(diskFileItem));
                    results.add(id.toString());
                } finally {
                    IO.delete(file);
                }
            }
        } catch (IOException ex) {
            throw new UploadActionException("Ошибка сохранения файлов", ex);
        } finally {
            // removeSessionFileItems(request, true);
        }
        return Strings.join(results.toArray(), ",");
    }

    private String extractAlias(final FileItem item) {
        String alias = item.getFieldName();
        int n = alias.indexOf('-');
        return (n == -1) ? alias : alias.substring(0, n);
    }

   
    private File getFile(DiskFileItem item) throws IOException {
        if (!item.isInMemory()) {
            return item.getStoreLocation();
        }
        File f = File.createTempFile("upload", null);
        IO.copy(item.getInputStream(), f);
        return f;
    }
}
