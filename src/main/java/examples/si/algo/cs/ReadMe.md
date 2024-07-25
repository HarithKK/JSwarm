# CS (Cuckoo Search Algorithm)

Developed by Yang and Deb  [[1]](#1) in 2009 based on the breeding behaviours of cuckoo birds.
```
ACO p = new CS(
            <ObjectiveFunction>,
            <Number of Iterations>,
            <Number of Dimensions>,
            <Number of Nests>,
            <Minimum Boundary of Variables>,
            <Maximum Boundary of Variables>,
            <Alpha>,
            <Pa>,
            <Whether you want to result global minima or global maxima>
    );

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> X. -S. Yang and Suash Deb, "Cuckoo Search via Lévy flights," 2009 World Congress on Nature & Biologically Inspired Computing (NaBIC), 2009, pp. 210-214, doi: 10.1109/NABIC.2009.5393690.
