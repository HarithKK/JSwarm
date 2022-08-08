# MS (Monkey Search Optimization)

Developed by Mucherino, Antonio, and Seref  [[1]](#1) in 2007 based on the food searching behaviours of monkeys.

```
MS p = new MS(
            boolean isLocalMinima
                <ObjectiveFunction>,
                <Number of Iterations>,
                <Number of Monkeys>,
                <Number of Dimensions>,
                <Number of Max Height of Tree>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <C1>
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Mucherino, Antonio, and Onur Seref. "Monkey search: a novel metaheuristic search for global optimization." AIP conference proceedings. Vol. 953. No. 1. American Institute of Physics, 2007.
