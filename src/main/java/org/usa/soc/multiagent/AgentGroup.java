package org.usa.soc.multiagent;

import examples.si.algo.alo.Ant;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.SeriesData;
import org.usa.soc.core.ds.SeriesDataObject;
import org.usa.soc.si.AgentComparator;
import org.usa.soc.util.Mathamatics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AgentGroup extends SeriesData {
    private List<AbsAgent> agents;

    public AgentGroup(String name){

        super(name);
        this.agents = new ArrayList<>();
    }

    public void addAgent(AbsAgent agent){
        this.agents.add(agent);
    }

    public List<AbsAgent> getAgents() {
        return agents;
    }

    public void setAgents(List<AbsAgent> agents) {
        this.agents = agents;
    }

    public void makeStep(Margins m) {
        for(int i=0 ;i< agents.size(); i++){
            AbsAgent agent = agents.get(i);
            if(Agent.class.isInstance(agent))
                ((Agent)agent).step();
        }
    }

    public int getAgentsCount(){ return agents.size(); }

    public SeriesDataObject getLocations(){
        SeriesDataObject obj = new SeriesDataObject(getAgentsCount());
        for(int i=0; i< getAgentsCount(); i++){
            AbsAgent agent = this.agents.get(i);
            obj.addXY(i, agent.getPosition());
        }
        return obj;
    }

    public void sort(Comparator comparator){
        Collections.sort(agents, comparator);
    }

    public void removeAgent(Agent agent){
        this.agents.remove(agent);
    }
}
