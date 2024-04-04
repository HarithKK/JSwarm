# BA (Bat-Inspired Algorithm)

Developed by Yang  [[1]](#1) in 2010 based on the foraging behaviour of bats using sound frequency.

```
BA p = new BA(
                <ObjectiveFunction>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Number of Bats>,
                <Min Frequency>,
                <Max Frequency>,
                <Alpha>,
                <Gamma>,
                <A0>
                <R0>
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Yang, XS. (2010). A New Metaheuristic Bat-Inspired Algorithm. In: González, J.R., Pelta, D.A., Cruz, C., Terrazas, G., Krasnogor, N. (eds) Nature Inspired Cooperative Strategies for Optimization (NICSO 2010). Studies in Computational Intelligence, vol 284. Springer, Berlin, Heidelberg. https:
//doi.org/10.1007/978-3-642-12538-6_6