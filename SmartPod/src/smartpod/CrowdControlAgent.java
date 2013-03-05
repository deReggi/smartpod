/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.wrapper.AgentController;
import java.util.Date;

/**
 *
 * @author Janez Feldin
 */
public class CrowdControlAgent extends SPAgent
{
	public Date lastGeneration;
	public Date nextGeneration;
	public long numPodsGenerated = 0;
	

	@Override
	protected void setup()
	{
		lastGeneration = getCurrentDate();
		nextGeneration = new Date();
		nextGeneration = getNextGenerationTime(lastGeneration);
		
		
		addBehaviour(new CrowdControlBehaviour(this));
	}
	
	private class CrowdControlBehaviour extends CyclicBehaviour
	{
		public CrowdControlBehaviour(Agent a)
		{
			super(a);
		}

		@Override
		public void action()
		{
			Date currentDate = getCurrentDate();
			
			if ( nextGeneration.getTime() < currentDate.getTime())
			{
				lastGeneration = currentDate;
				nextGeneration = getNextGenerationTime(lastGeneration);
				
//				generateCrowdForOneDay();
			}
		}
		
	}
	
	public Date getNextGenerationTime(Date date)
	{
		Date result = new Date();
		result.setTime(((date.getTime()/86400000)+1)*86400000);
		
		System.out.println(date + "      "+result);
		return result;
	}
	
	public void generateCrowdForOneDay()
	{
		for(int i = 0;i<5;i++)
		{
			generatePeopleGroupAgent("Postaja1", "Postaja2", i*(int)(Math.random()*1000+100));
		}
	}
	
	public void generatePeopleGroupAgent(String startingStation,String endingStation,int delay)
	{
		AID garageAID = new AID(startingStation, false);
		AID destinationAID = new AID(endingStation, false);
		PassengerGroupAgent testAgent2 = new PassengerGroupAgent(garageAID, destinationAID, delay);
		try
		{
		((AgentController) getContainerController().acceptNewAgent("PeopleGroup"+(numPodsGenerated++), testAgent2)).start();
		}
		catch(Exception e)
		{
			System.err.println("CrowdControlAgent.java in generatePeopleGroupAgent. Error message: "+e.toString());
		}
	}
	
	
}
