package org.usa.soc.util;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    String filepath;

    private FileHandler(){}
    private List<String> lines;

    public FileHandler(String path){
        this.filepath = path;
    }

    public FileHandler read(){
        try {
            lines = Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public List<List<Double>> getDoubles(){
        List<List<Double>> data = new ArrayList<>();

        for(String line: lines){
            List<Double> d = new ArrayList<>();
            for(String s: line.split(" ")){
                if(!s.isEmpty() && !s.isBlank()){
                    try{
                        d.add(Double.parseDouble(s));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            data.add(d);
        }
        return data;
    }

    public RealMatrix toRealMatrix(){

        List<List<Double>> data = getDoubles();
        int m = data.size();
        int n = data.get(0).size();
        RealMatrix matrix = MatrixUtils.createRealMatrix(m, n);

        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                matrix.addToEntry(i,j,data.get(i).get(j));
            }
        }
        return matrix;
    }
}
