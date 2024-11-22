package examples.multiagent.leader_election.core.data_structures;

public class LogEntry {
    private int term = 1;
    int tid = -1;
    String data = "start";

    public LogEntry(int term, int tid, String data) {
        this.setTerm(term);
        this.tid = tid;
        this.data = data;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
