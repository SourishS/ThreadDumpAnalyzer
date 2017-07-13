package com.sosen.threaddetective;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadDump {
    private String filename;
    private long generationTime;
    private List<Thread> threads = new ArrayList<>();
    private Map<String, Thread> threadsMap = new HashMap<>();

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void addThread(Thread thread) {
        this.threads.add(thread);
        this.threadsMap.put(thread.getId(), thread);
    }

    public Thread getThread(String threadId) {
        return threadsMap.get(threadId);
    }

    @Override
    public String toString() {
        return filename + "[Generated at " + new Date(generationTime) + "]";
    }
}