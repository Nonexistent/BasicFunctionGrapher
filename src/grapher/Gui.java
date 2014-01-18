package grapher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gui {
	private JFrame frame = new JFrame("Graph");
	private BufferedImage graphArea = new BufferedImage(280, 250, BufferedImage.TYPE_INT_RGB);
	private JLabel function = new JLabel();
	private FunctionManager functionManager;
	private Graph graph;
	private LinkedHashMap<Double, Double> yMap;
	
	public Gui(){
		this.graph = new Graph(this);
		this.yMap = this.graph.getMap();
	}
	
	private void createFM(){
		this.graph.init();
		this.functionManager = new FunctionManager(this.graph);
	}
	
	public void go(){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setSize(420, 500);
		frame.getContentPane().add(graph());
		createFM();
		frame.getContentPane().add(label());
		frame.getContentPane().add(buttons());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void clearGraph(){
		graphArea.getGraphics().setColor(Color.WHITE);
		graphArea.getGraphics().fillRect(0, 0, graphArea.getWidth(), graphArea.getHeight());
	}
	
	private Component graph(){
		JPanel p = new JPanel();
		JLabel label = new JLabel(new ImageIcon(this.graphArea));
		clearGraph();
		p.add(label);
		return p;
	}
	
	private Component label(){
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		p.add(new JLabel("f(x) = "));
		p.add(function);
		return p;
	}
	
	@SuppressWarnings("serial")
	private Component numbers(){
		JPanel inner = new JPanel(new GridLayout(4, 3, 3, 3));//row, colomn
		inner.add(new JButton(new AbstractAction(" 1 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "1");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 2 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "2");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 3 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "3");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 4 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "4");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 5 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "5");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 6 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "6");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 7 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "7");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 8 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "8");
			}
		}));
		inner.add(new JButton(new AbstractAction(" 9 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "9");
			}
		}));
		inner.add(new JPanel());
		inner.add(new JButton(new AbstractAction(" 0 "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "0");
			}
		}));
		return inner;
	}
	
	@SuppressWarnings("serial")
	private Component operators(){
		JPanel p = new JPanel(new GridLayout(4,2,3,3));
		p.add(new JButton(new AbstractAction(" + "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "+");
			}
		}));
		p.add(new JButton(new AbstractAction(" - "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "-");
			}
		}));
		p.add(new JButton(new AbstractAction(" * "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "*");
			}
		}));
		p.add(new JButton(new AbstractAction(" / "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "/");
			}
		}));
		p.add(new JButton(new AbstractAction(" ^ "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "^");
			}
		}));
		p.add(new JButton(new AbstractAction("X"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "x");
			}
		}));
		p.add(new JButton(new AbstractAction("("){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + "(");
			}
		}));
		p.add(new JButton(new AbstractAction(" ) "){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText(function.getText() + ")");
			}
		}));
		return p;
	}
	
	@SuppressWarnings("serial")
	private Component func(){
		JPanel p = new JPanel(new GridLayout(4,2,3,3));
		p.add(new JButton(new AbstractAction("Back"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = function.getText();
				function.setText(s.replace(s.substring(s.length() - 1), ""));
			}
		}));
		p.add(new JButton(new AbstractAction("Clear All"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText("");
				clearGraph();
				yMap.clear();
				graph.init();
				frame.repaint();
			}
		}));
		p.add(new JButton(new AbstractAction("Clear Expression"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				function.setText("");
				yMap.clear();
				frame.repaint();
			}
		}));
		p.add(new JButton(new AbstractAction("Graph"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Gui.this.functionManager.calculate(function.getText());
				graph.plot();
				frame.repaint();
			}
		}));
		return p;
	}
	
	private Component buttons(){
		JPanel p = new JPanel();
		p.add(numbers());
		p.add(operators());
		p.add(func());
		return p;
	}
	
	public BufferedImage getGraphArea(){
		return this.graphArea;
	}
	
	public Graphics getGraphics(){
		return this.graphArea.getGraphics();
	}
}
