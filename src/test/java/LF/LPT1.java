package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.WalkType;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

public class LPT1 {
    @RepeatedTest(10)
    public void L1T5TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(5, 5, 1, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L1T5", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T10TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(10, 5, 1, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L1T10", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T15TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 1, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L1T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L1T20TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 1, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L1T20", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T5TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(5, 5, 2, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L2T5", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T10TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(10, 5, 2, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L2T10", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T15TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 2, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L2T15", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    public void L2T20TSOA(RepetitionInfo t) {
        try{
            Main m = new Main(15, 5, 2, 15,0.001, 0.0001, WalkType.RANDOM_THETA);
            Core.executeForwardTSOAInd(m, "L2T20", t.getCurrentRepetition(), 150, 350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
