package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 *
 * @author Andreas
 */
public class PathFindingCommunicator extends Communicator
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
	 * PathFindingCommunicator constructor.
	 * @param pathFindingAgent the parent agent
	 */
	public PathFindingCommunicator(PathFindingAgent pathFindingAgent)
	{
		this.agent = pathFindingAgent;
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
	 * @param roadToTake the next road name.
	 */
	public void informPathFindingResult(ACLMessage requestMessage, String roadToTake)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_PATH_FINDING);
		msg.setContent("optimal path found");
		msg.addReceiver(requestMessage.getSender());
		msg.addUserDefinedParameter("pod", requestMessage.getUserDefinedParameter("pod"));
		msg.addUserDefinedParameter("road_to_take", roadToTake);
		msg.addUserDefinedParameter("destination", requestMessage.getUserDefinedParameter("destination"));
		agent.send(msg);
	}
}
