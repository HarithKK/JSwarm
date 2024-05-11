# AVOA (African Vultures Optimization Algorith)

Developed by Benyamin Abdollahzadeha, Farhad Soleimanian Gharehchopogha, Seyedali Mirjalili  [[1]](#1) in 2021 based on the effective style of capturing the prey.

```
AVOA p = new AVOA(
                <ObjectiveFunction>,
                <Number of Vultures>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <Omega>,
                <Alpha>,
                <Beta>,
                <P1>,
                <P2>,
                <P3>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Abdollahzadeh, Benyamin, Farhad Soleimanian Gharehchopogh, and Seyedali Mirjalili. "African vultures optimization algorithm: A new nature-inspired metaheuristic algorithm for global optimization problems." Computers & Industrial Engineering 158 (2021): 107408..
