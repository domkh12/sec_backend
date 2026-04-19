package site.secmega.secapi.feature.report;


import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GenerateReportService {

    File generateExcelReport(List<?> objects, String template) throws IOException;

    File generatePDFReport(List<?> objects, String template) throws IOException;
}
