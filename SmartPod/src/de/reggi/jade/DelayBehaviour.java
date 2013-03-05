package de.reggi.jade;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

/**
 *
 * @author Andreas
 */
public class DelayBehaviour extends SimpleBehaviour
{

	private long	timeout,
					wakeupTime;
	private boolean	finished = false;

	public DelayBehaviour(Agent a, long timeout)
	{
		super(a);
		this.timeout = timeout;
	}

	@Override
	public void onStart()
	{
		wakeupTime = System.currentTimeMillis() + timeout;
	}

	@Override
	public void action()
	{
		long dt = wakeupTime - System.currentTimeMillis();
		if (dt <= 0)
		{
			finished = true;
			handleElapsedTimeout();
		}
		else
		{
			block(dt);
		}

	}

	protected void handleElapsedTimeout()
	{
		/* can be redefined in sub_class */
	}

	@Override
	public boolean done()
	{
		return finished;
	}
}