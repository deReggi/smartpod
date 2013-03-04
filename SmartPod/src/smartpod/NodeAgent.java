package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import java.util.ArrayList;

/**
 * Class for creating node agent.
 * It extends SPAgent.
 * @author Janez Feldin
 */
public class NodeAgent extends SPAgent
{
	/***************************************************************************
	 * Variables
	 **************************************************************************/

	/**
	 * The currently registered pods at the node.
	 */
	public ArrayList<AID> registeredPods = new ArrayList<AID>();
	/**
	 * The currently registered pods in the process of departing the node.
	 */
	public ArrayList<AID> departingPods = new ArrayList<AID>();
	/**
	 * The AID of the path finding agent.
	 */
	protected AID pathFindingAgent;
	/**
	 * The position of the node.
	 */
    protected Vector2D position;
	/**
	 * Maximum allowed pods on the station at a given moment.
	 */
    protected int podsCapacity;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/
    
	/**
	 * Constructor for node agent.
	 * 
	 * @param position 
	 *		Vector2D that contains the desired position.
	 * @param podsCapacity
	 *		Maximum allowed pods on the station at a given moment.
	 */
    public NodeAgent(Vector2D position,int podsCapacity)
    {
        this.position = position;
		this.podsCapacity = podsCapacity;
    }
	
	/***************************************************************************
	 * JADE setup and behaviors
	 **************************************************************************/

	/**
	 * This method gets called when agent is started.
	 * It adds the desired behaviour to the agent.
	 */
    @Override
    protected void setup()
    {
        //adds the behviour to the agent
        addBehaviour(new NodeAgentBehaviour(this));
    }
    
    /**
	 * Behaviour class for NodeAgent.
	 * It extends CyclicBehaviour.
	 */
    public class NodeAgentBehaviour extends CyclicBehaviour
    {
		/**
		 * Constructor for Node's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 */
        public NodeAgentBehaviour(Agent a)
        {
            super(a);
        }

		/**
		 * Method that performs actions in NodeAgentBehaviour class.
		 * It gets called each time Jade platform has spare resources.
		 */
        @Override
        public void action()
        {
        }
    }
    
	/***************************************************************************
	 * Getters & setters
	 **************************************************************************/
	
	/**
	 * This method returns the position of this node.
	 * 
	 * @return
	 *		Vector2D that represents the position of the node location.
	 */
    public Vector2D getPosition()
    {
        return position;
    }
	
	/**
	 * Method used for setting the node's position.
	 * 
	 * @param position 
	 *		Vector2D that contains the desired position.
	 */
    public void setPosition(Vector2D position)
    {
        this.position = position;
    }
	
	/**
	 * Method that returns the integer value representing the maximum number 
	 * of pods, that can be on this station at a given moment.
	 * 
	 * @return
	 *		Integer value representing stations pod capacity
	 */
    public int getPodsCapacity()
    {
        return podsCapacity;
    }

	/**
	 * Method used to set the maximum number of pods, that can be on this 
	 * station at a given moment.
	 * 
	 * @param podsCapacity
	 *		Integer value representing the maximum number of pods.
	 */
    public void setPodsCapacity(int podsCapacity)
    {
        this.podsCapacity = podsCapacity;
    }
}
