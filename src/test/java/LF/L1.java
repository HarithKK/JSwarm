package LF;

import examples.multiagent.leader_election.core.*;
import org.junit.jupiter.api.*;

public class L1 {

    @RepeatedTest(1)
    public void L1T51(RepetitionInfo t) {
        try{
            Core.executeForwardTSOA(5,5,5,15, "L1T5", t.getCurrentRepetition(), 150, 350, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
