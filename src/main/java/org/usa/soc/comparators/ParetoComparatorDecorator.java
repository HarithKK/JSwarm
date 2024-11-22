package org.usa.soc.comparators;

import java.util.Comparator;

public class ParetoComparatorDecorator<T>  {
    ParetoComparator<T> comparators;
    public ParetoComparatorDecorator(){
        comparators = new ParetoComparator<>();
    }

    public ParetoComparatorDecorator<T> add(Comparator<T> comparator){
        comparators.add(comparator);
        return this;
    }

    public ParetoComparator<T> build(){
        return comparators;
    }
}
