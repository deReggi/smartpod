package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 * Class for creating junction agent. It extends NodeAgent.
 * @author Janez Feldin
 */
public class JunctionNodeAgent extends NodeAgent
{
	// agent communicator
	private NodeCommunicator communicator = new NodeCommunicator(this);
	
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
        addBehaviour(new JunctionAgentBehaviour(this));
    }
    
    
    /**
	 * Behaviour class for JunctionAgent.
	 * It extends CyclicBehaviour.
	 */
    public class JunctionAgentBehaviour extends CyclicBehaviour
    {
		/**
		 * Constructor for junction's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 */
        public JunctionAgentBehaviour(Agent a)
        {
            super(a);
        }

		/**
		 * Method that performs actions in JunctionAgentBehaviour class.
		 * It gets called each time Jade platform has spare resources.
		 */
        @Override
        public void action()
        {
			// check departure message box
			ArrayList<ACLMessage> departureMessages = communicator.checkPodDepartureMessages();
			for (ACLMessage msg : departureMessages)
			{
				System.out.println("com-node : "+msg.getContent());
			}
			
			// check arrival message box
			ArrayList<ACLMessage> arrivalMessages = communicator.checkPodArrivalRequestMessages();
			for (ACLMessage msg : arrivalMessages)
			{
				System.out.println("com-node : "+msg.getContent());
				
				communicator.confirmPodToNodeArrival(msg);
			}
			
			// check other messages
			ArrayList<ACLMessage> messages = communicator.checkMessageBox(null);
			for (ACLMessage msg : messages)
			{
				System.out.println("com-node : "+msg.getContent());
			}
        }
    }
    
}
