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
	public MessageTemplate podDetachedTemplate	= MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_ROAD_DETACH));
		
	/**
	 * RoadCommunicator constructor.
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
	 * Sends the INFORM message of road weight to the delegate.
	 */
	public void informRoadWeight()
	{
		System.out.println("RoadCommunicator - informRoadWeight()");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_ROAD_WEIGHT_UPDATE);
		msg.setContent("my weight is");
		msg.addReceiver(((RoadAgent)agent).weightUpdateDelegate);
		msg.addUserDefinedParameter("weight", String.valueOf(((RoadAgent)agent).weight));
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM response message containing the road data.
	 */
	public void informRoadData(ACLMessage requestMessage)
	{
		System.out.println("RoadCommunicator - informRoadData()");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD_ATTACH);
		msg.setContent("my road data");
		msg.addReceiver(requestMessage.getSender());
		msg.addUserDefinedParameter("end_node", ((RoadAgent)agent).endNode);
		msg.addUserDefinedParameter("start_position", ((RoadAgent)agent).startPosition.stringRepresentation());
		msg.addUserDefinedParameter("end_position", ((RoadAgent)agent).endPosition.stringRepresentation());
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message of road weight to all nodes.
	 */
//	public void informRoadWeight()
//	{
//		for (AID updateDelegate : ((RoadAgent)agent).weightUpdateDelegates)
//		{
//			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//			msg.setOntology(ONTOLOGY_ROAD_NODE);
//			msg.setContent("my weight is");
//			msg.addReceiver(updateDelegate);
//			msg.addUserDefinedParameter("weight", String.valueOf(((RoadAgent)agent).weight));
//			agent.send(msg);
//		}
//	}
}
