package com.sosen.threaddetective;

import java.util.HashMap;
import java.util.Map;

import com.sosen.threaddetective.utils.Logger;

/**
 * 
 * @author sourish
 *
 */
public class TheadDependencyDetector {
    public void dectctDependency(ThreadDump threadDump) {
        Logger.log(getClass(), "Analysing thread dump %s", threadDump.getFilename());
        Map<String, ThreadDependency> threadDependenciesMap = new HashMap<>();
        for (Thread thread : threadDump.getThreads()) {
            for (String lockid : thread.getOwnedLocks()) {
                if (threadDependenciesMap.containsKey(lockid)) {
                    threadDependenciesMap.get(lockid).addLockownerThread(thread);
                } else {
                    ThreadDependency threadDependency = new ThreadDependency(lockid);
                    threadDependenciesMap.put(lockid, threadDependency);
                    threadDependency.addLockownerThread(thread);
                }
            }

            for (String lockid : thread.getWaitingForLockes()) {
                if (threadDependenciesMap.containsKey(lockid)) {
                    threadDependenciesMap.get(lockid).addLockrequiringThread(thread);
                } else {
                    ThreadDependency threadDependency = new ThreadDependency(lockid);
                    threadDependenciesMap.put(lockid, threadDependency);
                    threadDependency.addLockrequiringThread(thread);
                }
            }
        }
    }
}
