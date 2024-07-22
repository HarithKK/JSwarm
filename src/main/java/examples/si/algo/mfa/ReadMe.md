# MFA (Moth-Flame Optimization Algorithm)

Developed by Mirjalili  [[1]](#1) in 2015 based on the pattern of moving moths around flames. 

```
MFA p = new MFA(
                <ObjectiveFunction>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Number of Moths>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <b>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Mirjalili, S. (2015). Moth-flame optimization algorithm: A novel nature-inspired heuristic paradigm. Knowledge-based systems, 89, 228-249.