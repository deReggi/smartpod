package smartpod;

import jade.core.AID;
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
	private MessageTemplate roadWeightUpdateTemplate	= 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_ROAD_WEIGHT_UPDATE));
	private MessageTemplate pathFindRequestTemplate =
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
	public ACLMessage checkPathFindingRequests()
	{
		return agent.receive(pathFindRequestTemplate);
	}
	
	/**
	 * Sends the INFORM response of path finding result.
	 * @param requestMessage the request message.
	 * @param roadToTake the next road agent id.
	 */
	public void informPathFindingResult(ACLMessage requestMessage, AID roadToTake)
	{
		System.out.printf("%-10s :: informPathFindingResult(%s)\n",agent.getLocalName(),roadToTake.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_PATH_FINDING);
		msg.setContent("optimal path found");
		msg.addReceiver(requestMessage.getSender());
		msg.addUserDefinedParameter("pod", requestMessage.getUserDefinedParameter("pod"));
		msg.addUserDefinedParameter("road_to_take", roadToTake.getLocalName());
		msg.addUserDefinedParameter("destination", requestMessage.getUserDefinedParameter("destination"));
		agent.send(msg);
	}
}
