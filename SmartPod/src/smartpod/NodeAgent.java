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
	//declaration of variables
	public ArrayList<AID> registeredPods = new ArrayList<AID>();
	protected AID pathFindingAgent;
    protected Vector2D position;
    
	/**
	 * This method returns the position of this node.
	 * @return Vector2D that represents the position of the node location.
	 */
    public Vector2D getPosition()
    {
        return position;
    }
	
	/**
	 * Method used for setting the node's position.
	 * @param position Vector2D that contains the desired position.
	 */
    public void setPosition(Vector2D position)
    {
        this.position = position;
    }
    
	/**
	 * Constructor for node agent.
	 * @param position Vector2D that contains the desired position.
	 */
    public NodeAgent(Vector2D position)
    {
        this.position = position;
    }
    
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
    
}
