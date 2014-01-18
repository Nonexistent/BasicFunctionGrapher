package grapher;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class Graph {
	private int Xmax = 10;
	private int Xmin = -10;
	private int Ymax = 10;
	private int Ymin = -10;
	private int xLength = 0;
	private int yLength = 0;
	private double topIncre;
	private double bottomIncre;
	private double rightIncre;
	private double leftIncre;
	private BufferedImage graphArea;
	private Graphics2D g;
	private LinkedHashMap<Double, Double> yMap = new LinkedHashMap<Double, Double>();
	
	public Graph(Gui gui){
		this.graphArea = gui.getGraphArea();
		this.xLength = this.graphArea.getWidth();
		this.yLength = this.graphArea.getHeight();
		this.g = (Graphics2D)gui.getGraphics();
	}
	
	public void init(){
		//Draws the x and y axis and increments
		int xh1 = 0; int yh1 = yLength/2;
		int xh2 = xLength;
		int xv1 = xLength/2; int yv1 = 0;
		int yv2 = yLength;
		topIncre = (yLength/2)/(Math.abs(Ymax));
		bottomIncre = (yLength/2)/(Math.abs(Ymin));
		leftIncre = (xLength/2)/(Math.abs(Xmin));
		rightIncre = (xLength/2)/(Math.abs(Xmax));
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLACK);
		g.drawLine(xh1, yh1, xh2, yh1);
		g.drawLine(xv1, yv1, xv1, yv2);
		g.setColor(Color.decode("#d3d3d3"));
		for(double i = (xLength/2) + rightIncre; i < xLength; i = i + rightIncre){
			g.drawLine((int)i, 0, (int)i, yLength);
		}
		for(double i = (xLength/2) - leftIncre; i > 0; i = i - leftIncre){
			g.drawLine((int)i, 0, (int)i, yLength);
		}
		for(double i = (yLength/2) - topIncre; i > 0; i = i - topIncre){
			g.drawLine(0, (int)i, xLength, (int)i);
		}
		for(double i = (yLength/2) + bottomIncre; i < yLength; i = i + bottomIncre){
			g.drawLine(0, (int)i, xLength, (int)i);
		}
	}
	
	public void plot(){
		//CHANGE INTO DOING EACH SIDE SEPARATLY; FOR LEFT SIDE, AND THEN FOR RIGHT SIDE
		g.setColor(Color.RED);
		for(Double d : this.yMap.keySet()){
			g.setStroke(new BasicStroke(2));
			g.draw(new Line2D.Double(d, this.yMap.get(d), d, this.yMap.get(d)));
		}
	}
	
	public double functionToImage(double input){
		if(input > 0){
			return (yLength/2) - ((Math.abs(topIncre) * input));
		}
		return (yLength/2) + ((Math.abs(bottomIncre) * Math.abs(input)));
	}
	
	public LinkedHashMap<Double, Double> getMap(){
		return this.yMap;
	}

	public double getTop() {
		return this.topIncre;
	}

	public double getBottom() {
		return this.bottomIncre;
	}

	public double getRight() {
		return this.rightIncre;
	}

	public double getLeft() {
		return this.leftIncre;
	}
	
	public int getXmax(){
		return this.Xmax;
	}
	
	public int getXmin(){
		return this.Xmin;
	}
	
}
