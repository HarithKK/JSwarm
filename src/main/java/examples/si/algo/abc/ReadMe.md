# ABC (Artificial Bee Colony)

Developed by araboga, Dervis, and Bahriye  [[1]](#1) in 2009 based on the nectar searching behaviours of honey bees.

```
ABC p = new ABC(
                <ObjectiveFunction>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Number of Food Sources>
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Max Trial Count>
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Karaboga, Dervis, and Bahriye Akay. "A comparative study of artificial bee colony SIAlgorithm." Applied mathematics and computation 214.1 (2009): 108-132.