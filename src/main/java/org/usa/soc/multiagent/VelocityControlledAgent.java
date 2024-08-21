package org.usa.soc.multiagent;

public class VelocityControlledAgent extends Agent {

    Double roll, pitch, yaw, motorPower = 10.0;
    double startingAng = 0;

    public void init(double x, double y, double ang){
        updatePosition(x, y);
        this.startingAng = ang;
    }

    public void setAngles(double roll, double pitch, double yaw){
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void setMotorPower(double v){
        this.motorPower = v;
    }

    @Override
    public void step() {

        double vy_r = motorPower * roll;
        double vx_r = motorPower * pitch;

        double vx = vx_r * Math.cos(startingAng - yaw) - vy_r * Math.cos((Math.PI/2 - (startingAng - yaw)));
        double vy = vx_r * Math.sin(startingAng - yaw) - vy_r * Math.sin((Math.PI/2 - (startingAng - yaw)));
        this.updatePosition(getX()+vx, getY()+vy);
    }
}
