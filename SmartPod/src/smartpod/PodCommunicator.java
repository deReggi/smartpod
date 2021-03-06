package smartpod;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * Class for creating PodCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class PodCommunicator extends Communicator
{
	/**
	 * The message templates for receiving messages.
	 */
	private MessageTemplate podArrivalTemplate	= MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_ARRIVAL));
	private MessageTemplate podDepartureTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_DEPARTURE));
	private MessageTemplate podRoadAttachTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_ROAD_ATTACH));
	
	/**
	 * PodCommunicator constructor.
	 * @param pod the parent agent
	 */
	public PodCommunicator(PodAgent pod)
	{
		this.agent = pod;
	}
	
	/**
	 * Checks for received pod departure messages.
	 * @return ArrayList of received pod departure ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodDepartureMessages()
	{
		return checkMessageBox(podDepartureTemplate);
	}
	
	/**
	 * Checks for received pod arrival messages.
	 * @return ArrayList of received pod arrival ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodArrivalRequestMessages()
	{
		return checkMessageBox(podArrivalTemplate);
	}
	
	/**
	 * Checks for received road attach messages.
	 * @return ArrayList of received road attach ACLMessages.
	 */
	public ArrayList<ACLMessage> checkRoadAttachMessages()
	{
		return checkMessageBox(podRoadAttachTemplate);
	}
	
	/**
	 * Sends the INFORM response to pod departure to road request.
	 * @param requestMessage the request message from (source) node.
	 */
	public void acceptPodToRoadDeparture(ACLMessage requestMessage)
	{
		System.out.printf("%-10s :: acceptPodToRoadDeparture()\n",agent.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_NODE_DEPARTURE);
		msg.setContent("accept proposal");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message that the pod has been transfered to the road.
	 * @param road the road that pod was transferred to.
	 */
	public void informPodToRoadTransfer(AID road)
	{
		System.out.printf("%-10s :: informPodToRoadTransfer(%s)\n",agent.getLocalName(),road.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD_ATTACH);
		msg.setContent("pod attached");
		msg.addReceiver(road);
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message that the pod has been transfered to the node.
	 * @param road the road that pod was transferred from.
	 */
	public void informPodToNodeTransfer(AID road)
	{
		System.out.printf("%-10s :: informPodToNodeTransfer(%s)\n",agent.getLocalName(),road.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD_DETACH);
		msg.setContent("pod detached");
		msg.addReceiver(road);
		agent.send(msg);
	}
	
	/**
	 * Sends the REQUEST message for pod arrival to the node.
	 * @param node the node the pod arrives at.
	 */
	public void requestPodToNodeArrival(AID node)
	{
		System.out.printf("%-10s :: requestTransport(%s)\n",agent.getLocalName(),node.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE_ARRIVAL);
		msg.setContent("pod to node transfer request");		
		msg.addReceiver(node);
		msg.addUserDefinedParameter("destination", ((PodAgent)agent).getFinalDestinationNodeName());
		agent.send(msg);
	}
}
