package com.sosen.threaddetective;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

public class Thread {
    private String id;
    private String name;
    private State threadState;
    private List<String> ownedLocks = new ArrayList<>();
    private List<String> waitingForLockes = new ArrayList<>();

    private String traceString;
    private List<String> traceElements = new ArrayList<>();

    public Thread(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getThreadState() {
        return threadState;
    }

    public void setThreadState(State threadState) {
        this.threadState = threadState;
    }

    public List<String> getOwnedLocks() {
        return ownedLocks;
    }

    public void addOwnedLocks(String ownedLock) {
        this.ownedLocks.add(ownedLock);
    }

    public List<String> getWaitingForLockes() {
        return waitingForLockes;
    }

    public void addWaitingForLockes(String waitingForLock) {
        this.waitingForLockes.add(waitingForLock);
    }

    public String getTraceString() {
        return traceString;
    }

    public void setTraceString(String traceString) {
        this.traceString = traceString;
    }

    public List<String> getTraceElements() {
        return traceElements;
    }

    public void addTraceElement(String traceElement) {
        this.traceElements.add(traceElement);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object anotherThread) {
        return (anotherThread instanceof Thread) && id != null && id.equals(((Thread) anotherThread).id);
    }

    @Override
    public String toString() {
        return name + "[" + id + "]|" + threadState + "| Owned locks: " + ownedLocks + "| waiting for "
                + waitingForLockes;
    }
}
