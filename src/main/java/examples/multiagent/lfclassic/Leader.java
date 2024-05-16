package examples.multiagent.lfclassic;

import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.Randoms;

public class Leader extends Agent {
    double xDirection = 1, yDirection=1, mag = 2;

    @Override
    public void step() {
        if(this.getX() > this.getMargines().xMax - 5){
            this.xDirection = -1;
        }else if(this.getX() < this.getMargines().xMin + 5){
            this.xDirection = 1;
        }

        if(this.getY() > this.getMargines().yMax - 5){
            yDirection = -1;
            mag += Randoms.rand(-2,2);
        }else if(this.getY() < this.getMargines().yMin + 5){
            yDirection = 1;
            mag += Randoms.rand(-2,2);
        }

        this.updatePosition(this.getX() + xDirection, this.getY() + (mag*yDirection));
    }
}