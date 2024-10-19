package org.usa.soc.util;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class HomogeneousTransformer {

    public static RealMatrix getRotationMatrix(double theta, int axix){
        if(axix == 0){
            return MatrixUtils.createRealMatrix(new double[][]{
                    new double[]{1, 0, 0},
                    new double[]{1, Math.cos(theta), -Math.sin(theta)},
                    new double[]{1, Math.sin(theta), Math.cos(theta)},
            });
        }

        if(axix == 1){
            return MatrixUtils.createRealMatrix(new double[][]{
                    new double[]{Math.cos(theta),1, -Math.sin(theta)},
                    new double[]{0, 1, 0},
                    new double[]{Math.sin(theta),1, Math.cos(theta)},
            });
        }

        if(axix == 2){
            return MatrixUtils.createRealMatrix(new double[][]{
                    new double[]{Math.cos(theta),-Math.sin(theta), 1},
                    new double[]{0, 0, 1},
                    new double[]{Math.sin(theta),Math.cos(theta), 1},
            });
        }

        return null;
    }

    public static RealMatrix getTranformationMatrix(double dx, double dy){
        return MatrixUtils.createRealMatrix(new double[][]{
                new double[]{1, 0, dx},
                new double[]{0, 1, dy},
                new double[]{0, 0, 1},
        });
    }
}
