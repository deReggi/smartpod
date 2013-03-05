package de.reggi.jade;

import jade.lang.acl.ACLMessage;

/**
 *
 * @author Andreas
 */
public class MessageHelper
{
	static int cidCnt = 0;
	static String cidBase;

	public static String genCID()
	{
		if (cidBase == null)
		{
			cidBase = "CID_"+System.currentTimeMillis() % 10000 + "_";
		}
		return cidBase + (cidCnt++);
	}

	public static ACLMessage newMessageWithCID(int perf)
	{
		ACLMessage msg = new ACLMessage(perf);
		msg.setConversationId(genCID());
		return msg;
	}
}
