package smartpod;

import com.janezfeldin.Math.Vector2D;

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
	public String roadName		= null;
	public String nodeName		= null;
	public double D = 0.0; // the road distance. G = D + W
	public double C = 0.0; // the movement cost to move from the parent node. 
	public double G = 0.0; // acumulated cost
	public double H = 0.0; // the estimated movement cost to move to the end node.
	public double F = 0.0; // F = G + C + H
	
	public boolean opened = false;
	public boolean closed = false;
	
//	public PFNode(PFNode parentNode, String nodeName, String roadName, double weight, Vector2D startPosition, Vector2D endPosition, Vector2D finalPosition)
//	{
//		this.parentNode = parentNode;
//		this.nodeName = nodeName;
//		this.roadName = roadName;
//		this.D = startPosition.dist(endPosition)*distanceWeight;
//		this.H = finalPosition.dist(endPosition)*heuristicWeight + 1;
//		this.C = D + weight;
//		this.F = G + C + H;
//	}
	
	public PFNode(RoadAgent road)
	{
		this.nodeName = road.endNode;
		this.roadName = road.getLocalName();
		this.D = road.startPosition.dist(road.endPosition)*distanceWeight;
		this.C = D + road.weight;
		this.position = road.endPosition;
	}
	
	public PFNode(String nodeName)
	{
		this.nodeName = nodeName;
	}

	public void setRoadWeight(double weight)
	{
		this.G = D + weight;
		this.F = G + C + H;
	}
	
	public void setFinalPosition(Vector2D finalPosition)
	{
		this.H = finalPosition.dist(position)*heuristicWeight + 1;
		this.F = G + C + H;
	}
	
	public void setParentNode(PFNode parentNode)
	{
		this.parentNode = parentNode;
		this.G = (parentNode != null ? parentNode.G + parentNode.C : 0.0);
		this.F = G + C + H;
	}
	
	@Override
	public String toString()
	{
		return "===="+roadName+"===>"+nodeName;//+" parentNode : "+parentNode;
	}
}
