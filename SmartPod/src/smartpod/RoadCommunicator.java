package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * Class for creating RoadCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class RoadCommunicator extends Communicator
{
	/**
	 * The message templates for receiving messages.
	 */
	public MessageTemplate podAttachedTemplate	= MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_ROAD_ATTACH));
	public MessageTemplate podDetachedTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_ROAD_DETACH));
		
	/**
	 * RoadCommunicator constructor initializes the messageTemplate.
	 * @param road the parent agent
	 */
	public RoadCommunicator(RoadAgent road)
	{
		this.agent = road;
	}
	
	/**
	 * Checks for received pod attach messages.
	 * @return ArrayList of received pod attach ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodAttachMessages()
	{
		return checkMessageBox(podAttachedTemplate);
	}
	
	/**
	 * Checks for received pod detach messages.
	 * @return ArrayList of received pod detach ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodDetachMessages()
	{
		return checkMessageBox(podDetachedTemplate);
	}
	
	/**
	 * Sends the INFORM response message to road weight request.
	 * @param requestMessage
	 */
	public void informRoadWeightRequest(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_ROAD_ENV);
		msg.setContent("my weight is");
		msg.addReceiver(requestMessage.getSender());
		msg.addUserDefinedParameter("weight", String.valueOf(((RoadAgent)agent).weight));
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
			msg.setContent("my weight is");
			msg.addReceiver(nodeAgent.getAID());
			msg.addUserDefinedParameter("weight", String.valueOf(((RoadAgent)agent).weight));
			agent.send(msg);
		}
	}
}
