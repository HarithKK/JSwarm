# JarSwarm

JarSwarm is the java based natural inspired optimization library. This is intended for 
researches, software engineers, developers and students who would like to use natural inspired optimization
to solve their optimization problems. 

![JAVA Version][java-image]


## Implemented Algorithms

### PSO (Particle Swarm Optimization)

Developed by Kennedy, James, and Russell Eberhart [[1]](#1) in 1995 based on the movements of birds
and fishes. 

```
PSO p = new PSO(
                <ObjectiveFunction>,
                <Number of Particles>,
                <Number of Dimensions>,
                <Number of Iterations>,
                <c1>,
                <c2>,
                <w>,
                <Minimum Boundary of Variables>,
                <Maximum Boundary of Variables>,
                <Whether you want to result global minima or global maxima>);

p.runOptimizer();
Double bestOptimizedValue = p.getGBestValue();
List<Double> bestOptimizedLocations = p.getGBest().toList();
```

## Testing Benchmark Functions

Testing the precision. robustness and general performance of optimization algorithms [[2]](#2).

### Single Objective

1. Rastrigin function
2. Ackley function	Ackley's 
3. Sphere function	
4. Rosenbrock function	
5. Beale function
6. Goldstein–Price function
7. Booth function
8. Bukin function
9. Matyas function
10. Lévi function
11. Himmelblau's function
12. Easom function
13. ggholder function
14. cCormick function
15. Schaffer function N. 2
16. Schaffer function N. 4
17. Styblinski–Tang function

## Installing and Using the Library

TBD : We hope to move the library to the Maven central dependency repository. 

## Contributing

This is an open source project. 

TBD : We will provide you the contribution guidelines soon.



## References
<a id="1">[1]</a> Kennedy, James, and Russell Eberhart. "Particle swarm optimization." Proceedings of ICNN'95-international conference on neural networks. Vol. 4. IEEE, 1995.
<a id="2">[2]</a> https://en.wikipedia.org/wiki/Test_functions_for_optimization


[java-image]: https://
