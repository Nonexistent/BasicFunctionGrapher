package grapher.managers;

import grapher.Function;
import grapher.Graph;
import grapher.gui.LinearRegressionPanel;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.TreeSet;

public class LinearRegression extends ManagerBase {
	private double[][] xyValues;
	LinkedList<String> temp = new LinkedList<String>();
	private BufferedReader reader;
	private final TreeSet<Double> filter = new TreeSet<Double>();
	LinearRegressionPanel panel;
	final Color color = Color.decode("#00A300");
	public double[][] x;
	public double[] y;
	double intercept = 0;
	double slope = 0;

	public LinearRegression(Graph graph, LinearRegressionPanel panel) {
		super(graph);
		this.panel = panel;
	}

	public void drawLine(double learnRate) {
		gradiantDescent(learnRate);
		String expression = Double.toString(slope) + "x+" + Double.toString(intercept);
		new Function("line", expression, color, this, graph, null);
	}

	public LinearRegression submitData(File file) {
		try {
			parseFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < xyValues[0].length; i++) {
			filter.add(xyValues[0][i]);
		}
		graph.setXmax(filter.last() + 3);
		graph.setXmin(filter.first() - 3);
		filter.clear();
		for (int i = 0; i < xyValues[1].length; i++) {
			filter.add(xyValues[1][i]);
		}
		graph.setYmax(filter.last() + 3);
		graph.setYmin(filter.first() - 3);
		filter.clear();
		panel.clearGraph();
		graph.init();
		updateValues();
		for (int i = 0; i < xyValues[0].length; i++) {
			xyValues[0][i] = (xyValues[0][i] - Xmin) * xImageIncrement;
			xyValues[1][i] = functionToImage(xyValues[1][i]);
		}
		graph.plotPoints(xyValues);
		panel.getPanel().repaint();
		return this;
	}

	private void parseFile(File file) throws Exception {
		String g = "";
		reader = new BufferedReader(new FileReader(file));
		while ((g = reader.readLine()) != null) {
			temp.add(g);
		}
		reader.close();
		x = new double[2][temp.size()];
		y = new double[temp.size()];
		xyValues = new double[2][temp.size()];
		for (int i = 0; i < xyValues[0].length; i++) {
			String[] t = temp.get(i).split(",");
			x[0][i] = 1;
			x[1][i] = Double.parseDouble(t[0]);
			y[i] = Double.parseDouble(t[1]);
			xyValues[0][i] = Double.parseDouble(t[0]);
			xyValues[1][i] = Double.parseDouble(t[1]);
		}
	}

	private void gradiantDescent(double learnRate) {
		int m = y.length;
		double prevCost = cost();
		do {
			prevCost = cost();
			double s0 = 0;
			double s1 = 0;
			for (int a = 0; a < m; a++) {
				s0 += (((x[0][a] * intercept) + (x[1][a] * slope)) - y[a]) * x[0][a];
			}
			for (int a = 0; a < m; a++) {
				s1 += (((x[0][a] * intercept) + (x[1][a] * slope)) - y[a]) * x[1][a];
			}
			double temp0 = intercept - ((learnRate / m) * s0);
			double temp1 = slope - ((learnRate / m) * s1);
			intercept = temp0;
			slope = temp1;
		} while (prevCost - cost() > 0.0000001);
		System.out.println("cost: " + cost());
	}

	private double cost() {
		double[] p = new double[x[0].length];
		double sum = 0;
		for (int a = 0; a < p.length; a++) {
			p[a] = (x[0][a] * intercept) + (x[1][a] * slope);
		}
		for (int a = 0; a < p.length; a++) {
			p[a] = Math.pow((p[a] - y[a]), 2);
		}
		for (double a : p) {
			sum += a;
		}
		return sum / (2 * p.length);
	}

	private double functionToImage(double input) {
		return input > 0 ? yImageOrigin - (yImageIncrement * input)
				: yImageOrigin + (yImageIncrement * Math.abs(input));
	}
}
