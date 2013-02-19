package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Andreas
 */
public class NodeCommunicator extends Communicator
{	
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
	
	public void proposePodToRoadTransfer(PodAgent pod, RoadAgent road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.setOntology(ONTOLOGY_NODE_POD);
		msg.setContent(road.getName());
		msg.addReceiver(pod.getAID());
		agent.send(msg);
	}
	
	public void confirmPodToNodeTransfer(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setOntology(ONTOLOGY_NODE_POD);
		msg.setContent("proposal accepted");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
}
