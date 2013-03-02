package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;
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
	 * @param position Vector2D that contains the desired position.
	 */
    public JunctionNodeAgent(Vector2D position, int podsCapacity)
    {
		super(position,podsCapacity);
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
//				System.out.println("com-node : "+msg.getContent());
				
				departingPods.remove(msg.getSender());
			}
			
			// check arrival message box
			ArrayList<ACLMessage> arrivalMessages = communicator.checkPodArrivalRequestMessages();
			for (ACLMessage msg : arrivalMessages)
			{
//				System.out.println("com-node : "+msg.getContent());
				
				AID podAID = msg.getSender();

				if (!registeredPods.contains(podAID))
				{
					registeredPods.add(podAID);

					AID destinationAID = new AID(msg.getUserDefinedParameter("destination"),false);

					communicator.confirmPodToNodeArrival(msg);
					communicator.requestPathFinding(podAID, destinationAID);
				}
				else
				{
					System.err.println("com-node : pod already registered");
				}
			}
			
			// check path finding result message box
			ArrayList<ACLMessage> pathFinding = communicator.checkPathFindingResultMessages();
			for (ACLMessage msg : pathFinding)
			{
//				System.out.println("com-node : "+msg.getContent());
				
				AID podAID = new AID(msg.getUserDefinedParameter("pod"),false);
				
				departingPods.add(podAID);
				registeredPods.remove(podAID);
				
				AID roadAID = new AID(msg.getUserDefinedParameter("road_to_take"),false);
				AID destinationAID = new AID(msg.getUserDefinedParameter("destination"),false);
				communicator.requestPodToRoadDeparture(podAID, roadAID, destinationAID);
			}
        }
    } 
}
