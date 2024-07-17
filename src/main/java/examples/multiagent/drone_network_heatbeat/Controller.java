package examples.multiagent.drone_network_heatbeat;

import org.usa.soc.multiagent.AgentGroup;

import java.util.HashMap;
import java.util.List;

public class Controller {
    public static HashMap<Integer, DroneAgent> dronesMap = new HashMap<>();
    public static final double OmegaLeader = 10.0;
    public static final double alpha = 0.7;

    public static int K = 15;

    public static final int DISCONNECTED_KEY = 1;

    public static AgentGroup agentGroup = new AgentGroup("Drones");
    public static AgentGroup cAgentGroup = new AgentGroup("Crashed Agents");
    public static AgentGroup dAgentGroup = new AgentGroup("Disconnected Agents");

    public static int incrementor = 1;

    public static HashMap<Integer, List<StateMap>> stateSamplesMap = new HashMap<>();

    public static HashMap<Integer, List<DoubleStateMap>> pKtlMap = new HashMap<>();
}
