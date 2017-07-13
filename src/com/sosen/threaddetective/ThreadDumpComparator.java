package com.sosen.threaddetective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sosen.threaddetective.utils.Logger;

/**
 * 
 * @author sourish
 *
 */
public class ThreadDumpComparator {
    public ThreadComparisonResult compareThreadDumps(List<ThreadDump> threadDumps) {
        if (threadDumps.size() < 2) {
            Logger.log(getClass(), "There should be at least two dumps to compare");
            return null;
        }

        Logger.log(getClass(), "Sorting dumps based on generation time");
        sortDumps(threadDumps);

        // Maps the tread id with corresponding dump ids
        Map<String, List<String>> threadSrcDumpMap = new HashMap<>();
        Map<String, ThreadDump> threadDumpMap = new HashMap<>();
        Logger.log(getClass(), "Analyzing dumps...");
        for (ThreadDump dump : threadDumps) {
            threadDumpMap.put(dump.getFilename(), dump);
            for (Thread thead : dump.getThreads()) {
                if (!threadSrcDumpMap.containsKey(thead.getId())) {
                    threadSrcDumpMap.put(thead.getId(), new ArrayList<>());
                }
                threadSrcDumpMap.get(thead.getId()).add(dump.getFilename());
            }
        }

        for (Iterator<Entry<String, List<String>>> it = threadSrcDumpMap.entrySet().iterator(); it.hasNext();) {
            // Remove threads those are present in only one dump
            if (it.next().getValue().size() < 2) {
                it.remove(); 
            }
        }

        ThreadComparisonResult threadComparisonResult = new ThreadComparisonResult();

        for (Entry<String, List<String>> e : threadSrcDumpMap.entrySet()) {
            ThreadDump firstDump = threadDumpMap.get(e.getValue().get(0));
            ThreadDump lastDump = threadDumpMap.get(e.getValue().get(e.getValue().size() - 1));

            ThreadComparisonResultItem resultItem = new ThreadComparisonResultItem();
            resultItem.setThreadId(e.getKey());
            resultItem.setDumps(e.getValue());
            resultItem.setRunningDuration(lastDump.getGenerationTime() - firstDump.getGenerationTime());
            // resultItem.setSevLevel(sevLevel);
            threadComparisonResult.addResultItems(resultItem);
        }
        return threadComparisonResult;
    }

    private void sortDumps(List<ThreadDump> threadDumps) {
        Collections.sort(threadDumps, (td1, td2) -> Long.compare(td1.getGenerationTime(), td2.getGenerationTime()));
    }
}
