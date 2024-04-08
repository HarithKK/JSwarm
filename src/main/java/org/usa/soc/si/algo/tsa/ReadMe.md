# TSA (Tunicate Swarm Algorithm:)

Developed by Benyamin Satnam Kaura, Lalit K. Awasthia, A.L. Sangala, Gaurav Dhiman  [[1]](#1) in 2020 based on the  imitates jet propulsion and swarm behaviors of tunicates during the navigation and foraging process.

```
TSA p = new TSA(
                <ObjectiveFunction>,
                <Number of Tunicates>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Kaur, Satnam, et al. "Tunicate Swarm Algorithm: A new bio-inspired based metaheuristic paradigm for global optimization." Engineering Applications of Artificial Intelligence 90 (2020): 103541.
