# ALO (Ant Lion Optimization Algorithm)

Developed by Mirjalili  [[1]](#1) in 2015 based on the hunting behaviour of ant lions.

```
ALO p = new ALO(
                <ObjectiveFunction>,
                <Number of Ants>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Mirjalili, S. (2015). The ant lion optimizer. Advances in engineering software, 83, 80-98.