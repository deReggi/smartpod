package smartpod;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * Class for creating NodeCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class NodeCommunicator extends Communicator
{	
	/**
	 * The message templates for receiving messages.
	 */
	private MessageTemplate podArrivalTemplate =
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_ARRIVAL));
	private MessageTemplate podDepartureTemplate = 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_DEPARTURE));
	private MessageTemplate pathFindResultTemplate = 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_PATH_FINDING));
	private MessageTemplate transportRequestTemplate = 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology(ONTOLOGY_PASSENGER_TRANSPORT));
	
	
	/**
	 * NodeCommunicator constructor.
	 * 
	 * @param node
	 *		The parent agent.
	 */
	public NodeCommunicator(NodeAgent node)
	{
		this.agent = node;
	}
	
	/**
	 * Checks for received pod departure messages.
	 * 
	 * @return
	 *		ArrayList of received pod departure ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodDepartureMessages()
	{
		return checkMessageBox(podDepartureTemplate);
	}
	
	/**
	 * Checks for received pod arrival messages.
	 * 
	 * @return
	 *		ArrayList of received pod arrival ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodArrivalRequestMessages()
	{
		return checkMessageBox(podArrivalTemplate);
	}
	
	/**
	 * Checks for received path finding result messages.
	 * 
	 * @return 
	 *		ArrayList of received path finding result ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPathFindingResultMessages()
	{
		return checkMessageBox(pathFindResultTemplate);
	}
	
	/**
	 * Receives the pod arrival request message.
	 * 
	 * @return
	 *		The arrived ACLMessage or null.
	 */
	public ACLMessage checkPodArrivalRequests()
	{
		return agent.receive(podArrivalTemplate);
	}
	
	/**
	 * Receives the passenger transport request message.
	 * 
	 * @return
	 *		The arrived ACLMessage or null.
	 */
	public ACLMessage checkPassengerTransportRequests()
	{
		return agent.receive(transportRequestTemplate);
	}
	
	/**
	 * Sends REQUEST message for the pod departure to the road.
	 * 
	 * @param podAID
	 *		The pod involved in the transfer.
	 * @param roadAID
	 *		The road the pod should transfer to.
	 * @param destinationAID
	 *		The final destination.
	 */
	public void requestPodToRoadDeparture(AID podAID, AID roadAID, AID destinationAID)
	{
		System.out.printf("%-10s :: requestPodToRoadDeparture(%s,%s,%s)\n",agent.getLocalName(),podAID.getLocalName(),roadAID.getLocalName(),destinationAID.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE_DEPARTURE);
		msg.setContent("pod to road transfer request");
		msg.addReceiver(podAID);
		msg.addUserDefinedParameter("road", roadAID.getLocalName());
		msg.addUserDefinedParameter("destination", destinationAID.getLocalName());
		agent.send(msg);
	}
	
	/**
	 * Sends the CONFIRM message for accepting pod arrival to node request.
	 * 
	 * @param requestMessage
	 */
	public void confirmPodToNodeArrival(ACLMessage requestMessage)
	{
		System.out.printf("%-10s :: confirmPodToNodeArrival()\n",agent.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setOntology(ONTOLOGY_POD_NODE_ARRIVAL);
		msg.setContent("pod to node transfer confirm");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	/**
	 * Sends the REQUEST message to the path finding agent.
	 * 
	 * @param conversationId
	 *		The conversation id.
	 * @param destinationAID
	 *		The destination node agent id.
	 */
	public void requestPathFinding(String conversationId, AID destinationAID)
	{
		System.out.printf("%-10s :: requestPathFinding(\"%s\",%s)\n",agent.getLocalName(),conversationId,destinationAID.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setConversationId(conversationId);
		msg.setOntology(ONTOLOGY_PATH_FINDING);
		msg.addReceiver(((NodeAgent)agent).pathFindingAgent);
		msg.addUserDefinedParameter("destination", destinationAID.getLocalName());
		agent.send(msg);
	}
}
