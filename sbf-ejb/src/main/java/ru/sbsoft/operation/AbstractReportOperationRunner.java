package ru.sbsoft.operation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import ru.sbsoft.common.IO;
import ru.sbsoft.common.SystemProperty;
import ru.sbsoft.dao.JdbcWorkExecutor;
import ru.sbsoft.dao.SqlExecutor;
import ru.sbsoft.dao.report.ICustomReportDao;
import ru.sbsoft.generator.api.Lookup;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.api.i18n.consts.SBFReportStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.CustomReportParamModel;
import ru.sbsoft.shared.param.ParamInfo;

/**
 * Операция формирования пользовательского отчета
 *
 * @author sokolov
 */
public abstract class AbstractReportOperationRunner extends BaseOperationRunner {

    private final String JASPER_SERVER_ID = "ru.sbsoft.jasperserver.url";
    private final String JASPER_SERVER_USR = "ru.sbsoft.jasperserver.user";
    private final String JASPER_SERVER_PWD = "ru.sbsoft.jasperserver.password";
    private final String JASPER_API_URL = "rest_v2/reports";
    private final String PARAM_ROW_ID = "ROW_ID";
    private final String PARAM_USER = "j_username";
    private final String PARAM_PWD = "j_password";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Lookup
    ICustomReportDao reportDao;

    @Override
    public void run() throws Exception {
        SystemProperty jserverPropertyUrl = new SystemProperty(JASPER_SERVER_ID, null);
        if (null == jserverPropertyUrl.getParameterValue()) {
            throw new ApplicationException(SBFReportStr.msgJasperServerNoSet, JASPER_SERVER_ID);
        }
        SystemProperty jserverPropertyUser = new SystemProperty(JASPER_SERVER_USR, null);
        if (null == jserverPropertyUser.getParameterValue()) {
            throw new ApplicationException(SBFReportStr.msgJasperServerNoUsr, JASPER_SERVER_USR);
        }
        SystemProperty jserverPropertyPwd = new SystemProperty(JASPER_SERVER_PWD, null);
        if (null == jserverPropertyPwd.getParameterValue()) {
            throw new ApplicationException(SBFReportStr.msgJasperServerNoPwd, JASPER_SERVER_PWD);
        }
        BigDecimal reportId = getBigDecimalParam(ParamInfo.REPORT_ID);
        if (null == reportId) {
            throw new ApplicationException(SBFReportStr.msgReportParameterError, ParamInfo.REPORT_ID);
        }
        CustomReportInfo reportInfo = reportDao.getCustomReport(reportId);
        if (null == reportInfo) {
            throw new ApplicationException(SBFReportStr.msgReportEntityError, reportId.toString());
        }
        String fileType = getStringParam(ParamInfo.REPORT_FORMAT);
        if (null == fileType) {
            throw new ApplicationException(SBFReportStr.msgReportParameterError, ParamInfo.REPORT_FORMAT);
        }

        final List<ReportParameter> params = new ArrayList<>();
        BigDecimal idRow = getSelectedRecord();
        if (idRow != null) {
            params.add(new ReportParameter(PARAM_ROW_ID, idRow.toString()));
        }

        if (reportInfo.getParams() != null) {
            reportInfo.getParams().forEach(m -> {
                ReportParameter p = modelToParameter(m);
                if (p != null) {
                    params.add(p);
                }
            });
        }

        final List<ReportParameter> sqlParams = getParamsFromHeaderSQL(reportInfo, idRow);
        if (sqlParams != null) {
            params.addAll(sqlParams);
        }

        params.add(new ReportParameter(PARAM_USER, jserverPropertyUser.getParameterValue()));
        params.add(new ReportParameter(PARAM_PWD, jserverPropertyPwd.getParameterValue()));

        String stRequest = buildUrl(jserverPropertyUrl.getParameterValue(), reportInfo.getReportPath(), fileType, params);

        info(getLocaleResource(SBFReportStr.msgCreateReport));
        try (CloseableHttpClient client = HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build()) {
            HttpGet request = new HttpGet(stRequest);
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int status = statusLine.getStatusCode();
            if (status != HttpStatus.SC_OK) {
                throw new ApplicationException(SBFReportStr.msgReportRunError, String.valueOf(status), statusLine.getReasonPhrase());
            }
            try (InputStream in = response.getEntity().getContent()) {
                File file = File.createTempFile("jasperreport-", null);
                try {
                    int read;
                    byte[] bytes = new byte[8192];
                    file.deleteOnExit();
                    try (OutputStream out = new FileOutputStream(file)) {
                        while ((read = in.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                        }
                    }
                    saveExport(file, getReportName(reportInfo.getReportPath()) + "." + fileType.toLowerCase());
                } finally {
                    IO.delete(file);
                }
            }
        }
    }

    private List<ReportParameter> getParamsFromHeaderSQL(CustomReportInfo reportInfo, BigDecimal idRow) {
        try {
            if (null == reportInfo.getHeaderSQL() || reportInfo.getHeaderSQL().trim().length() == 0) {
                return null;
            }
            List<Object> sqlParams = new ArrayList<>();
            String[] parts = reportInfo.getHeaderSQL().split("\\s");
            for (int i = 0; i < parts.length; i++) {
                String st = parts[i];
                if (st.startsWith(":")) {
                    String paramName = st.substring(1);
                    sqlParams.add(getParameterValue(paramName, reportInfo, idRow));
                    parts[i] = "?";
                }
            }
            Object[] params;
            if (sqlParams.isEmpty()) {
                params = new Object[0];
            } else {
                params = sqlParams.toArray();
            }
            StringBuilder sb = new StringBuilder();
            for (String p : parts) {
                if (!p.isEmpty()) {
                    sb.append(p);
                    sb.append(" ");
                }
            }

            SqlExecutor executor = new SqlExecutor(new JdbcWorkExecutor(em), sb.toString(), params);
            return executor.exec(rs -> {
                List<ReportParameter> result = new ArrayList<>();
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        if (metaData.getColumnType(i) == Types.DATE) {
                            result.add(new ReportParameter(metaData.getColumnName(i).toUpperCase(), dateFormat.format(rs.getDate(metaData.getColumnName(i)))));
                        }
                        if (metaData.getColumnType(i) == Types.TIMESTAMP) {
                            result.add(new ReportParameter(metaData.getColumnName(i).toUpperCase(), dateTimeFormat.format(rs.getDate(metaData.getColumnName(i)))));
                        }
                        result.add(new ReportParameter(metaData.getColumnName(i).toUpperCase(), rs.getString(metaData.getColumnName(i))));
                    }
                }
                return result;
            });
        } catch (SQLException ex) {
            throw new ApplicationException(SBFReportStr.msgReportHeaderExecError, ex.getLocalizedMessage());
        }
    }

    private Object getParameterValue(String paramName, CustomReportInfo reportInfo, BigDecimal idRow) {
        if (PARAM_ROW_ID.equalsIgnoreCase(paramName)) {
            return idRow;
        }
        return getParameterValue(paramName, reportInfo);
    }

    private Object getParameterValue(String paramName, CustomReportInfo reportInfo) {
        for (CustomReportParamModel pm : reportInfo.getParams()) {
            if (paramName.equalsIgnoreCase(pm.getCode())) {
                switch (pm.getParamType()) {
                    case STRING:
                        return getStringParam(paramName);
                    case LONG:
                        return getLongParam(paramName);
                    case DATE:
                        Date dateV = getDateParam(paramName);
                        return dateV != null ? new java.sql.Date(dateV.getTime()) : null;
                    default:
                        throw new ApplicationException("Unknown parameter type {1}", pm.getParamType().getCode());
                }
            }
        }
        throw new ApplicationException(SBFReportStr.msgReportHeaderParameterError, paramName);
    }

    private String buildUrl(String url, String reportPath, String fileType, List<ReportParameter> params) {
        StringBuilder sb = new StringBuilder(url);
        if (!url.endsWith("/")) {
            sb.append("/");
        }
        sb.append(JASPER_API_URL);
        if (!reportPath.startsWith("/")) {
            sb.append("/");
        }
        sb.append(reportPath)
                .append(".")
                .append(fileType)
                .append("?");
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(params.get(i).getName())
                        .append("=")
                        .append(params.get(i).getURLValue());
            }
        }
        return sb.toString();
    }

    private String getReportName(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    private ReportParameter modelToParameter(CustomReportParamModel model) {
        switch (model.getParamType()) {
            case STRING:
                String sv = getStringParam(model.getCode());
                return sv != null ? new ReportParameter(model.getCode(), sv) : null;
            case LONG:
                Long lv = getLongParam(model.getCode());
                return lv != null ? new ReportParameter(model.getCode(), String.valueOf(lv)) : null;
            case DATE:
                YearMonthDay ymdv = getYearMonthDayParam(model.getCode());
                return ymdv != null ? new ReportParameter(model.getCode(), ymdParamFormat(ymdv)) : null;
            default:
                throw new ApplicationException("Unknown parameter type {1}", model.getParamType().getCode());
        }
    }

    private String ymdParamFormat(YearMonthDay ymd) {
        StringBuilder sb = new StringBuilder(String.valueOf(ymd.getYear()))
                .append("-");
        String st = String.valueOf(ymd.getMonth());
        if (st.length() < 2) {
            sb.append("0");
        }
        sb.append(st);
        sb.append("-");
        st = String.valueOf(ymd.getDay());
        if (st.length() < 2) {
            sb.append("0");
        }
        sb.append(st);
        return sb.toString();
    }

    private static class ReportParameter {

        private final String name;
        private final String value;

        public ReportParameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getURLValue() {
            try {
                return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException ex) {
                throw new ApplicationException(SBFReportStr.msgReportURLEncodingError, value);
            }
        }

    }

}
