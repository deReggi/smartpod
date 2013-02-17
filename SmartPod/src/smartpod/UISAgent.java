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
 *
 * @author Janez Feldin
 */
public class UISAgent extends Agent
{
	
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
