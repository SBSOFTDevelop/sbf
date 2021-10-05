package ru.sbsoft.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.operation.BaseOperationRunner;

/**
 *
 * @author Kiselev
 */
public abstract class BaseReportRunner extends BaseOperationRunner {

    protected String templateName;

    private boolean dataVital = true;

    public BaseReportRunner() {
        String cname = this.getClass().getSimpleName();
        String reportStr = "Report";
        if (cname.endsWith(reportStr)) {
            cname = cname.substring(0, cname.length() - reportStr.length());
        }
        this.templateName = cname.replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setDataVital(boolean dataVital) {
        this.dataVital = dataVital;
    }

    @Override
    public void run() throws Exception {
        long dataProgressMax = 50;
        long reportProgressVal = 90;
        long finalProgressVal = 100;
        initProgress(finalProgressVal);
        info("Формирование данных");
        ProgressIndicator dataProgress = new ProgressIndicatorImpl(0, dataProgressMax);
        List<SubReport> reports = getSubReports(dataProgress);
        if (reports == null || reports.isEmpty() || isDummy(reports, dataVital)) {
            warn("Нет данных для формирования.");
            return;
        }
        IReportEngine reportEngine = createReportEngine();
        for (SubReport report : reports) {
            reportEngine.addSubReport(report);
        }
        updateProgress(dataProgressMax);
        checkInterrupted();
        info("Формирование документа");
        File reportFile = reportEngine.createReport(this);
        updateProgress(reportProgressVal);
        saveReport(reportFile, reportEngine.getReportName());
        updateProgress(finalProgressVal);
    }
    
    protected abstract IReportEngine createReportEngine();

    protected abstract List<SubReport> getSubReports(ProgressIndicator progress) throws InterruptedException;

    private static boolean isDummy(List<SubReport> reports, boolean dataVital) {
        for (SubReport r : reports) {
            if (r != null && ((r.getDataSource() != null && !r.getDataSource().isEmpty()) || (!dataVital && (r.getParameters() != null && !r.getParameters().isEmpty())))) {
                return false;
            }
        }
        return true;
    }

    public static class ParamCollector {

        private final Map<String, Object> res = new HashMap<String, Object>();

        public ParamCollector() {
        }

        public ParamCollector(Map<String, Object> params) {
            res.putAll(res);
        }

        public void put(Enum e, Object o) {
            res.put(e.name(), o);
        }

        public void put(String name, Object o) {
            res.put(name, o);
        }

        public Map<String, Object> getRes() {
            return res;
        }
    }

    public static class DataCollector {

        private final List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        private Map<String, Object> row = new HashMap<String, Object>();
        private String first = null;

        public DataCollector() {
        }

        public void put(Enum e, Object o) {
            put(e.name(), o);
        }

        public void put(String name, Object o) {
            if (first == null) {
                first = name;
            } else if (name.equals(first)) {
                res.add(row);
                row = new HashMap<String, Object>();
            }
            row.put(name, o);
        }

        public List<Map<String, Object>> getRes() {
            List<Map<String, Object>> res2 = new ArrayList<Map<String, Object>>(res);
            if (!row.isEmpty()) {
                res2.add(row);
            }
            return res2;
        }
    }

}
