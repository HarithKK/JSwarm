# CHOA (Chimp Optimization Algorithm)

Developed by Benyamin M.Khishe, M.R.Mosavi  [[1]](#1) in 2020 based on the individual intelligence and sexual motivation of chimps in their group hunting, which is
different from the other social predators

```
CHOA p = new CHOA(
                <ObjectiveFunction>,
                <Number of Chimps>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>
                <Upper limit of F value>,
                <Chaotic Type>
                );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

There are six chaotic types.

1. Chaotics.type.QUADRATIC
2. Chaotics.type.BERNOULLI
3. Chaotics.type.GAUSS_MOUSE
4. Chaotics.type.LOGISTIC
5. Chaotics.type.SINGER
6. Chaotics.type.TENT

## References
<a id="1">[1]</a> Khishe, Mohammad, and Mohammad Reza Mosavi. "Chimp optimization SIAlgorithm." Expert systems with applications 149 (2020): 113338.
