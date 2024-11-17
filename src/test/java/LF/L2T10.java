package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class L2T10 {

    @RepeatedTest(10)
    public void L2T5Raft(RepetitionInfo t) {
        try{
            Main m = new Main(10, 5, 2, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRaft(m, "L2T10", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T5Random(RepetitionInfo t) {
        try{
            Main m = new Main(10, 5, 2, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardRandom(m, "L2T10", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T5GHS(RepetitionInfo t) {
        try{
            Main m = new Main(10, 5, 2, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardGHS(m, "L2T10", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T5TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(10, 5, 2, 15,0.001, 0.0001, WalkType.FORWARD);
            Core.executeForwardTSOAInd(m, "L2T10", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
