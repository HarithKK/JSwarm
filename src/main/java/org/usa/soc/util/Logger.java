package org.usa.soc.util;

public class Logger {

    private static Logger instance;

    public static Logger getInstance(){
        if(instance == null){
            instance = new Logger();
        }
        return instance;
    }

    private Logger(){}

    private boolean isLoggerOn = false;

    public void log(String message){
        if(isLoggerOn){
            System.out.println(message);
        }
    }
}
