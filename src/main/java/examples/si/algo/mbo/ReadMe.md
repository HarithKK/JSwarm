# MBO (Marriage in Honey Bees Optimization)

Developed by Abbas [[1]](#1) in 2001 based on the marriage behaviour of honey bees. 

```
MBO p = new MBO(
                <ObjectiveFunction>,
                <Number of Workers>,
                <Number of Drones>,
                <Number of Queens>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>,
                <Alpha>
                <Minimam Queen Speed>,
                <Maximum Queen Speed>,
                <Selection probability of a drone>,
                <Mutation probability>,
                <Number of Mutations at One iteration>,
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

**Note: This one should much improvements, Not Recommend to use**

## References
<a id="1">[1]</a> Abbass, Hussein A. "MBO: Marriage in honey bees optimization-A haplometrosis polygynous swarming approach." Proceedings of the 2001 congress on evolutionary computation (IEEE Cat. No. 01TH8546). Vol. 1. IEEE, 2001.
