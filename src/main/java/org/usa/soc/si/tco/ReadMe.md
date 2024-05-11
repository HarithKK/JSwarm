# TCO (Termite Colony Optimization)

Developed by Ramin Hedayatzadeh, Foad Akhavan Salmassi, Manijeh Keshtgari, Reza Akbari and Koorush Ziarati  [[1]](#1) in 2010 based on the decision-making behaviour of termite movements.

```
TCO p = new TCO(
                <ObjectiveFunction>,
                <Number of Iterations>,
                <Number of Dimensions>,
                <Number of Termites>
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Initial pheramone value>,
                <Evaporation rate>,
                <Sensing radius>,
                <Social constants>,
                <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> R. Hedayatzadeh, F. Akhavan Salmassi, M. Keshtgari, R. Akbari and K. Ziarati, "Termite colony optimization: A novel approach for optimizing continuous problems," 2010 18th Iranian Conference on Electrical Engineering, 2010, pp. 553-558, doi: 10.1109/IRANIANCEE.2010.5507009.