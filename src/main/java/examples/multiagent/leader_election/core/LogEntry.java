package examples.multiagent.leader_election.core;

public class LogEntry {
    int term = 1;
    int tid = -1;
    String data = "start";

    public LogEntry(int term, int tid, String data) {
        this.term = term;
        this.tid = tid;
        this.data = data;
    }
}
