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
public class RoadAgent extends UISAgent
{

    private String startNode = "";
    private String endNode = "";
    private Point startPosition;
    private Point endPosition;
    private String roadBelongingType = "";//(inbound ali outgoing) inbound - cesta pripada vozlišču v katerega se izteka; outgoing - cesta pripada vozlišču iz katerega izhaja

    public RoadAgent(String startNode, String endNode, Point startPosition, Point endPosition, String roadBelongingType)
    {
        this.startNode = startNode;
        this.endNode = endNode;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.roadBelongingType = roadBelongingType;
    }

    public String getStartNode()
    {
        return startNode;
    }

    public String getEndNode()
    {
        return endNode;
    }

    public Point getStartPosition()
    {
        return startPosition;
    }

    public Point getEndPosition()
    {
        return endPosition;
    }

    public String getParentNode()
    {
        if (roadBelongingType.equals("inbound"))
        {
            return endNode;
        }
        else
        {
            return startNode;
        }
    }

    public void setStartNode(String startNode)
    {
        this.startNode = startNode;
        throw new UnsupportedOperationException("Še ne dela, poišči lokacijo od vozlišča z imaneom" + startNode);
    }

    public void setEndNode(String endNode)
    {
        this.endNode = endNode;
        throw new UnsupportedOperationException("Še ne dela, poišči lokacijo od vozlišča z imaneom" + endNode);
    }

    public void setStartPosition(Point startPosition)
    {
        this.startPosition = startPosition;
        throw new UnsupportedOperationException("Še ne dela, poišči še ime vozlišča na lokaciji" + startPosition);
    }

    public void setEndPosition(Point endPosition)
    {
        this.endPosition = endPosition;
        throw new UnsupportedOperationException("Še ne dela, poišči še ime vozlišča na lokaciji" + endPosition);
    }

    public void setRoadBelongingType(String roadBelongingType)
    {
        this.roadBelongingType = roadBelongingType;
    }

    @Override
    protected void setup()
    {

        addBehaviour(new RoadAgentBehaviour(this));
    }

    public class RoadAgentBehaviour extends CyclicBehaviour
    {

        public RoadAgentBehaviour(Agent a)
        {
            super(a);
        }

        @Override
        public void action()
        {
//            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
