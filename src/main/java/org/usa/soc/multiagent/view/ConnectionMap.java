package org.usa.soc.multiagent.view;

import org.usa.soc.core.AbsAgent;

public class ConnectionMap {
    public AbsAgent from, to;

    public ConnectionMap(AbsAgent from, AbsAgent to){
        this.from = from;
        this.to = to;
    }
}
