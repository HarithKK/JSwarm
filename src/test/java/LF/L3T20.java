package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.data_structures.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class L3T20 {

    @RepeatedTest(10)
    public void L3T20Raft(RepetitionInfo t) {
        try{
            Main m = new Main(20, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRaft(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L3T20Random(RepetitionInfo t) {
        try{
            Main m = new Main(20, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRandom(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L3T20GHS(RepetitionInfo t) {
        try{
            Main m = new Main(20, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardGHS(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L3T20TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(20, 5, 3, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardTSOA(m, "L3T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
