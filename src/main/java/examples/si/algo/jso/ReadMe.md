# JSO (Jellyfish Search Optimizer)

Developed by Benyamin Jui-Sheng Chou, Dinh-Nhat Truong  [[1]](#1) in 2020 based on the search behavior of jellyfish involves their following the ocean current, their mo-
tions inside a jellyfish swarm.

```
JSO p = new JSO(
                <ObjectiveFunction>,
                <Number of Jellifishes in swarm>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <Beta>,
                <Gamma>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Chou, Jui-Sheng, and Dinh-Nhat Truong. "A novel metaheuristic optimizer inspired by behavior of jellyfish in ocean." Applied Mathematics and Computation 389 (2021): 125535.
