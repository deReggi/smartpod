package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Class for creating NodeCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class NodeCommunicator extends Communicator
{	
	/**
	 * NodeCommunicator constructor initializes the messageTemplate.
	 * @param node the parent agent.
	 */
	public NodeCommunicator(NodeAgent node)
	{
		this.agent = node;
		this.messageTemplate = 
				MessageTemplate.and(
					MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
					MessageTemplate.or(
						MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE), 
						MessageTemplate.MatchOntology(ONTOLOGY_ROAD_NODE)));
	}
	
	/**
	 * Sends PROPOSE message for the pod transfer to the road.
	 * @param pod	the pod involved in the transfer
	 * @param road	the road the pod should transfer to
	 */
	public void proposePodToRoadTransfer(PodAgent pod, RoadAgent road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.setOntology(ONTOLOGY_NODE_POD);
		msg.setContent("go to");
		msg.addReceiver(pod.getAID());
		msg.addUserDefinedParameter("road", road.getName());
		agent.send(msg);
	}
	
	/**
	 * Sends the CONFIRM message for accepting pod transfer request (destination).
	 * @param requestMessage
	 */
	public void confirmPodToNodeTransfer(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setOntology(ONTOLOGY_NODE_POD);
		msg.setContent("confirm");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
}
