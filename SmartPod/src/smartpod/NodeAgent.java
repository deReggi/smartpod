/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Class for creating node agent.
 * It extends SPAgent.
 * @author Janez Feldin
 */
public class NodeAgent extends SPAgent
{
	//declaration of variables
    private Point position;
    
	/**
	 * This method returns the position of this node.
	 * @return Point that represents the position of the node location.
	 */
    public Point getPosition()
    {
        return position;
    }
	
	/**
	 * Method used for setting the node's position.
	 * @param position Point that contains the desired position.
	 */
    public void setPosition(Point position)
    {
        this.position = position;
    }
    
	/**
	 * Constructor for node agent.
	 * @param position Point that contains the desired position.
	 */
    public NodeAgent(Point position)
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
		 * It get's called each time Jade platform has spare resources.
		 */
        @Override
        public void action()
        {
        }
    }
    
}
