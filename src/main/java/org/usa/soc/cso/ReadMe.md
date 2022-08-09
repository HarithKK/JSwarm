# CSO.CSO (Cat Swarm Optimization)

Developed by  Chu, Shu-Chuan, Pei-Wei Tsai, and Jeng-Shyang Pan  [[1]](#1) in 2006 based on the searching behaviour and tracing a prey behaviour of cats.

```
CSO.CSO p = new CSO.CSO(
                <ObjectiveFunction>,
                <Number of Dimensions>,
                <Number of Iterations>,
                <Number of Cats>,
                <Seekers Tracers Ratio>
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <SMP>,
                <CDC>,
                <SRD>,
                <SPC>,
                <C>
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Chu, Shu-Chuan, Pei-Wei Tsai, and Jeng-Shyang Pan. "Cat swarm optimization." Pacific Rim international conference on artificial intelligence. Springer, Berlin, Heidelberg, 2006.