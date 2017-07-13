package com.sosen.threaddetective;

import java.util.List;

public class ThreadComparisonResultItem {
    private String threadId;
    private long runningDuration;
    private int sevLevel;
    private List<String> dumps;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public long getRunningDuration() {
        return runningDuration;
    }

    public void setRunningDuration(long runningDuration) {
        this.runningDuration = runningDuration;
    }

    public int getSevLevel() {
        return sevLevel;
    }

    public void setSevLevel(int sevLevel) {
        this.sevLevel = sevLevel;
    }

    public List<String> getDumps() {
        return dumps;
    }

    public void setDumps(List<String> dumps) {
        this.dumps = dumps;
    }

}
