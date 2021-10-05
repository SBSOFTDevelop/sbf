package ru.sbsoft.common.server;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author vlki
 */
public final class ServletUtl {

    public static URI getServerUrl(HttpServletRequest req) {
        StringBuffer url = req.getRequestURL();
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();
        try {
            //return new URL(url.substring(0, url.length() - uri.length() + ctx.length())).toURI().normalize();
            return new URL(url.substring(0, url.length() - uri.length())).toURI().normalize();
        } catch (MalformedURLException|URISyntaxException ex) {
            throw new RuntimeException("Unexpected: fail to create URL from HttpServletRequest", ex);
        }
    }

    private ServletUtl() {
    }
}
