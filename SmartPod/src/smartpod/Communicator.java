package smartpod;

import jade.lang.acl.ACLMessage;

/**
 *
 * @author Andreas
 */
public class Communicator
{
	
	public ACLMessage createMessageQueryIf(String ontology, String content)
    {
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
		msg.setOntology(ontology);
		msg.setContent(content);
		return msg;
    }
}
