package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static double[] fill (int n, double val){
        double[] d = new double[n];
        Arrays.fill(d, val);
        return d;
    }

    public static void writeToFile(String filepath, String data){
        Path path = Paths.get(filepath);
        System.out.println(data);
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

    public static double calcStd(List<Double> l){
        double mean = l.stream().mapToDouble(d -> (Double)d).average().getAsDouble();
        double standardDeviation = 0.0;
        for (Double num : l) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation / l.size());
    }
}
