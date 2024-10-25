package org.usa.soc.si.runners;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionsFactory {
    List<Class<?>> list= new ArrayList<>();

    public FunctionsFactory register(Class<?> function){
        list.add(function);
        return this;
    }

    public ObjectiveFunction get(int i, int d){
        try {
            ObjectiveFunction fn = (ObjectiveFunction) list.get(i).getConstructor().newInstance();
            return fn.updateDimensions(d);
        } catch (Exception e) {
            Logger.getInstance().error(e.getMessage());
            return null;
        }
    }

    public FunctionsFactory build(){
        return this;
    }

    public String[] getFunctionNames(){
        return list.stream().map(d-> d.getSimpleName()).toArray(String[]::new);
    }
}
