package smartpod;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Class for creating PodCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class RoadCommunicator extends Communicator
{
		
	/**
	 * RoadCommunicator constructor initializes the messageTemplate.
	 * @param road the parent agent
	 */
	public RoadCommunicator(RoadAgent road)
	{
		this.agent = road;
		this.messageTemplate = 
				MessageTemplate.and(
					MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
					MessageTemplate.or(
						MessageTemplate.MatchOntology(ONTOLOGY_NODE_ROAD), 
						MessageTemplate.MatchOntology(ONTOLOGY_POD_ROAD)));
	}
	
	/**
	 * Sends the INFORM response message to road weight request.
	 * @param requestMessage
	 */
	public void informRoadWeightRequest(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_ROAD_ENV);
		msg.setContent(String.valueOf(((RoadAgent)agent).weight));
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message of road weight to all nodes.
	 */
	public void iformRoadWeight()
	{
		for (NodeAgent nodeAgent : agent.nodeList)
		{
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setOntology(ONTOLOGY_ROAD_NODE);
			msg.setContent(String.valueOf(((RoadAgent)agent).weight));
			msg.addReceiver(nodeAgent.getAID());
			agent.send(msg);
		}
	}
}
