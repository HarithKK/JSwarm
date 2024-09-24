package org.usa.soc.core.ds;

/*
This is the position vector
 */

import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Smoother;
import org.usa.soc.util.StringFormatter;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class Vector {

    private Double [] positionIndexes;
    private int numberOfDimensions;

    private double maxMagnitude = Double.MAX_VALUE;

    public Vector(int numberOfDimensions) {
        this.numberOfDimensions = numberOfDimensions;
        setPositionIndexes(new Double[numberOfDimensions]);
        resetAllValues(0.0);
    }

    public void setVector(Vector v){
        if(v == null)
            return;
        if(v.getNumberOfDimensions() != this.getNumberOfDimensions()){
            throw new IllegalArgumentException("Number of Dimensions are Mismatched!");
        }
        for(int i =0;i< this.getNumberOfDimensions();i++){
            this.positionIndexes[i] = v.positionIndexes[i];
        }
    }

    public Vector setMaxVector(){
        for(int i =0;i< this.getNumberOfDimensions();i++){
            this.positionIndexes[i] = Double.POSITIVE_INFINITY;
        }
        return this;
    }

    public Vector setMinVector(){
        for(int i =0;i< this.getNumberOfDimensions();i++){
            this.positionIndexes[i] = Double.NEGATIVE_INFINITY;
        }
        return this;
    }

    public void setVector(Vector v, double []min, double []max){
        if(v.getNumberOfDimensions() != this.getNumberOfDimensions()){
            throw new IllegalArgumentException("Number of Dimensions are Mismatched!");
        }
        for(int i =0;i< this.getNumberOfDimensions();i++){
            this.positionIndexes[i] = Validator.validatePosition(min[i], max[i],v.positionIndexes[i]);
        }
    }

    public Double[] getPositionIndexes() {
        return positionIndexes;
    }

    public void setPositionIndexes(Double[] positionIndexes) {
        if(positionIndexes.length != this.getNumberOfDimensions()){
            throw new IllegalArgumentException("Number of Dimensions are Mismatched!");
        }
        this.positionIndexes = positionIndexes;
    }

    public Vector setValues(double []values){
        int count = Math.min(values.length, this.getNumberOfDimensions());
        for(int i=0; i< count; i++){
            this.positionIndexes[i] = values[i];
        }
        return this;
    }

    public  void setValue(Double value, int index){
        if(index >= this.getNumberOfDimensions()){
            throw new ArrayIndexOutOfBoundsException("Index should be less than "+ this.getNumberOfDimensions());
        }

        this.positionIndexes[index] = value;
    }

    public  void updateValue(Double value, int index){
        if(index >= this.getNumberOfDimensions()){
            throw new ArrayIndexOutOfBoundsException("Index should be less than "+ this.getNumberOfDimensions());
        }

        this.positionIndexes[index] = this.positionIndexes[index] + value;
    }

    public  void updateVector(Vector v){
        if(v.numberOfDimensions != this.getNumberOfDimensions()){
            throw new ArrayIndexOutOfBoundsException("Index should be less than "+ this.getNumberOfDimensions());
        }
        for(int i =0;i< this.getNumberOfDimensions();i++){
            this.positionIndexes[i] = this.positionIndexes[i] + v.positionIndexes[i];
        }
    }

    public Double getValue(int index){
        return this.positionIndexes[index];
    }

    public Vector resetAllValues(Double value){
        for(int i = 0; i< getNumberOfDimensions(); i++){
            this.positionIndexes[i] = value;
        }
        return this;
    }

    public Vector toCos(){
        for(int i = 0; i< getNumberOfDimensions(); i++){
            this.positionIndexes[i] = Math.cos(this.positionIndexes[i]);
        }
        return this;
    }

    public Vector toSin(){
        for(int i = 0; i< getNumberOfDimensions(); i++){
            this.positionIndexes[i] = Math.sin(this.positionIndexes[i]);
        }
        return this;
    }

    public Vector toExp(){
        for(int i = 0; i< getNumberOfDimensions(); i++){
            this.positionIndexes[i] = Math.exp(this.positionIndexes[i]);
        }
        return this;
    }

    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    public Vector operate(OPERATOR o, Double value) {
        Vector tempV = this.getClonedVector();
        for(int i=0; i< tempV.numberOfDimensions; i++){
            Double currentVal = tempV.getValue(i);
            switch (o){
                case ADD: tempV.setValue((currentVal+value),i); break;
                case SUB: tempV.setValue((currentVal-value),i); break;
                case MULP: tempV.setValue((currentVal*value),i); break;
                case DIV: tempV.setValue(( value == 0 ? Double.POSITIVE_INFINITY : currentVal/value),i); break;
            }
        }

        return Smoother.smooth(tempV, this.maxMagnitude);
    }

    public Vector operate(OPERATOR o, Vector v) {
        Vector tempV = this.getClonedVector();
        for(int i=0; i< tempV.numberOfDimensions; i++){
            Double currentVal = tempV.getValue(i);
            Double value = v.getValue(i);
            switch (o){
                case ADD: tempV.setValue((currentVal+value),i); break;
                case SUB: tempV.setValue((currentVal-value),i); break;
                case MULP: tempV.setValue((currentVal*value),i); break;
                case DIV: tempV.setValue(( value == 0 ? Double.POSITIVE_INFINITY : currentVal/value),i); break;
            }
        }
        return Smoother.smooth(tempV, this.maxMagnitude);
    }

    public double innerProduct(Vector v) {
        double ipValue = 0;
        for(int i=0; i< this.numberOfDimensions; i++){
            ipValue += this.getValue(i) * v.getValue(i);
        }
        return ipValue;
    }

    public String toString(){
        return StringFormatter.toString(this.positionIndexes);
    }

    public String toStringArray(){
        StringBuilder sb = new StringBuilder("[");
        for(double d : positionIndexes){
            sb.append(d);
            sb.append(",");
        }
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }

    public List<Double> toAbsList(int round){

        ArrayList<Double> l = new ArrayList<>();
        double deli = 10 * round;
        for(Double d :this.positionIndexes){
            l.add(Mathamatics.absRound(d, round));
        }

        return l;
    }

    public List<Double> toList(int round){

        ArrayList<Double> l = new ArrayList<>();
        for(Double d :this.positionIndexes){
            l.add(Mathamatics.round(d, round));
        }

        return l;
    }

    public double[] toDoubleArray(int round){

        double[] l = new double[this.numberOfDimensions];
        for(int i=0;i< this.numberOfDimensions;i++){
            l[i] = Mathamatics.round(this.positionIndexes[i], round);
        }
        return l;
    }

    public double getMaxMagnitude() {
        return maxMagnitude;
    }

    public void setMaxMagnitude(double maxMagnitude) {
        this.maxMagnitude = maxMagnitude;
    }

    public enum OPERATOR {
        ADD,
        SUB,
        MULP,
        DIV
    }

    public Vector getClonedVector(){
        Vector v = new Vector(this.numberOfDimensions);
        v.setVector(this);
        return v;
    }

    public double getDistance(Vector v){
        double sum = 0;
        for(int i=0;i<this.numberOfDimensions;i++){
            sum+= Math.pow(this.positionIndexes[i] - v.getValue(i),2);
        }
        return Math.sqrt(sum);
    }

    public double getMagnitude(){
        double sum = 0;
        for(int i=0;i<this.numberOfDimensions;i++){
            sum+= Math.pow(this.positionIndexes[i],2);
        }
        return Math.sqrt(sum);
    }

    public Vector toAbs(){
        Vector v = new Vector(numberOfDimensions);
        for(int i=0;i<this.numberOfDimensions;i++){
            v.setValue(Math.abs(this.getValue(i)), i);
        }
        return v;
    }

    public double getNonDistanceMagnitude(){
        double sum = 0;
        for(int i=0;i<this.numberOfDimensions;i++){
            sum+= this.positionIndexes[i];
        }
        return sum;
    }

    public Vector fixVector(double []min, double []max){
        for(int i =0;i< this.getNumberOfDimensions();i++){
            this.positionIndexes[i] = Validator.validatePosition(min[i], max[i],this.positionIndexes[i]);
        }
        return this;
    }

    public boolean isEqual(Vector v){
        if(v.numberOfDimensions != this.numberOfDimensions){
            return false;
        }

        for(int i=0;i<this.numberOfDimensions;i++){
            if(this.positionIndexes[i] != v.positionIndexes[i]){
                return false;
            }else {
                continue;
            }
        }
        return true;
    }
}
