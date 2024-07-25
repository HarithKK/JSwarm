# GEO (Golden Eagle Optimizer )

Developed by Abdolkarim Mohammadi-Balania, Mahmoud Dehghan Nayeria, Adel Azara, Mohammadreza and Taghizadeh-Yazdib [[1]](#1) in 2021 based on the effective style of capturing and memorizing the prey.

```
GEO p = new GEO(
                <ObjectiveFunction>,
                <Number of Eagles>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <Pa0>,
                <PaT>,
                <Pc0>,
                <PcT>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Mohammadi-Balani, Abdolkarim, et al. "Golden eagle optimizer: A nature-inspired metaheuristic SIAlgorithm." Computers & Industrial Engineering 152 (2021): 107050.
