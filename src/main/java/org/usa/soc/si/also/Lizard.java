package org.usa.soc.si.also;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Lizard {

    private Vector position;
    private double bodyAngle, directiveBodyAngle;
    private double tailAngle, directiveTailAngle;
    private double fitnessValue;
    private double tourque;
    private Vector lbest;
    private double lbestValue;
    private double It;
    private double Ib;
    private double[] minBoundary, maxBoundary;
    double lb, lt, mb, mt;
    double a,b,c,d,e,f,g;

    public Lizard(int numberOfDimensions, double[] minBoundary, double[] maxBoundary, double lb, double lt, double mb, double mt, double Ib, double It) {
        this.maxBoundary =maxBoundary;
        this.minBoundary = minBoundary;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1);
        this.lbest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1);
        this.setBodyAngle(Randoms.rand(-45, 45));
        this.setTailAngle(Randoms.rand(-90, 90));
        this.directiveBodyAngle = Randoms.rand(-10, 10);
        this.directiveTailAngle = Randoms.rand(-10, 10);
        this.Ib = Ib;
        this.It = It;
        this.setTourque(Randoms.rand(0,1));
        this.lb = lb;
        this.lt = lt;
        this.mb = mb;
        this.mt = mt;
    }

    public void calculateValues(double m, double l){

        if((Ib == mb * lb *lb) && (It == mt * lt* lt)){
            calculateC3(m,l);
        }else if(m == 1 && l == 1){
            calculateC4();
        }else{
            calculateC2();
        }
    }

    public void calculateAngle(){
        bodyAngle = Validator.validatePosition(-45, 45, directiveBodyAngle);
        directiveBodyAngle = Validator.validatePosition(-10, 10,(directiveBodyAngle * directiveBodyAngle - b) / ( d- e) + (tourque * (f / ( d-e))));
        tailAngle = Validator.validatePosition(-45, 45, directiveTailAngle);
        directiveTailAngle = Validator.validatePosition(-10, 10, (((-(directiveTailAngle*directiveTailAngle))+ c)/(d-e)) + (tourque * (g/(d-e))));
    }

    private void calculateC2(){
        a = 0.5 * lb * lb * lt * lt * mb* mb*mt*mt*Math.sin(2*(Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle)));
        b = ((lt*lt*mb*mt) + ((mb + mt)*It))*lb*lt*mb*mt*directiveTailAngle*directiveTailAngle*Math.sin(Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle));
        c = ((lb*lb*mb*mt) + ((mb + mt)*Ib))*lb*lt*mb*mt*directiveTailAngle*directiveTailAngle*Math.sin(Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle));
        d = lb * lb * lt * lt * mb* mb*mt*mt*Math.pow(Math.cos(Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle)), 2);
        e = ((lb*lb*mb*mt) + Ib*(mb+mt))*((lt*lt*mb*mt) + It*(mb+mt));
        f = ((lt*lt*mb*mt) + It*(mb+mt)) - (lb*lt*mb*mt)*Math.cos(Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle))*(mb+mt);
        g = ((lb*lb*mb*mt) + Ib*(mb+mt)) - (lb*lt*mb*mt)*Math.cos(Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle))*(mb+mt);
    }

    private void calculateC3(double m, double l){
        double angleDiff = Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle);
       a = Math.pow(m,4)*Math.pow(l, 4)*Math.sin(2*angleDiff)/5000;
       b = Math.pow(m,4)*Math.pow(l, 4)*Math.pow(directiveTailAngle,2)*Math.sin(angleDiff)*19/11250;
       c = Math.pow(m,4)*Math.pow(l, 4)*Math.pow(directiveTailAngle,2)*Math.sin(angleDiff)*11/5000;
       d = Math.pow(m,4)*Math.pow(l, 4)*Math.pow(Math.cos(angleDiff),2)/2500;
       e = Math.pow(m,4)*Math.pow(l, 4)*209/22500;
       f = Math.pow(m,3)*Math.pow(l, 2)*(56 - (9*Math.cos(angleDiff)))/900;
       g = Math.pow(m,3)*Math.pow(l, 2)*(11 - (2*Math.cos(angleDiff)))/100;
    }

    private void calculateC4(){
        double angleDiff = Math.toRadians(this.bodyAngle) - Math.toRadians(this.tailAngle);
        a = Math.sin(2*angleDiff);
        b = Math.pow(directiveTailAngle,2)*Math.sin(angleDiff);
        c = Math.pow(directiveTailAngle,2)*Math.sin(angleDiff);
        d = Math.pow(Math.cos(angleDiff),2);
        e = 1;
        f = (56 - (9*Math.cos(angleDiff)));
        g = (11 - (2*Math.cos(angleDiff)));
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public Vector getPosition() {
        return this.position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position.fixVector(minBoundary, maxBoundary));
    }

    public Vector getLbest() {
        return lbest.getClonedVector();
    }

    public void setLbest(Vector lbest) {
        this.lbest.setVector(lbest.fixVector(minBoundary, maxBoundary));
    }

    public double getTourque() {
        return tourque;
    }

    public void setTourque(double tourque) {
        this.tourque = tourque;
    }

    public void generateNewTorque(double globalBest, double globalWorst) {
        double qValue = 0;
        if(fitnessValue != lbestValue){
            qValue = (globalBest - fitnessValue) / (globalBest - globalWorst);
        }else{
            qValue = tourque;
        }
        this.tourque = Randoms.randAny(0, qValue);
    }

    public double getBodyAngle() {
        return bodyAngle;
    }

    public void setBodyAngle(double bodyAngle) {
        this.bodyAngle = bodyAngle;
    }

    public double getTailAngle() {
        return tailAngle;
    }

    public void setTailAngle(double tailAngle) {
        this.tailAngle = tailAngle;
    }

    public double getLbestValue() {
        return lbestValue;
    }

    public void setLbestValue(double lbestValue) {
        this.lbestValue = lbestValue;
    }
}
