package grapher.managers;

import grapher.Graph;

public class ManagerBase {
	protected Graph graph;
	public double xImageIncrement;
	public double Xmin;
	public int xImageLength;
	public double yImageOrigin = 0;
	public double yImageIncrement = 0;
	
	public ManagerBase(Graph graph){
		this.graph = graph;
		this.xImageIncrement = graph.getxImageIncrement();
		this.Xmin = graph.getXmin();
		this.xImageLength = graph.getxImageLength();
		this.yImageIncrement = graph.getYImageIncrement();
		this.yImageOrigin = graph.getYImageOrigin();
	}
	
	public void updateValues() {
		this.Xmin = graph.getXmin();
		this.xImageIncrement = graph.getxImageIncrement();
		this.yImageIncrement = graph.getYImageIncrement();
		this.yImageOrigin = graph.getYImageOrigin();
	}
}
