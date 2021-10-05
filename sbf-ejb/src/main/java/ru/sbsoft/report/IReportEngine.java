package ru.sbsoft.report;

import java.io.File;

/**
 *
 * @author Kiselev
 */
public interface IReportEngine {

    void addSubReport(SubReport subReport);

    File createReport(Object sender) throws Exception;

    String getReportName();
}
