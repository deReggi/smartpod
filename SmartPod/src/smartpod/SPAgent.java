/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;

/**
 * Class for creating SPAgent. It extends jade.core.Agent class.
 * This is the base class for all the other agent classes.
 *
 * @author Janez Feldin
 */
public class SPAgent extends Agent
{
    
	/**
	 * This method is used to get all the Agents from the jade's container containing this agent.
	 * @return AMSAgentDescription array that contains all AMSDescriptions of all the agents.
	 */
    public AMSAgentDescription[] getAllAgents()
    {
        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults((long) -1);
        AMSAgentDescription[] allAgents;

        try
        {
            allAgents = AMSService.search(this, new AMSAgentDescription(), sc);
            return allAgents;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }

	/**
	 * This method is used to get all the Agents from jade's container, containing this agent, that has desired phrase in their name.
	 * @param name String of the search phrase.
	 * @return AMSAgentDescription array that contains all AMSDescriptions of all agents that have specified phrase in their name.
	 */
    public AMSAgentDescription[] getAllAgentsContaining(String name)
    {
        AMSAgentDescription[] allAgents = getAllAgents();
        int count = 0;

        for (int i = 0; i < allAgents.length; i++)
        {
            if (allAgents[i].getName().getLocalName().indexOf(name) != -1)
            {
                count++;
            }
        }

        if (count == 0)
        {
            return null;
        }

        AMSAgentDescription[] result = new AMSAgentDescription[count];
        int j = 0;
        for (int i = 0; i < allAgents.length; i++)
        {
            if (allAgents[i].getName().getLocalName().indexOf(name) != -1)
            {
                result[j++] = allAgents[i];
            }
        }
        return result;
    }

	/**
	 * This method is used to get all with a specified local name from jade's container containing this agent.
	 * @param name name of the Agent we wish to find as a String.
	 * @return AMSAgentDescription of the agent with specified name.
	 * if agent with that name is not found null is returned.
	 */
    public AMSAgentDescription getAgentByName(String name)
    {
        AMSAgentDescription[] allAgents = getAllAgents();

        for (int i = 0; i < allAgents.length; i++)
        {
            if (allAgents[i].getName().getLocalName().equals(name))
            {
                return allAgents[i];
            }
        }
        return null;
    }
}
