package utils;

import com.cedarsoftware.util.io.JsonWriter;
import examples.multiagent.drone_network_heatbeat.Controller;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.PrintToFile;

public class Serializer {

    double[] f;
    Vector[] s;

    String name;
    PrintToFile pw;

    StringBuilder sb;

    public Serializer(int T, int d, PrintToFile pw){
        s = new Vector[T];
        this.f = new double[T];

        for(int i=0; i<T; i++){
            s[i] = new Vector(d);
        }
        this.pw = pw;
    }

    public void append(SIAlgorithm algorithm){
        name = algorithm.getName();
        for(int i=0; i< algorithm.getStepsCount();i++){
            this.s[i].updateVector(algorithm.getPHistory().get(i));
            this.f[i] += algorithm.getHistory().get(i);
        }
    }

    public void build(int T){

        for(int i=0; i< T;i++){
            this.s[i].operate(Vector.OPERATOR.DIV, (double)T);
            this.f[i] = this.f[i]/T;
        }

        sb = new StringBuilder("{");
        sb.append("\"step\":"+ (T) + ",");
        sb.append("\"name\":"+ (name) + ",");
        sb.append("\"f\":" + JsonWriter.objectToJson(this.f) + ",");
        sb.append("\"s\":" + JsonWriter.objectToJson(s) + ",");
        sb.append("},");

    }

    public void save(){
        pw.log(sb.toString());
    }
}
