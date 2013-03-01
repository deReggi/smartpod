package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;

/**
 * The path finding node wrapper class.
 * 
 * @author Andreas
 */
public class PFNode
{
	private static final double distanceWeight	= 0.01;
	private static final double heuristicWeight = 0.01;
	
	private Vector2D position = null;
	
	public PFNode parentNode	= null;
	public AID roadAID			= null;
	public AID nodeAID			= null;
	public double D = 0.0; // the road distance. C = D + W
	public double C = 0.0; // the movement cost to move from the parent node.
	public double P = 0.0; // the movement cost to the parent node
	public double G = 0.0; // G = P + C
	public double H = 0.0; // the estimated movement cost to move to the end node.
	public double F = 0.0; // F = G + H
	
	public boolean opened = false;
	public boolean closed = false;
	
//	public PFNode(PFNode parentNode, String nodeAID, String roadAID, double weight, Vector2D startPosition, Vector2D endPosition, Vector2D finalPosition)
//	{
//		this.parentNode = parentNode;
//		this.nodeAID = nodeAID;
//		this.roadAID = roadAID;
//		this.D = startPosition.dist(endPosition)*distanceWeight;
//		this.H = finalPosition.dist(endPosition)*heuristicWeight + 1;
//		this.C = D + weight;
//		this.F = G + C + H;
//	}
	
	public PFNode(RoadAgent road)
	{
		this.nodeAID = road.endNode;
		this.roadAID = road.getAID();
		this.D = road.startPosition.dist(road.endPosition)*distanceWeight;
		this.C = D + road.weight;
		this.G = P + C;
		this.position = road.endPosition;
	}
	
	public PFNode(AID nodeAID)
	{
		this.nodeAID = nodeAID;
	}

	public void setRoadWeight(double weight)
	{
		this.C = D + weight;
		this.G = P + C;
		this.F = G + H;
	}
	
	public void setFinalPosition(Vector2D finalPosition)
	{
		this.H = finalPosition.dist(position)*heuristicWeight + 1;
		this.F = G + H;
	}
	
	public void setParentNode(PFNode parentNode)
	{
		this.parentNode = parentNode;
		this.P = (parentNode != null ? parentNode.G + parentNode.C : 0.0);
		this.G = P + C;
		this.F = G + H;
	}
	
	@Override
	public String toString()
	{
		return "===="+roadAID.getLocalName()+"===>"+nodeAID.getLocalName();//+" parentNode : "+parentNode;
	}
}
