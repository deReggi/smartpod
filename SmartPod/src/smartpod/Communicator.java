package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * The base Communicator class.
 * 
 * @author Andreas
 */
public class Communicator
{
	/**
	 * The message ontology.
	 */
	protected final String ONTOLOGY_NODE_NODE_ARRIVAL	= "node_node_arrival";
	protected final String ONTOLOGY_POD_NODE_ARRIVAL	= "pod_node_arrival";
	protected final String ONTOLOGY_POD_NODE_DEPARTURE	= "pod_node_departure";
	protected final String ONTOLOGY_POD_ROAD_ATTACH		= "pod_road_attach";
	protected final String ONTOLOGY_POD_ROAD_DETACH		= "pod_road_detach";
	protected final String ONTOLOGY_ROAD_WEIGHT_UPDATE	= "road_weight_update";
	protected final String ONTOLOGY_PATH_FINDING		= "path_finding";
	protected final String ONTOLOGY_PASSENGER_TRANSPORT	= "passenger_transport";
		
	/**
	 * The parent agent.
	 */
	public SPAgent agent = null;
	
	/**
	 * Checks for received messages with given template.
	 * 
	 * @param messageTemplate
	 *		The MessageTemplate to use. If null all messages will be supplied.
	 * 
	 * @return 
	 *		ArrayList of received ACLMessages.
	 */
	public ArrayList<ACLMessage> checkMessageBox(MessageTemplate messageTemplate)
	{
		ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
		ACLMessage msg = receiveMessage(messageTemplate);
		while (msg != null)
		{
			messages.add(msg);
			msg = receiveMessage(messageTemplate);
		}
		return messages;
	}
	
	/**
	 * Receives an ACL message from the agent message queue.
	 * 
	 * @param messageTemplate
	 *		The MessageTemplate to use.
	 * 
	 * @return
	 *		A new ACL message, or null if no message is present.
	 */
	public ACLMessage receiveMessage(MessageTemplate messageTemplate)
	{
		if (messageTemplate != null)
		{
			return agent.receive(messageTemplate);
		}
		return agent.receive();
	}	
}
