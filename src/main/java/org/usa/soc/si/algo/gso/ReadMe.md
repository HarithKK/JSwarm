# GSO (Glowworm Swarm Optimization)

Developed by Krishnanand and Ghose  [[1]](#1) in 2009 based on the mating and following behaviours of glow worms.

```
GSO p = new GSO(
                <ObjectiveFunction>,
                <Number of Dimensions>,
                <Number of Iterations>,
                <Number of Worms>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Initial Luciferin Value l0>,
                <Initial Sensing Radius r0>,
                <Luciferin Decay Constant>,
                <Luciferin Enhance Constant>,
                <nt>,
                <rs>,
                <beta>,
                <s>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Krishnanand, K. N., and Debasish Ghose. "Glowworm swarm optimization for simultaneous capture of multiple local optima of multimodal functions." Swarm intelligence 3.2 (2009): 87-124.
