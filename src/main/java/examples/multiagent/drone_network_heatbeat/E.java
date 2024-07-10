package examples.multiagent.drone_network_heatbeat;

import java.util.Arrays;

class E {
    int[] A;
    int[] B;

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
