package examples.multiagent.leader_election;

import org.usa.soc.util.Randoms;

import java.util.List;

public class Critarian {

    public int selectCritarian(Critarians ctype, List<Drone> layer, int i){
        switch (ctype){
            case RANDOM:
                return random(0, layer.size());
        }
        return 0;
    }

    public int random(int i, int j){
        return Randoms.rand(i, j);
    }

}
