/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 *
 * @author Janez Feldin
 */
public class JunctionAgent extends UISAgent
{
    private Point position;
    
    public Point getPosition()
    {
        return position;
    }
    public void setPosition(Point position)
    {
        this.position = position;
    }
    
    public JunctionAgent(Point position)
    {
        this.position = position;
    }
    
    @Override
    protected void setup()
    {
        
        addBehaviour(new CrossroadAgentBehaviout(this));
    }
    
    
    
    public class CrossroadAgentBehaviout extends CyclicBehaviour
    {
        public CrossroadAgentBehaviout(Agent a)
        {
            super(a);
        }

        @Override
        public void action()
        {
        }
    }
    
}
