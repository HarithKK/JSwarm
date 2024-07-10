package examples.multiagent.common;

import java.util.Arrays;

public class E {
    public int[] A;
    public int[] B;

    public E(int[] A, int[] B) {
        this.A = A;
        this.B = B;
    }

    public E(int count){
        this.A = new int[count];
        this.B = new int[count];

        Arrays.fill(this.A, 0);
        Arrays.fill(this.B, 0);
    }
}
