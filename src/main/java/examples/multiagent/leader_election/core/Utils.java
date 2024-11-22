package examples.multiagent.leader_election.core;

import org.usa.soc.util.Randoms;

public class Utils {
    public static void addRandomProcessingWeight(int from, int to){
        try {
            Thread.sleep(Randoms.rand(from, to));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addTransactionProcessingWeight(){
        Utils.addRandomProcessingWeight(10,30);
    }
}
