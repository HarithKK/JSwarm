package FunctionsFactory;

import examples.si.benchmarks.singleObjective.SphereFunction;
import org.junit.jupiter.api.Test;
import org.usa.soc.si.runners.FunctionsFactory;
import org.usa.soc.si.runners.Main;

public class TestFactory {

    public static void main(String[] args) {
        Main.executeMain(new FunctionsFactory().register(new SphereFunction()).build());
    }
}
