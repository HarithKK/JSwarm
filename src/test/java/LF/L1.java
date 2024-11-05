package LF;

import examples.multiagent.leader_election.core.*;
import org.junit.jupiter.api.*;

public class L1 {

    @RepeatedTest(5)
    public void L1T51(RepetitionInfo t) {
        try{
            Core.executeForwardSI(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, Critarian.SICritatianType.TSOA, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T52(RepetitionInfo t) {
        try{
            Core.executeForwardSI(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, Critarian.SICritatianType.ALSO, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T53(RepetitionInfo t) {
        try{
            Core.executeForwardSI(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, Critarian.SICritatianType.PSO, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T54(RepetitionInfo t) {
        try{
            Core.executeForwardSI(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, Critarian.SICritatianType.CSO, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T55(RepetitionInfo t) {
        try{
            Core.executeForwardSI(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, Critarian.SICritatianType.MFA, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T56(RepetitionInfo t) {
        try{
            Core.executeForwardRaft(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T57(RepetitionInfo t) {
        try{
            Core.executeForwardRandom(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T58(RepetitionInfo t) {
        try{
            Core.executeForwardGHS(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(5)
    public void L1T59(RepetitionInfo t) {
        try{
            Core.executeForwardTSOA(5,5,5,15, "L1T5", t.getCurrentRepetition(), 100, 150, WalkType.FORWARD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
