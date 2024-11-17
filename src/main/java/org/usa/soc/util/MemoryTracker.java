package org.usa.soc.util;

import java.util.List;

public class MemoryTracker {
    private long size =0;

    public MemoryTracker updateInt(int count){
        setSize(getSize() + (Integer.SIZE * count));
        return this;
    }

    public MemoryTracker updateDouble(int count){
        setSize(getSize() + (Double.SIZE * count));
        return this;
    }

    public MemoryTracker updateLong(int count){
        setSize(getSize() + (Long.SIZE * count));
        return this;
    }

    public MemoryTracker update(String s){
        setSize(getSize() + (s.getBytes().length * Byte.SIZE));
        return this;
    }

    public MemoryTracker update(List l){
        Object o = l.get(0);
        if(o instanceof Double)
            return updateDouble(l.size());

        if(o instanceof Integer)
            return updateInt(l.size());

        return this;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
