package examples.multiagent.drone_network_heatbeat;

import com.cedarsoftware.util.io.JsonWriter;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.util.PrintToFile;

public class Serializer {
    StringBuilder sb = null;
    PrintToFile ptf;

    int step = 1;

    public Serializer(PrintToFile pw){
        ptf = pw;
    }

    public Serializer build(Algorithm algorithm){
        sb = new StringBuilder("{");
        sb.append("\"step\":"+ (step++) + ",");
        sb.append("\"dronesMap\":" + JsonWriter.objectToJson(Controller.dronesMap) + ",");
        sb.append("\"agents\":" + JsonWriter.objectToJson(algorithm.getAgentsMap()) + ",");
        sb.append("\"pKtlMap\":" + JsonWriter.objectToJson(Controller.pKtlMap) + ",");
        sb.append("},");

        return this;
    }

    public void save(){
        ptf.log(sb.toString());
    }
}
