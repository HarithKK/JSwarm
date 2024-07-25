# SSA (Squirrel Search Algorithm)

Developed by Benyamin Mohit Jaina, Vijander Singha, Asha Rania  [[1]](#1) in 2019 based on the movement using gliding technique for finding food source.

```
SSA p = new SSA(
                <ObjectiveFunction>,
                <Number of Squirrels>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <Pdp>,
                <Gc>,
                <Air Density>,
                <Speed>,
                <Surface Body Area>,
                <Loosing height>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Jain, Mohit, Vijander Singh, and Asha Rani. "A novel nature-inspired SIAlgorithm for optimization: Squirrel search SIAlgorithm." Swarm and evolutionary computation 44 (2019): 148-175.
