package com.sosen.threaddetective;

import java.util.ArrayList;
import java.util.List;

import com.sosen.threaddetective.report.ReportGenerator;

/**
 * 
 * @author sourish
 *
 */
public class ThreadDumpMain {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please pass thread dump filenames");
            System.exit(0);
        }
        try (ReportGenerator generator = new ReportGenerator()) {
            generator.startHTMLReport();

            List<ThreadDump> threadDumps = new ArrayList<>();

            for (String dumpFilename : args) {
                threadDumps.add(createDump(generator, dumpFilename));
            }

            ThreadComparisonResult comparisonResult = new ThreadDumpComparator().compareThreadDumps(threadDumps);
            generator.writeComparisonData(threadDumps, comparisonResult);
        }
    }

    private static ThreadDump createDump(ReportGenerator generator, String dumpFilename) throws Exception {
        ThreadDump threadDump = new ThreadDumpReader().readDump(dumpFilename);
        generator.writeThreadDumpData(threadDump);
        new TheadDependencyDetector().dectctDependency(threadDump);

        return threadDump;
    }
}
