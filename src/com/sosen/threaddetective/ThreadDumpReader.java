package com.sosen.threaddetective;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.State;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sosen.threaddetective.utils.Logger;

/**
 * 
 * @author sourish
 *
 */
public class ThreadDumpReader {
    public ThreadDump readDump(String dumpFilename) throws Exception {
        if (dumpFilename == null || dumpFilename.isEmpty()) {
            throw new IOException("Dumpfile name can not be null or empty");
        }

        File dumpFile = new File(dumpFilename);

        if (!dumpFile.exists()) {
            throw new IOException("Dumpfile " + dumpFilename + "does not exists");
        }
        Logger.log(getClass(), "Reading file %s", dumpFilename);

        try (Reader reader = new Reader(dumpFilename)) {
            ThreadDump dump = new ThreadDump();

            dump.setFilename(dumpFile.getName());
            long generationTime = getDumpGenerationTime(reader);
            dump.setGenerationTime(generationTime);
            Logger.log(getClass(), "Generated at : %s", new Date(generationTime));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\"")) {
                    dump.addThread(getThreadData(line, reader));
                }
            }

            Logger.log(getClass(), "Total threads in dump %s: %s", dumpFilename, dump.getThreads().size());
            return dump;
        }
    }

    private Thread getThreadData(String line, Reader reader) throws IOException {
        Thread thread = new Thread(getThreadId(line));
        thread.setName(getThreadName(line));
        thread.setThreadState(getThreadState(reader));
        addTraceElements(thread, reader);

        return thread;
    }

    private String getThreadId(String line) {
        return line.substring(line.lastIndexOf(" ")).trim();
    }

    private void addTraceElements(Thread thread, Reader reader) throws IOException {
        String line;

        while ((line = reader.readLine()) != null && !line.trim().startsWith("at"))
            ;
        String traceString = "";
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            traceString += line + "<br>";
            if (line.trim().startsWith("at")) {
                thread.addTraceElement(line);
            } else if (line.trim().startsWith("-")) {
                if (line.contains("wait")) {
                    thread.addWaitingForLockes(getLockTocken(line));
                } else if (line.contains("locked")) {
                    thread.addOwnedLocks(getLockTocken(line));
                }
            }
        }

        thread.setTraceString(traceString);
    }

    private String getLockTocken(String line) {
        int begin = line.indexOf("<");
        int end = line.indexOf(">", begin + 1);
        return line.substring(begin + 1, end);
    }

    private State getThreadState(Reader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.contains("State"))
            ;
        return State.valueOf(line.split(":")[1].trim());
    }

    private String getThreadName(String line) {
        int begin = line.indexOf("\"");
        int end = line.indexOf("\"", begin + 1);
        return line.substring(begin + 1, end);
    }

    private long getDumpGenerationTime(Reader reader) throws ParseException, IOException {
        DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long generationTime = m_ISO8601Local.parse(reader.readLine()).getTime();
        return generationTime;
    }

    class Reader implements Closeable {

        private BufferedReader bufferedReader;
        private FileReader fileReader;

        public Reader(String dumpFile) throws FileNotFoundException {
            fileReader = new FileReader(dumpFile);
            bufferedReader = new BufferedReader(fileReader);
        }

        public String readLine() throws IOException {
            return bufferedReader.readLine();
        }

        public void close() {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    Logger.log(getClass(), "Error while closing file connection ", e);
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Logger.log(getClass(), "Error while closing file connection ", e);
                }
            }
        }
    }
}
