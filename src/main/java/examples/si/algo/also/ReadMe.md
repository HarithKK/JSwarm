# ALSO (Artificial Lizard Search Optimization )

Developed by Neetesh Kumar, Navjot Singh, Deo Prakash Vidyarthi  [[1]](#1) in 2021 based on the effective style of capturing the prey.

```
ALSO p = new ALSO(
                <ObjectiveFunction>,
                <Number of Lizards>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <Total Mass>,
                <Total Length>,
                <C1>,
                <C2>,
                <Body Inertia>,
                <Tail Inertia>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Kumar, Neetesh, Navjot Singh, and Deo Prakash Vidyarthi. "Artificial lizard search optimization (ALSO): A novel nature-inspired meta-heuristic SIAlgorithm." Soft Computing 25.8 (2021): 6179-6201.
