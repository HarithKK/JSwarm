# PSO (Particle Swarm Optimization)

Developed by Kennedy, James, and Russell Eberhart [[1]](#1) in 1995 based on the movements of birds
and fishes. 

```
PSO p = new PSO(
                <ObjectiveFunction>,
                <Number of Particles>,
                <Number of Dimensions>,
                <Number of Iterations>,
                <c1>,
                <c2>,
                <w>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getGBestValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Kennedy, James, and Russell Eberhart. "Particle swarm optimization." Proceedings of ICNN'95-international conference on neural networks. Vol. 4. IEEE, 1995.
