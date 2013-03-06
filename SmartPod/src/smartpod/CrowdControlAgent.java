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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Janez Feldin
 */
public class CrowdControlAgent extends SPAgent
{

	public Date lastGeneration;
	public Date nextGeneration;
	public long numPodsGenerated = 0;
	private Map<String, String> stationMap = new HashMap<String, String>(6);

	@Override
	protected void setup()
	{
		lastGeneration = new Date(getCurrentDate().getTime());
		nextGeneration = new Date();
		nextGeneration = getNextGenerationTime(lastGeneration);
//		System.out.println(nextGeneration);


		stationMap.put("bufet", "Postaja1");
		stationMap.put("postaja", "Postaja2");
		stationMap.put("knjižnica", "Postaja3");
		stationMap.put("predavalnica", "Postaja4");
		stationMap.put("menza", "Postaja5");
		stationMap.put("dormi", "Postaja6");

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

			if (nextGeneration.getTime() < currentDate.getTime())
			{
				lastGeneration = currentDate;
				nextGeneration = getNextGenerationTime(lastGeneration);

				generateCrowdForOneDay();
			}
		}
	}

	public Date getNextGenerationTime(Date date)
	{
		Date result = new Date();
		result.setTime(((date.getTime() / 86400000) + 1) * 86400000);

		return result;
	}

	public void generateCrowdForOneDay()
	{
		int oneHour = 3600;
		int requests;

		//bufet -> dormi
		int bufetRand = 5;
		int bufetOff = 2;
		requests = (int) (Math.random() * bufetRand) + bufetOff;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("bufet"), stationMap.get("dormi"), (int) (1.5 * oneHour), (int) (oneHour));
		}

		//dormi -> predavalnice
		int dormiRand = 15;
		int dormiOff = 10;
		requests = (int) (Math.random() * dormiRand) + dormiOff;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("dormi"), stationMap.get("predavalnica"), (int) (6 * oneHour), (int) (2 * oneHour));
		}

		//predavalnice -> menza
		int predavalnice1Rand = 15;
		int predavalnice1Off = 15;
		requests = (int) (Math.random() * predavalnice1Rand) + predavalnice1Off;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("predavalnica"), stationMap.get("menza"), (int) (11 * oneHour), (int) (2 * oneHour));
		}

		//menza -> predavalnice
		int menza1Rand = 10;
		int menza1Off = 6;
		requests = (int) (Math.random() * menza1Rand) + menza1Off;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("menza"), stationMap.get("predavalnica"), (int) (12 * oneHour), (int) (2 * oneHour));
		}

		//menza -> dormi
		int menza2Rand = 10;
		int menza2Off = 4;
		requests = (int) (Math.random() * menza2Rand) + menza2Off;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("menza"), stationMap.get("dormi"), (int) (12 * oneHour), (int) (2 * oneHour));
		}

		//menza -> postaja
		int menza3Rand = 3;
		int menza3Off = 2;
		requests = (int) (Math.random() * menza3Rand) + menza3Off;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("menza"), stationMap.get("postaja"), (int) (12 * oneHour), (int) (2 * oneHour));
		}

		//predavalnice -> dormi
		int predavalnica3Rand = 3;
		int predavalnica3Off = 2;
		requests = (int) (Math.random() * predavalnica3Rand) + predavalnica3Off;
		for (int i = 0; i < requests; i++)
		{
			generatePeopleGroupAgent(stationMap.get("predavalnica"), stationMap.get("dormi"), (int) (16 * oneHour), (int) (6 * oneHour));
		}


	}

	public void generatePeopleGroupAgent(String startingStation, String endingStation, int delay, int timeFrame)
	{


		AID garageAID = new AID(startingStation, false);
		AID destinationAID = new AID(endingStation, false);

		System.out.println(delay - timeFrame / 2 + (int) (timeFrame * Math.random()));
		PassengerGroupAgent group = new PassengerGroupAgent(garageAID, destinationAID, delay - timeFrame / 2 + (int) (timeFrame * Math.random()));
		try
		{
			((AgentController) getContainerController().acceptNewAgent("PeopleGroup" + (numPodsGenerated++), group)).start();
		}
		catch (Exception e)
		{
			System.err.println("CrowdControlAgent.java in generatePeopleGroupAgent. Error message: " + e.toString());
		}
	}
}
