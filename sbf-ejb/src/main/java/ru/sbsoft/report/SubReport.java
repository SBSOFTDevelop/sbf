package ru.sbsoft.report;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Sokoloff
 */
public class SubReport {

    private Map<String, Object> parameters;
    private List<Map<String, Object>> dataSource;
    private String templateName;

    public SubReport(Map<String, Object> parameters, List<Map<String, Object>> dataSource, String templateName) {
        this.parameters = parameters;
        this.dataSource = dataSource;
        this.templateName = templateName;
    }
    
    public SubReport(Map<String, Object> parameters, List<Map<String, Object>> dataSource) {
        this(parameters, dataSource, null);
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<Map<String, Object>> getDataSource() {
        return dataSource;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void setDataSource(List<Map<String, Object>> dataSource) {
        this.dataSource = dataSource;
    }

}
