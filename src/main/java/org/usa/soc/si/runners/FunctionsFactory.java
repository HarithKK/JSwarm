package org.usa.soc.si.runners;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Logger;

import java.util.ArrayList;
import java.util.List;

class Functions{
        List<ObjectiveFunction> list= new ArrayList<>();


        public ObjectiveFunction get(int i, int d){
            return list.get(i).updateDimensions(d);
        }
        public String[] getFunctionNames(){
            return list.stream().map(d-> d.getClass().getSimpleName()).toArray(String[]::new);
        }
}

public class FunctionsFactory {
    Functions f;
    public FunctionsFactory(){
        f = new Functions();

    }

    public FunctionsFactory register(Class<?> function){
        try {
            ObjectiveFunction fn = (ObjectiveFunction) function.getConstructor().newInstance();
            f.list.add(fn);
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage());
            return null;
        }
        return this;
    }

    public FunctionsFactory register(ObjectiveFunction function){
        f.list.add(function);
        return this;
    }

    public Functions build(){
        return this.f;
    }
}
