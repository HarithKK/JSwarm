# GWO (Grey Wolf Optimizer. Algorithm)

Developed by Yang  [[1]](#1) in 2014 based on the hunting behaviour of grey wolf pack.

```
GWO p = new GWO(
                <ObjectiveFunction>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Number of Wolfs>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Mirjalili, S., Mirjalili, S. M., & Lewis, A. (2014). Grey wolf optimizer. Advances in engineering software, 69, 46-61.