package org.usa.soc.multiagent;

import org.knowm.xchart.style.markers.Marker;
import org.usa.soc.core.Flag;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.exceptions.KillOptimizerException;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

public abstract class Algorithm{

    private Flag isPaused = new Flag(), isKilled=new Flag(), initialized=new Flag();

    protected Map<String, AgentGroup> agents = new HashMap<>();

    private int interval;
    private long currentStep;
    private Margins margins;

    private long nanoDuration;

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

    public abstract void run();

    public void  runInitializer(){
        initialize();
        this.initialized.set();
    }
    public void runOptimizer(long maxSteps) throws Exception{

        if(!this.initialized.isSet()){
            throw new RuntimeException("Initialize Failed");
        }

        if(getAgents().isEmpty()){
            throw new RuntimeException("No Agents Registered");
        }

        this.nanoDuration = System.nanoTime();
        for(long step = 0; step< maxSteps; step++){
            run();

            for(String key: this.getAgents().keySet()){
                for(Agent agent: this.getAgents().get(key).getAgents()){
                    agent.step();
                }
            }

            if(this.stepCompleted != null)
                this.stepCompleted.performAction(step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    public void runOptimizer() throws Exception{

        if(!this.initialized.isSet()){
            throw new RuntimeException("Initialize Failed");
        }

        if(getAgents().isEmpty()){
            throw new RuntimeException("No Agents Registered");
        }

        long step = 0;
        for(;;){
            run();

            for(String key: this.getAgents().keySet()){
                for(Agent agent: this.getAgents().get(key).getAgents()){
                    agent.step();
                }
            }


            if(this.stepCompleted != null)
                this.stepCompleted.performAction(step);
            stepCompleted(step);
            step++;
        }

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
        this.getAgents().put(key, agentGroup);
        return agentGroup;
    }


    protected AgentGroup addAgents(String key, Class<?> agent, int count, Marker marker, Color color) throws Exception{
        AgentGroup agentGroup = new AgentGroup(key);
        agentGroup.setMarker(marker);
        agentGroup.setMarkerColor(color);
        agentGroup.setAgents(createAgents(count, agent));
        this.getAgents().put(key, agentGroup);
        return agentGroup;
    }

    private List<Agent> createAgents(int count, Class<?> agent) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Agent> list = new ArrayList<>();
        for(int i=0; i<count;i++){
            Agent inst = (Agent) agent.getDeclaredConstructor().newInstance();
            inst.initPosition(margins);
            list.add(inst);
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

    public Map<String, AgentGroup> getSeriesData() { return getAgents(); }

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
}
