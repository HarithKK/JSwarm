package org.usa.soc.multiagent;

import examples.si.algo.ms.Monky;
import org.knowm.xchart.style.markers.Marker;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.Flag;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.exceptions.KillOptimizerException;
import org.usa.soc.util.Logger;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Algorithm{

    protected Flag isPaused = new Flag(), isKilled=new Flag(), initialized=new Flag();

    protected Map<String, AgentGroup> agents = new HashMap<>();

    protected int interval,stepsCount = -1;
    protected long currentStep;
    private Margins margins;

    protected long nanoDuration;

    private StepCompleted stepCompleted;

    public Algorithm(int interval){
        this.setInterval(interval);

        this.isPaused.unset();
        this.isKilled.unset();
        this.initialized.unset();
    }

    public Algorithm(){
        this.setInterval(0);

        this.isPaused.unset();
        this.isKilled.unset();
        this.initialized.unset();
    }

    public abstract void initialize();

    public abstract void step() throws Exception;

    public void setFirstAgents(String str,ArrayList<?> l){
        this.agents.put(str, new AgentGroup(str));
    }

    public List<AbsAgent> getFirstAgents(){
        return this.agents.get(agents.keySet().toArray()[0]).getAgents();
    }

    public void  runInitializer(){
        initialize();
        this.initialized.set();
    }
    public void run(long maxSteps) throws Exception{

        if(!this.initialized.isSet()){
            throw new RuntimeException("Initialize Failed");
        }

        if(agents.isEmpty()){
            throw new RuntimeException("No Agents Registered");
        }

        this.nanoDuration = System.nanoTime();
        for(long step = 0; step< maxSteps; step++){
            step();

            for(String key: this.agents.keySet()){
                for(AbsAgent agent: this.agents.get(key).getAgents()){
                    if(Agent.class.isInstance(agent))
                        ((Agent)agent).step();
                }
            }

            if(this.stepCompleted != null)
                this.stepCompleted.performAction(step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    public void run() throws Exception{

        if(!this.initialized.isSet()){
            throw new RuntimeException("Initialize Failed");
        }

        if(agents.isEmpty()){
            throw new RuntimeException("No Agents Registered");
        }

        long step = 0;
        this.nanoDuration = System.nanoTime();
        for(;;){
            if(stepsCount > 0 && step > stepsCount){
                break;
            }
            this.step();

            for(String key: this.agents.keySet()){
                try{
                    for (AbsAgent agent : this.agents.get(key).getAgents()) {
                        if(Agent.class.isInstance(agent))
                            ((Agent)agent).step();
                    }
                }catch (Exception e){
                    Logger.getInstance().error("Algorithm Error " +e.getMessage());
                }
            }

            if(this.stepCompleted != null)
                this.stepCompleted.performAction(step);
            stepCompleted(step);
            step++;
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
        this.executionCompleted();
    }

    public void executionCompleted(){
        return;
    }

    public void stepCompleted(long step) throws InterruptedException, KillOptimizerException {

        this.currentStep = step;

        if(this.getInterval() == 0){
            return;
        }else{
            Thread.sleep(this.getInterval());
        }

        if(this.isKilled.isSet()){
            this.isKilled.unset();
            throw new KillOptimizerException("Forcefully stopped!");
        }

        this.checkPaused();
    }

    protected AgentGroup addAgents(String key, Class<?> agent, int count) throws Exception{
        AgentGroup agentGroup = new AgentGroup(key);
        agentGroup.setAgents(createAgents(count, agent));
        this.agents.put(key, agentGroup);
        return agentGroup;
    }


    protected AgentGroup addAgents(String key, Class<?> agent, int count, Marker marker, Color color) throws Exception{
        AgentGroup agentGroup = new AgentGroup(key);
        agentGroup.setMarker(marker);
        agentGroup.setMarkerColor(color);
        agentGroup.setAgents(createAgents(count, agent));
        this.agents.put(key, agentGroup);
        return agentGroup;
    }

    protected AgentGroup addAgents(String key, Marker marker, Color color) throws Exception{
        AgentGroup agentGroup = new AgentGroup(key);
        agentGroup.setMarker(marker);
        agentGroup.setMarkerColor(color);
        agentGroup.setAgents(new ArrayList<>());
        this.agents.put(key, agentGroup);
        return agentGroup;
    }

    private List<AbsAgent> createAgents(int count, Class<?> agent) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<AbsAgent> list = new ArrayList<>();
        for(int i=0; i<count;i++){
            var inst = agent.getDeclaredConstructor().newInstance();
            ((Agent)inst).initPosition(margins);
            list.add((AbsAgent)inst);
        }
        return list;
    }

    protected void checkPaused() throws InterruptedException{
        if(this.isPaused.isSet()){
            while(this.isPaused.isSet()){ Thread.sleep(500); }
        }
    }

    public void pauseOptimizer() {
        this.isPaused.set();
    }

    public void resumeOptimizer() {
        this.isPaused.unset();
    }

    public void stopOptimizer() {
        this.isKilled.set();
        this.initialized.unset();
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public boolean isPaused() {
        return isPaused.isSet();
    }

    public Margins getMargins() {
        return margins;
    }

    public void setMargins(Margins margins) {
        this.margins = margins;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getNanoDuration() {
        return nanoDuration;
    }

    public void setStepCompleted(StepCompleted stepCompleted) {
        this.stepCompleted = stepCompleted;
    }

    public String getName(){ return this.getClass().getSimpleName(); }

    public AgentGroup getAgents(String key) {
        return agents.get(key);
    }

    public List<?> getAgents() {
        return Stream.of(agents.values()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Map<String, AgentGroup> getAgentsMap(){ return agents; };

    protected boolean isInitialized() {
        return this.initialized.isSet();
    }

    protected void setInitialized(boolean v) {
        this.initialized.setValue(v);
    }

}
