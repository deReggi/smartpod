package smartpod;

import jade.core.Agent;

/**
 * Class for creating SPAgent. It extends jade.core.Agent class.
 * This is the base class for all the other agent classes.
 *
 * @author Janez Feldin
 */
public class SPAgent extends Agent
{
	/**
	 * Calculates current time in seconds within a day.
	 * 
	 * @return
	 *		Long value representing seconds.
	 */
	public long getCurrentTime()
	{
		long time = System.currentTimeMillis();
		// for simulation milliseconds represent seconds.
		return time%86400;
	}
}
