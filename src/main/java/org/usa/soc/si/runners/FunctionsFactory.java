package org.usa.soc.si.runners;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionsFactory {
    List<ObjectiveFunction> list= new ArrayList<>();

    public FunctionsFactory register(Class<?> function){
        try {
            ObjectiveFunction fn = (ObjectiveFunction) function.getConstructor().newInstance();
            list.add(fn);
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage());
            return null;
        }
        return this;
    }

    public FunctionsFactory register(ObjectiveFunction function){
        list.add(function);
        return this;
    }

    public ObjectiveFunction get(int i, int d){
        return list.get(i).updateDimensions(d);
    }

    public FunctionsFactory build(){
        return this;
    }

    public String[] getFunctionNames(){
        return list.stream().map(d-> d.getClass().getSimpleName()).toArray(String[]::new);
    }
}
