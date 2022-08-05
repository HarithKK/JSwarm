# ACO (Ant Colony Optimization)

Developed by Dorigo, Birattari and Stutzle  [[1]](#1) in 2006 based on the food searching behaviours of ants. This is more 
focused on path finding problems and Toksari and Duran [[2]](#2) focused with optimization problems.

```
ACO p = new ACO(
                <ObjectiveFunction>,
                <Number of Ants>,
                <Number of Iterations>,
                <Number of Process Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Alpha>
                <Evaporation Rate>,
                <Initial Pheromone Value>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestValue();
List<Double> bestOptimizedLocations = p.getBest().toList();
```

## References
<a id="1">[1]</a> https://en.wikipedia.org/wiki/Test_functions_for_optimization
<a id="1">[1]</a> Dorigo, Marco, Mauro Birattari, and Thomas Stutzle. "Ant colony optimization." IEEE computational intelligence magazine 1.4 (2006): 28-39.
