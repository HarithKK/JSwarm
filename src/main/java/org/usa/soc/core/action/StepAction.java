package org.usa.soc.core.action;

import org.usa.soc.core.ds.Vector;

public abstract class StepAction {
    public abstract void performAction(Vector best, Double bestValue, int step);
}
