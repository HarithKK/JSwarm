# JarSwarm

JarSwarm is the java based natural inspired optimization library. This is intended for 
researches, software engineers, developers and students who would like to use natural inspired optimization
to solve their optimization problems. 

![JAVA Version][java-image]


## Implemented Algorithms

1. [PSO (Particle Swarm Optimization)](https://github.com/kolithawarnakulasooriya/JarSwarm/blob/mbo/src/main/java/org/usa/soc/pso/ReadMe.md)
2. [ACO (Ant Colony Optimization)](https://github.com/kolithawarnakulasooriya/JarSwarm/blob/mbo/src/main/java/org/usa/soc/aco/ReadMe.md)


### MBO (Ant Colony Optimization)

Developed by Abbas [[5]](#5) in 2001 based on the marriage behaviour of honey bees. 

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
Double bestOptimizedValue = p.getBestValue();
List<Double> bestOptimizedLocations = p.getBest().toList();
```

## Testing Benchmark Functions

Testing the precision. robustness and general performance of optimization algorithms [[2]](#2).

### Single Objective

1. Rastrigin function
2. Ackley function
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
<a id="3">[3]</a> Dorigo, Marco, Mauro Birattari, and Thomas Stutzle. "Ant colony optimization." IEEE computational intelligence magazine 1.4 (2006): 28-39.
<a id="4">[4]</a> Toksari, M. Duran. "Ant colony optimization for finding the global minimum." Applied Mathematics and computation 176.1 (2006): 308-316.
<a id="5">[5]</a> Abbass, Hussein A. "MBO: Marriage in honey bees optimization-A haplometrosis polygynous swarming approach." Proceedings of the 2001 congress on evolutionary computation (IEEE Cat. No. 01TH8546). Vol. 1. IEEE, 2001.

[java-image]: https://img.shields.io/badge/dynamic/xml?color=red&label=java&query=1.8&url=https%3A%2F%2Fopenjdk.java.net%2Fprojects%2Fjdk8%2F
