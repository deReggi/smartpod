package de.reggi.jade;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

/**
 * MyReceiver: a more user friendly ReceiverBehaviour.
 *
 * Creation: new MyReceiver( Agent, Timeout (or -1), MessageTemplate )
 *
 * - terminates when 1) desired message is received OR timeout expires 
 * - on termination, handle(msg) is called
 */
public class MyReceiver extends SimpleBehaviour
{
	private MessageTemplate template;
	private long timeOut,
			wakeupTime;
	private boolean finished;
	private ACLMessage msg;

	public ACLMessage getMessage()
	{
		return msg;
	}

	public MyReceiver(Agent a, int millis, MessageTemplate mt)
	{
		super(a);
		timeOut = millis;
		template = mt;
	}

	@Override
	public void onStart()
	{
		wakeupTime = (timeOut < 0 ? Long.MAX_VALUE : System.currentTimeMillis() + timeOut);
	}

	@Override
	public boolean done()
	{
		return finished;
	}

	@Override
	public void action()
	{
		if (template == null)
		{
			msg = myAgent.receive();
		}
		else
		{
			msg = myAgent.receive(template);
		}

		if (msg != null)
		{
			finished = true;
			handle(msg);
			return;
		}
		long dt = wakeupTime - System.currentTimeMillis();
		if (dt > 0)
		{
			block(dt);
		}
		else
		{
			finished = true;
			handle(msg);
		}
	}

	public void handle(ACLMessage m)
	{
		/* can be redefined in sub_class */
	}

	@Override
	public void reset()
	{
		msg = null;
		finished = false;
		super.reset();
	}

	public void reset(int dt)
	{
		timeOut = dt;
		reset();
	}
}