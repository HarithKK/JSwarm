package org.usa.soc.util;

import java.util.Date;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

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

    public void printTrail(double value, double percentage){
        System.out.print("\r ["+ value +"] ["+percentage+"%] "  + generate(() -> "#").limit((long)(percentage)).collect(joining()));
    }

    public void info(String message){
        System.out.println("[Info - "+ new Date().toString() + "]: " + message);
    }

    public void error(String message){
        System.out.println("[Error - "+ new Date().toString() + "]: " + message);
    }
}
