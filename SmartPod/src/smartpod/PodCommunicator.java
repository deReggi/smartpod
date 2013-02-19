package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Andreas
 */
public class PodCommunicator extends Communicator
{
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
	
	public void acceptPodToRoadTransfer(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msg.setOntology(ONTOLOGY_POD_NODE);
		msg.setContent("proposal accepted");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	public void informPodToRoadTransfer(RoadAgent road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD);
		msg.setContent(agent.getName());
		msg.addReceiver(road.getAID());
		agent.send(msg);
	}
	
	public void requestPodToNodeTransfer(NodeAgent node)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE);
		msg.setContent(agent.getName());
		msg.addReceiver(node.getAID());
		agent.send(msg);
	}
}
