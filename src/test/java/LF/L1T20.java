package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class L1T20 {

    @RepeatedTest(10)
    public void L1T5Raft(RepetitionInfo t) {
        try{
            Main m = new Main(20, 5, 1, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRaft(m, "L1T20", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5Random(RepetitionInfo t) {
        try{
            Core.executeForwardRandom(20,15,5,15, "L1T20", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5GHS(RepetitionInfo t) {
        try{
            Core.executeForwardGHS(20,15,5,15, "L1T20", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5TSOA(RepetitionInfo t) {
        try{
            Core.executeForwardTSOA(20,15,5,15, "L1T20", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
