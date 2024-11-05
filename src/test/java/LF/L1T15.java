package LF;

import examples.multiagent.leader_election.core.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class L1T15 {

    @RepeatedTest(10)
    public void L1T5Raft(RepetitionInfo t) {
        try{
            Core.executeForwardRaft(15,10,5,15, "L1T15", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5Random(RepetitionInfo t) {
        try{
            Core.executeForwardRandom(15,10,5,15, "L1T15", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5GHS(RepetitionInfo t) {
        try{
            Core.executeForwardGHS(15,10,5,15, "L1T15", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5TSOA(RepetitionInfo t) {
        try{
            Core.executeForwardTSOA(15,10,5,15, "L1T15", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
