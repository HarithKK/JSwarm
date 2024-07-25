package org.usa.soc.core;
public class Flag {
    private boolean value;

    public Flag(){
        this.value = false;
    }

    public boolean isSet(){ return value == true; }

    public void set(){this.value = true;}
    public void unset(){this.value=false;}

    public void setValue(boolean value){this.value = value;}

}