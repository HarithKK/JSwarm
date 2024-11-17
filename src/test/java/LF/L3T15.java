package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class L3T15 {

    @RepeatedTest(10)
    public void L3T15Raft(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRaft(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L3T15Random(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRandom(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L3T15GHS(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardGHS(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L3T15TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardTSOAInd(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
