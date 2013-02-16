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
public class NodeAgent extends UISAgent
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
    
    public NodeAgent(Point position)
    {
        this.position = position;
    }
    
    @Override
    protected void setup()
    {
        
        addBehaviour(new NodeAgentBehaviout(this));
    }
    
    
    
    public class NodeAgentBehaviout extends CyclicBehaviour
    {
        public NodeAgentBehaviout(Agent a)
        {
            super(a);
        }

        @Override
        public void action()
        {
        }
    }
    
}
