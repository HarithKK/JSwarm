# ZOA (Zebra Optimization Algorithm)

Developed by Eva Trojovská, Mohammad Dehghani, Pavel Trojovský  [[1]](#1) in 2022 based on the herding movements of horses to save from predators.

```
ZOA p = new ZOA(
                <ObjectiveFunction>,
                <Number of Horses>,
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
<a id="1">[1]</a> E. Trojovská, M. Dehghani and P. Trojovský, "Zebra Optimization Algorithm: A New Bio-Inspired Optimization Algorithm for Solving Optimization Algorithm," in IEEE Access, vol. 10, pp. 49445-49473, 2022, doi: 10.1109/ACCESS.2022.3172789.
