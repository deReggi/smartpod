package smartpod;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Class for creating PodCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class PodCommunicator extends Communicator
{
	/**
	 * PodCommunicator constructor initializes the messageTemplate.
	 * @param pod the parent agent
	 */
	public PodCommunicator(PodAgent pod)
	{
		this.agent = pod;
		this.messageTemplate = 
				MessageTemplate.and(
					MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
						MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)),
					MessageTemplate.or(
						MessageTemplate.MatchOntology(ONTOLOGY_NODE_POD), 
						MessageTemplate.MatchOntology(ONTOLOGY_ROAD_POD)));
	}
	
	/**
	 * Sends the ACCEPT_PROPOSAL response to pod to road transfer proposal.
	 * @param requestMessage the request message from (source) node.
	 */
	public void acceptPodToRoadTransfer(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msg.setOntology(ONTOLOGY_POD_NODE);
		msg.setContent("accept proposal");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message that the pod was transfered to the road.
	 * @param road the road that pod was transferred to.
	 */
	public void informPodToRoadTransfer(AID road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD);
		msg.setContent("add");
		msg.addReceiver(road);
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message that the pod was transfered to the node.
	 * @param road the road that pod was transferred to.
	 */
	public void informPodToNodeTransfer(AID road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD);
		msg.setContent("remove");
		msg.addReceiver(road);
		agent.send(msg);
	}
	
	/**
	 * Sends the REQUEST message for pod transfer to the (destination) node.
	 * @param node the (destination) node the pod arrives at.
	 */
	public void requestPodToNodeTransfer(AID node)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE);
		msg.setContent("pod to node transfer request");
		msg.addReceiver(node);
		agent.send(msg);
	}
}
