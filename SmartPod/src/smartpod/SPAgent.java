package smartpod;

import jade.core.Agent;
import java.util.Date;

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
		//gets current date object
		Date temp = new Date(time*1000);
		
		// for simulation milliseconds represent seconds.
		return temp.getTime();
//		return time%86400;
	}
	
	public Date getCurrentDate()
	{
		long time = System.currentTimeMillis();
		//gets current date object
		Date temp = new Date(time*1000);
		
		return temp;
	}
}
