package com.sosen.threaddetective.report;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sosen.threaddetective.Thread;
import com.sosen.threaddetective.ThreadComparisonResult;
import com.sosen.threaddetective.ThreadComparisonResultItem;
import com.sosen.threaddetective.ThreadDump;
import com.sosen.threaddetective.utils.Logger;

/**
 * 
 * @author sourish
 *
 */
public class ReportGenerator implements Closeable {
    private FileWriter reportFileWriter;
    private BufferedWriter reportBufferedWriter;
    private File reportFolder;
    private File reportFile;

    private static final String REPORT_TEMPLATE_NAME = "resources/report.template.html";
    private static final String DUMPDATA_TEMPLATE_NAME = "resources/dumpdata.template.html";

    private static final String REPORT_HTML_NAME = "index.html";
    private static final String DUMPDATA_HTML_NAME = "dumpdata-%dumpfilename%.html";

    private static final String OUTPUT_FOLDER_PREFIX = "threaddata_";

    public void startHTMLReport() throws IOException {
        reportFolder = new File(OUTPUT_FOLDER_PREFIX + System.currentTimeMillis());
        boolean folderCreated = reportFolder.mkdir();
        if (!folderCreated) {
            // throw new RuntimeException("Output folder can not be created");
        }

        reportFile = new File(reportFolder, REPORT_HTML_NAME);
        reportFileWriter = new FileWriter(reportFile);
        reportBufferedWriter = new BufferedWriter(reportFileWriter);
    }

    public void writeThreadDumpData(ThreadDump threadDump) throws IOException {
        Logger.log(getClass(), "Reporting dump data");
        FileWriter fr = new FileWriter(new File(reportFolder, DUMPDATA_HTML_NAME.replace("%dumpfilename%",
                threadDump.getFilename())));
        BufferedWriter bw = new BufferedWriter(fr);

        String templateHtml = getTemplateContent(DUMPDATA_TEMPLATE_NAME);

        templateHtml = templateHtml.replace("%%filename%%", threadDump.getFilename());
        templateHtml = templateHtml.replace("%%gentime%%", new Date(threadDump.getGenerationTime()).toString());
        templateHtml = templateHtml.replace("%%threadcnt%%", String.valueOf(threadDump.getThreads().size()));

        StringBuilder sb = new StringBuilder();
        boolean odd = true;
        for (com.sosen.threaddetective.Thread thread : threadDump.getThreads()) {
            sb.append("<tr ");
            if (odd) {
                sb.append("class='odd' ");
            }

            sb.append("id='").append(thread.getId()).append("'");
            sb.append("name='").append(thread.getName()).append("'");
            sb.append("stack='").append(thread.getTraceString()).append("'");
            sb.append("'>");

            sb.append("<td>")
                    .append(thread.getId())
                    .append("</td><td>")
                    .append(thread.getName())
                    .append("</td><td>")
                    .append(thread.getThreadState())
                    .append("</td><td>")
                    .append(thread.getOwnedLocks())
                    .append("</td><td>")
                    .append(thread.getWaitingForLockes())
                    .append("</td><td><a onclick='showStack(this.parentElement.parentElement);'>View trace</a></td></tr>");
            odd = !odd;
        }

        templateHtml = templateHtml.replace("%%tabledata%%", sb.toString());

        bw.write(templateHtml);

        bw.close();
        fr.close();
    }

    public void writeComparisonData(List<ThreadDump> threadDumps, ThreadComparisonResult comparisonResult)
            throws IOException {
        Logger.log(getClass(), "Reporting comparison data");
        String templateHtml = getTemplateContent(REPORT_TEMPLATE_NAME);
        StringBuilder sb = new StringBuilder();
        Map<String, ThreadDump> threadDumpMap = new HashMap<>();
        for (ThreadDump threadDump : threadDumps) {
            threadDumpMap.put(threadDump.getFilename(), threadDump);
            sb.append(
                    "<a style='display: list-item;background-color: #aaf;line-height: 25px;padding-left: 5px;text-decoration: none;border: 1px solid #55f;margin-bottom: 1px;' href='")
                    .append(DUMPDATA_HTML_NAME.replace("%dumpfilename%", threadDump.getFilename())).append("'>")
                    .append(threadDump.getFilename()).append("</a>");
        }
        templateHtml = templateHtml.replace("%%dumps%%", sb.toString());
        templateHtml = templateHtml.replace("%%threadcnt%%", String.valueOf(comparisonResult.getResultItems().size()));

        sb = new StringBuilder();
        for (ThreadComparisonResultItem resultItem : comparisonResult.getResultItems()) {
            ThreadDump firstDump = threadDumpMap.get(resultItem.getDumps().get(0));
            ThreadDump lastDump = threadDumpMap.get(resultItem.getDumps().get(resultItem.getDumps().size() - 1));
            Thread thread = firstDump.getThread(resultItem.getThreadId());
            sb.append("<td>").append(thread.getId()).append("</td><td>").append(thread.getName()).append("</td><td>")
                    .append(thread.getThreadState()).append("</td><td>").append(resultItem.getRunningDuration())
                    .append("</td><td>").append(resultItem.getSevLevel()).append("</td><td>");

            for (String dump : resultItem.getDumps()) {
                sb.append("<a style='display: list-item; white-space: nowrap;' href='")
                        .append(DUMPDATA_HTML_NAME.replace("%dumpfilename%", dump)).append("#").append(thread.getId())
                        .append("'>").append(dump).append("</a>");
            }

            sb.append("</td></tr>");

        }
        templateHtml = templateHtml.replace("%%tabledata%%", sb.toString());

        reportBufferedWriter.write(templateHtml);
        Logger.log(getClass(), "Report generated at : %s", reportFile.getAbsolutePath());
    }

    private String getTemplateContent(String templateFile) throws IOException {
        return new String(Files.readAllBytes(new File(templateFile).toPath()), Charset.defaultCharset());
    }

    private void writeReportLine(String string) throws IOException {
        reportBufferedWriter.write(string);
        reportBufferedWriter.newLine();
    }

    @Override
    public void close() throws IOException {
        if (reportBufferedWriter != null) {
            reportBufferedWriter.close();

        }
        if (reportFileWriter != null) {
            reportFileWriter.close();
        }
    }
}
