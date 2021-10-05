package ru.sbsoft.server.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.ejb.EJB;
import javax.mail.internet.MimeUtility;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.sbsoft.common.IO;
import ru.sbsoft.dao.IStorageDao;
import ru.sbsoft.model.StorageItem;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {"/" + ServiceConst.DOWNLOAD_OPERATION_SERVICE + "/*"})
public class DownloadOperationService extends HttpServlet {

    private final static String CHAR_SET = "utf-8";
    private final static String GECKO_BROWSER = "gecko";

    @EJB
    private IStorageDao storageDao;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        request.setCharacterEncoding(CHAR_SET);

        final String fileIdString = request.getParameter(ServiceConst.FILE_ID_PARAM);
        final long fileId = Long.parseLong(fileIdString);
        
        final StorageItem fileInfo = storageDao.getStorageItem(fileId);
        final File file = storageDao.load(fileId);
        final int size = (int) file.length();
        final String browser = request.getParameter(ServiceConst.BROWSER_PARAM);

        response.setContentLength(size);
        response.setHeader("Content-Disposition", "attachment; filename=" + getEncodedFileName(fileInfo.getFileName(), browser.toLowerCase()));
        response.setContentType("application/octet-stream");

        FileInputStream src = null;
        try {
            IO.copy(src = new FileInputStream(file), response.getOutputStream());
        } finally {
            IO.close(src);
            IO.delete(file);
        }
    }

    private String getEncodedFileName(final String fileName, final String browser) throws UnsupportedEncodingException {
        if (browser.contains(GECKO_BROWSER) && !browser.contains("like gecko")) {
            return MimeUtility.encodeText(fileName, CHAR_SET, null).replaceAll(" ", "_");
        }
        return URLEncoder.encode(fileName, CHAR_SET).replaceAll("\\+", "_");
    }
}
