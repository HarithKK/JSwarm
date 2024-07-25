# GOA (Grasshopper Optimisation Algorithm)

Developed by Benyamin Shahrzad Saremi , Seyedali Mirjalili , Andrew Lewis  [[1]](#1) in 2019 based on the behaviour of grasshopper swarms in nature for solving optimisation problems.

```
GOA p = new GOA(
                <ObjectiveFunction>,
                <Number of Squirrels>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <cmin>,
                <cmax>,
                <Intensity Of Attraction>,
                <Attraction Length>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Saremi, Shahrzad, Seyedali Mirjalili, and Andrew Lewis. "Grasshopper optimisation SIAlgorithm: theory and application." Advances in engineering software 105 (2017): 30-47.
