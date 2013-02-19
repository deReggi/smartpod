package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 *
 * @author Andreas
 */
public class Communicator
{
	// unused
	public final String ONTOLOGY_NODE_NODE	= "co_nn";
	public final String ONTOLOGY_NODE_ENV	= "co_ne";
	public final String ONTOLOGY_ENV_NODE	= "co_en";
	public final String ONTOLOGY_ENV_ROAD	= "co_er";
	public final String ONTOLOGY_ROAD_ENV	= "co_re";
	
	public final String ONTOLOGY_POD_ENV	= "pod_environment";
	public final String ONTOLOGY_ROAD_POD	= "road_pod";
	public final String ONTOLOGY_ROAD_NODE	= "road_node";
	public final String ONTOLOGY_NODE_ROAD	= "node_road";
	
	// used
	public final String ONTOLOGY_NODE_POD	= "node_pod";
	public final String ONTOLOGY_POD_NODE	= "pod_node";
	public final String ONTOLOGY_POD_ROAD	= "pod_road";
	
	public MessageTemplate messageTemplate	= null;
	public SPAgent agent = null;
	
	public ArrayList<ACLMessage> checkMessageBox()
	{
		ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
		ACLMessage msg;
		do
		{
			msg = agent.receive(messageTemplate);
			if (msg != null)
			{
				messages.add(msg);
			}
		}
		while (msg != null);
		return messages;
	}
	
	public ACLMessage receiveMessage()
	{
		return agent.receive(messageTemplate);
	}	
}
