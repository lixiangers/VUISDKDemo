// IPriority.aidl
package com.roobo.priority;

interface IPriority {

    boolean setSchedPolicy(int pid, int policy, int rtPriority);

    int getSchedPolicy(int pid);

    boolean setPriority(int pid, int priority);

    int getPriority(int pid);

    boolean setNice(int pid, int nice);

    int getNice(int pid);

    boolean setRTPriority(int pid, int priority);

    int getRTPriority(int pid);

    void exec(String cmd);

}
