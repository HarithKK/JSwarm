package examples.multiagent.leader_election.core;

import org.usa.soc.util.Randoms;

import java.util.List;

public class Critarian {

    public enum Critarians {
        RANDOM
    }

    public int selectCritarian(Critarians ctype, List<Drone> layer){
        switch (ctype){
            case RANDOM:
                return random(0, layer.size()-1);
        }
        return 0;
    }

    public int random(int i, int j){
        return Randoms.rand(i, j);
    }

}
