# WSO (Wasp Swarm Optimization)

Developed by Cicirello and Smith [[1]](#1) in 2004 based on the fighting behaviour of wasp colony to
optimize the job assignment problem in factories. Later on Pedro, Thomas, and Sousa modified it to solve max-sat problem [[2]](#2).

```
WSO p = WSO(<ObjectiveFunction>
           <Number of Iterations>,
           <Number of Wasps>,
           <Number of Dimensions>,
           <Minimum Boundary of Variables>,
           <Maximum Boundary of Variables>,
           c1,
           c2,
           <Whether you want to result global minima or global maxima>);

p.initialize();
p.runOptimizer();
Double bestOptimizedValue = p.getBestDoubleValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## References
<a id="1">[1]</a> Cicirello, Vincent A., and Stephen F. Smith. "Wasp-like agents for distributed factory coordination." Autonomous Agents and Multi-agent systems 8.3 (2004): 237-266.
<a id="2">[2]</a> Pinto, Pedro C., Thomas A. Runkler, and João Sousa. "Wasp swarm SIAlgorithm for dynamic MAX-SAT problems." International conference on adaptive and natural computing algorithms. Springer, Berlin, Heidelberg, 2007.
