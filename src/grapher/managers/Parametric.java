package grapher.managers;

import grapher.Function;
import grapher.Graph;

public class Parametric extends ManagerBase{
	private double min = 0;
	private double max = 0;
	private double[][] xyValues;
	private String x;
	private String y;
	
	public Parametric(Graph graph, String x, String y, double min, double max) {
		super(graph);
		this.x = x;
		this.y = y;
		this.min = min;
		this.max = max;
	}
	
	public void draw(){
		double[] tempx = new Function(min, max, x).value;
		double[] tempy = new Function(min, max, y).value;
		xyValues = new double[2][tempx.length];
		for(int i = 0; i < tempx.length; i++){
			xyValues[0][i] = (tempx[i] - Xmin) * xImageIncrement;
			xyValues[1][i] = functionToImage(tempy[i]);
		}
		graph.plotLine(xyValues);
	}
	
	private double functionToImage(double input) {
		return input > 0 ?
			yImageOrigin - (yImageIncrement * input) :
		 yImageOrigin + (yImageIncrement * Math.abs(input));
	}
}
