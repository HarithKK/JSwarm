# FA (Firefly Algorithms)

Developed by Yang and Xin-She  [[1]](#1) in 2009 based on the attractiveness of fireflies to the light. This is more focused on multi-model problems.

```
FA p = new FA(
                <ObjectiveFunction>,
                <Number of Dimensions>,
                <Number of Flies>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Charasteristic length>,
                <Alpha>
                <Initial attractiveness>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Yang, Xin-She. "Firefly algorithms for multimodal optimization." International symposium on stochastic algorithms. Springer, Berlin, Heidelberg, 2009.