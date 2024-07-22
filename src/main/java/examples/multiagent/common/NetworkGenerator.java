package examples.multiagent.common;

import examples.multiagent.drone_network_heatbeat.DroneAgent;
import examples.multiagent.common.E;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.Randoms;

import java.util.*;

public class NetworkGenerator {

    double radius;

    Set<Double> xes = new HashSet<>();
    Set<Double> yes = new HashSet<>();

    HashMap<Integer, DroneAgent> dronesMap = new HashMap<>();

    double U = 0.3;

    double C = 20.0;

    int MaximumNumberOfConnections = 3;

    double leaderX;

    public NetworkGenerator(double leaderX, double leaderY, int count, double radius, Margins m){
        this.radius = radius;
        this.leaderX = leaderX;

        int index = 0;

        Queue<DroneAgent> queue = new ArrayDeque<>();
        queue.add(new DroneAgent(index, m, leaderX, leaderY, 0, new E(count+1)));

        while(!queue.isEmpty()){
            DroneAgent a = queue.poll();

            for(int i=0; i<MaximumNumberOfConnections; i++){
                if(index < count){
                    Vector v = generateAPositionVector(a.getX(), a.getY(), radius).operate(Vector.OPERATOR.MULP, 1.0);
                    DroneAgent b = new DroneAgent(++index, m, v.getValue(0), v.getValue(1), a.layer+1, new E(count+1));

                    if(a.edge.A[b.index] == 0 && b.edge.A[a.index] == 0){
                        a.setA(b.index, 1);
                        b.setA(a.index, 1);

                        a.setB(b.index, 1);
                        b.setB(a.index, -1);
                    }
                    queue.add(b);
                }
            }
            dronesMap.put(a.index, a);
        }

      //  reArrangeSystem(20.0, 50.0);

       // printAll(this.dronesMap);
    }

    private void reArrangeSystem(double xd, double yd) {
        int maxLayer = 0;
        for(DroneAgent a: dronesMap.values()){
            if(a.layer > maxLayer){
                maxLayer = a.layer;
            }
            a.setY(leaderX - a.layer*yd);
        }

        for(int i=1; i<=maxLayer; i++){

            double leftMostX = Double.MAX_VALUE;
            PriorityQueue<DroneAgent> pq = new PriorityQueue<>((a,b) -> {
                if(a.getX() < b.getX()){
                    return -1;
                } else if (a.getX() > b.getX()) {
                    return 1;
                }else{
                    return 0;
                }
            });

            for(DroneAgent a: dronesMap.values()){
                if(a.layer == i){
                    pq.add(a);
                    if(a.getX() < leftMostX){
                        leftMostX = a.getX();
                    }
                }
            }

            leftMostX = leaderX - (xd * pq.size()/2);

            while(!pq.isEmpty()){
                DroneAgent a = pq.poll();
                a.setX(leftMostX);
                leftMostX += xd;
            }

        }


    }

    public static void printAll(Map<Integer, DroneAgent> dronesMap){
        System.out.println("A ->");
        for(DroneAgent a: dronesMap.values()){
            System.out.print("[ ");
            for(int j : a.edge.A){
                System.out.print(j + ",");
            }
            System.out.println("]");
        }

        System.out.println("B ->");
        for(DroneAgent a: dronesMap.values()){
            System.out.print("[ ");
            for(int j : a.edge.B){
                System.out.print(j + ",");
            }
            System.out.println("]");
        }
    }

    public HashMap<Integer, DroneAgent> getDroneMap(){
        return dronesMap;
    }

    public Vector generateAPositionVector(double xc, double yc, double radius){

        for(int i=0; i< 10000; i++){
            double a = ((-1 + U) * Math.PI * Randoms.getDoubleRand()) - (U + 0.15) ;
            double r = radius * Math.sqrt(Randoms.getDoubleRand());
            double x = r * Math.cos(a) + xc;
            double y = r * Math.sin(a) + yc;

            if(xes.contains(x) || yes.contains(y)){
                continue;
            }
            for(double p=x-C; p < x+C; p++){
                xes.add(p);
            }
            for(double q=y-C; q < y+C; q++){
                yes.add(q);
            }
            return new Vector(2).setValues(new double[]{x, y});
        }
        return null;
    }

    public static HashMap<Integer, DroneAgent> generateStatic(Margins m, double ix, double iy){
        HashMap<Integer, DroneAgent> dronesMap = new HashMap<>();

        dronesMap.put(
                0,
                new DroneAgent(0, m, ix, iy, 0, new E(new int[]{0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                1,
                new DroneAgent(1, m, ix-10, iy-30, 1, new E(new int[]{1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0}, new int[]{-1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                2,
                new DroneAgent(2, m, ix, iy-30, 1, new E(new int[]{1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0}, new int[]{-1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0}))
        );
        dronesMap.put(
                3,
                new DroneAgent(3, m, ix+10, iy-30, 1, new E(new int[]{1, 0, 1, 0, 0, 0, 0,0, 0, 1, 1, 1}, new int[]{-1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}))
        );
        dronesMap.put(
                7,
                new DroneAgent(7, m, ix-3, iy-60, 2, new E(new int[]{0, 0, 1, 0, 0, 0, 1,0, 1, 0, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                8,
                new DroneAgent(8, m, ix+3, iy-60, 2, new E(new int[]{0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                4,
                new DroneAgent(4, m, ix-20, iy-60, 2, new E(new int[]{0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                5,
                new DroneAgent(5, m, ix-15, iy-60, 2, new E(new int[]{0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                6,
                new DroneAgent(6, m, ix-10, iy-60, 2, new E(new int[]{0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                9,
                new DroneAgent(9, m, ix+10, iy-60, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))

        );
        dronesMap.put(
                10,
                new DroneAgent(10, m, ix+15, iy-60, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                11,
                new DroneAgent(11, m, ix+20, iy-60, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))
        );

        return dronesMap;
    }

    public static HashMap<Integer, DroneAgent> generateStaticlayers4(Margins m, double ix, double iy){
        HashMap<Integer, DroneAgent> dronesMap = new HashMap<>();

        dronesMap.put(
                0,
                new DroneAgent(0, m, ix, iy, 0, new E(new int[]{0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                1,
                new DroneAgent(1, m, ix-10, iy-30, 1, new E(new int[]{1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, new int[]{-1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                2,
                new DroneAgent(2, m, ix+10, iy-30, 1, new E(new int[]{1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}, new int[]{-1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                3,
                new DroneAgent(3, m, ix-20, iy-70, 2, new E(new int[]{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}))
        );
        dronesMap.put(
                4,
                new DroneAgent(4, m, ix-10, iy-70, 2, new E(new int[]{0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}))
        );
        dronesMap.put(
                5,
                new DroneAgent(5, m, ix+10, iy-70, 2, new E(new int[]{0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 1, 0, 0}))
        );
        dronesMap.put(
                6,
                new DroneAgent(6, m, ix+20, iy-70, 2, new E(new int[]{0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 1, 0}))
        );
        dronesMap.put(
                7,
                new DroneAgent(7, m, ix-30, iy-110, 3, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 1}))
        );
        dronesMap.put(
                8,
                new DroneAgent(8, m, ix-20, iy-110, 3, new E(new int[]{0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0}, new int[]{0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                9,
                new DroneAgent(9, m, ix+20, iy-110, 3, new E(new int[]{0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0}, new int[]{0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0}))

        );
        dronesMap.put(
                10,
                new DroneAgent(10, m, ix+30, iy-110, 3, new E(new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0}))
        );
        dronesMap.put(
                11,
                new DroneAgent(11, m, ix-40, iy-150, 4, new E(new int[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0}))
        );

        return dronesMap;
    }

//    public static void main(String[] args) {
//        NetworkGenerator g = new NetworkGenerator(0, 0, 10, 20, new Margins(0,0,100,100));
//        g.getDroneMap();
//    }
}
