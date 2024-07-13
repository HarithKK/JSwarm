package org.usa.soc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PrintToFile {

    private static PrintToFile instance = null;
    private String filepath= "";

    public static PrintToFile getInstance(){
        if(instance == null){
            instance = new PrintToFile();
        }
        return instance;
    }

    private PrintToFile(){
    }

    public PrintToFile build(String filepath){
        this.filepath = filepath;
        return this;
    }

    public void log(String data){
        Path path = Paths.get(filepath);
        try {
            if(Files.exists(path)){
                Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
            }else{
                Files.write(path, data.getBytes(), StandardOpenOption.CREATE);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
