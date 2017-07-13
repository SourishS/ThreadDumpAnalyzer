package com.sosen.threaddetective;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sourish
 *
 */
public class ThreadDependency {
    private String lockid;
    private List<Thread> lockownerThreads = new ArrayList<>();
    private List<Thread> lockrequiringThreads = new ArrayList<>();

    public ThreadDependency(String lockid) {
        setLockid(lockid);
    }

    public String getLockid() {
        return lockid;
    }

    public void setLockid(String lockid) {
        this.lockid = lockid;
    }

    public List<Thread> getLockownerThreads() {
        return lockownerThreads;
    }

    public void addLockownerThread(Thread lockownerThread) {
        this.lockownerThreads.add(lockownerThread);
    }

    public List<Thread> getLockrequiringThreads() {
        return lockrequiringThreads;
    }

    public void addLockrequiringThread(Thread lockrequiringThread) {
        this.lockrequiringThreads.add(lockrequiringThread);
    }

}
