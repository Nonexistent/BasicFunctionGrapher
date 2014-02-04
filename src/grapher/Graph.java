package grapher;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class Graph {
	private double Xmax = 10;
	private double Xmin = -10;
	private double Ymax = 10;
	private double Ymin = -10;
	private int xImageLength = 0;
	private int yImageLength = 0;
	private double xImageOrigin = 0;
	private double yImageOrigin = 0;
	private double xImageIncrement = 0;
	private double yImageIncrement = 0;
	private BufferedImage graphArea;
	private Graphics2D g;
	Path2D.Double line = new Path2D.Double();

	public Graph(Gui gui) {
		this.graphArea = gui.getGraphArea();
		this.xImageLength = this.graphArea.getWidth();
		this.yImageLength = this.graphArea.getHeight();
		this.g = (Graphics2D) gui.getGraphics();
		this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void init() {
		// Draws the x and y axis and increments
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		xImageIncrement = (xImageLength) / (double)(Math.abs(Xmin) + Math.abs(Xmax));
		yImageIncrement = (yImageLength) / (double)(Math.abs(Ymin) + Math.abs(Ymax));
		xImageOrigin = -Xmin * xImageIncrement;
		yImageOrigin = Ymax * yImageIncrement;
		double xh1 = 0.0, yh1 = yImageOrigin, xh2 = xImageLength;
		double xv1 = xImageOrigin, yv1 = 0.0, yv2 = yImageLength;
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLACK);
		g.draw(new Line2D.Double(xh1, yh1, xh2, yh1));
		g.draw(new Line2D.Double(xv1, yv1, xv1, yv2));
		g.setColor(Color.decode("#d3d3d3"));
		for(int i = 0; i < Math.abs(Xmin) + Math.abs(Xmax); i++){
			double x = i * xImageIncrement;
			if(x != xv1){
			g.draw(new Line2D.Double(x, yv1, x, yImageLength));
			}
		}
		for(int i = 0; i < Math.abs(Ymin) + Math.abs(Ymax); i++){
			double y = i * yImageIncrement;
			if(y != yh1){
			g.draw(new Line2D.Double(xh1, y, xImageLength, y));
			}
		}
	}

	public void plot(double[][] xyValues) {
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.RED);
		line.moveTo(xyValues[0][0], xyValues[1][0]);
		for(int i = 0; i < xImageLength; i++){
			if(xyValues[1][i] != Double.MAX_EXPONENT && xyValues[1][i] != Double.MIN_EXPONENT){
			line.lineTo(xyValues[0][i], xyValues[1][i]);
			}else{
				if(xyValues[1][i] == Double.MAX_EXPONENT){
					line.lineTo(xyValues[0][i], Double.MIN_EXPONENT);
				}else if(xyValues[1][i] == Double.MIN_EXPONENT){
					line.lineTo(xyValues[0][i], Double.MAX_EXPONENT);
				}
				g.draw(line);
				line.reset();
				line.moveTo(xyValues[0][i], xyValues[1][i]);
			}
		}
		g.draw(line);
		line.reset();
	}

	public double functionToImage(double input) {
		if (input > 0) {
			return yImageOrigin - (yImageIncrement * input);
		}
		return yImageOrigin + (yImageIncrement * Math.abs(input));
	}
	
	public double getxImageIncrement() {
		return this.xImageIncrement;
	}

	public double getXmax() {
		return this.Xmax;
	}

	public double getXmin() {
		return this.Xmin;
	}
	
	public double getYmin(){
		return this.Ymin;
	}
	
	public double getYmax(){
		return this.Ymax;
	}
	public void setXmax(double xmax) {
		Xmax = xmax;
	}

	public void setXmin(double xmin) {
		Xmin = xmin;
	}

	public void setYmax(double ymax) {
		Ymax = ymax;
	}

	public void setYmin(double ymin) {
		Ymin = ymin;
	}

	
	public int getxImageLength(){
		return this.xImageLength;
	}

}
