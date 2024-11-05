package LF;

import examples.multiagent.leader_election.core.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

public class L1T1 {

    @RepeatedTest(10)
    public void L1T5Raft(RepetitionInfo t) {
        try{
            Core.executeForwardRaft(5,5,5,15, "L1T5", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5Random(RepetitionInfo t) {
        try{
            Core.executeForwardRandom(5,5,5,15, "L1T5", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5GHS(RepetitionInfo t) {
        try{
            Core.executeForwardGHS(5,5,5,15, "L1T5", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T5TSOA(RepetitionInfo t) {
        try{
            Core.executeForwardTSOA(5,5,5,15, "L1T5", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
