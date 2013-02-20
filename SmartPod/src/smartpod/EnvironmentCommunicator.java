package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 *
 * @author Andreas
 */
public class EnvironmentCommunicator extends Communicator
{
	/**
	 * The message templates for receiving messages.
	 */
	public MessageTemplate roadWeightUpdateTemplate	= 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_ROAD_WEIGHT_UPDATE));
	public MessageTemplate pathFindRequestTemplate =
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology(ONTOLOGY_PATH_FINDING));
	/**
	 * EnvironmentCommunicator constructor.
	 * @param environment the parent agent
	 */
	public EnvironmentCommunicator(EnvironmentAgent environment)
	{
		this.agent = environment;
	}
	
	/**
	 * Checks for road weight update messages.
	 * @return ArrayList of received weight update ACLMessages.
	 */
	public ArrayList<ACLMessage> checkRoadWeightUpdates()
	{
		return checkMessageBox(roadWeightUpdateTemplate);
	}
	
	/**
	 * Checks for received path finding requests.
	 * @return ArrayList of received path finding ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPathFindingRequests()
	{
		return checkMessageBox(pathFindRequestTemplate);
	}
	
	/**
	 * Sends the INFORM response of path finding result.
	 * @param requestMessage the request message.
	 * @param roadToTake the next RoadAgent.
	 */
	public void informPathFindingResult(ACLMessage requestMessage, RoadAgent roadToTake)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_PATH_FINDING);
		msg.setContent("optimal path found");
		msg.addReceiver(requestMessage.getSender());
		msg.addUserDefinedParameter("road_to_take", roadToTake.getLocalName());
		agent.send(msg);
	}
}
