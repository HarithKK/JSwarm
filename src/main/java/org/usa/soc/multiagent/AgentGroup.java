package org.usa.soc.multiagent;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.SeriesData;
import org.usa.soc.core.ds.SeriesDataObject;

import java.util.List;

public class AgentGroup extends SeriesData {
    private List<Agent> agents;

    public AgentGroup(String name){
        super(name);
    }

    public void addAgent(Agent agent){
        this.agents.add(agent);
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public void makeStep(Margins m) {
        for(int i=0 ;i< agents.size(); i++){
            Agent agent = agents.get(i);
            agent.step();
        }
    }

    public int getAgentsCount(){ return agents.size(); }

    public SeriesDataObject getLocations(){
        SeriesDataObject obj = new SeriesDataObject(getAgentsCount());
        for(int i=0; i< getAgentsCount(); i++){
            Agent agent = this.agents.get(i);
            obj.addXY(i, agent.getX(), agent.getY());
        }
        return obj;
    }

}
