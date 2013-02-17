/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Class for creating junction agent. It extends NodeAgent.
 * @author Janez Feldin
 */
public class JunctionNodeAgent extends NodeAgent
{
	/**
	 * Constructor for junction agent.
	 * @param position Point that contains the desired position.
	 */
    public JunctionNodeAgent(Point position)
    {
		super(position);
    }
    
	/**
	 * This method gets called when agent is started.
	 * It adds the desired behaviour to the agent.
	 */
    @Override
    protected void setup()
    {
        addBehaviour(new JunctionAgentBehaviout(this));
    }
    
    
    /**
	 * Behaviour class for JunctionAgent.
	 * It extends CyclicBehaviour.
	 */
    public class JunctionAgentBehaviout extends CyclicBehaviour
    {
		/**
		 * Constructor for junction's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 */
        public JunctionAgentBehaviout(Agent a)
        {
            super(a);
        }

		/**
		 * Method that performs actions in JunctionAgentBehaviour class.
		 * It get's called each time Jade platform has spare resources.
		 */
        @Override
        public void action()
        {
        }
    }
    
}
