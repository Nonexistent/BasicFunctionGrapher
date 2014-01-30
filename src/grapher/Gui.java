package grapher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Gui {
	private JFrame frame = new JFrame("Graph");
	private BufferedImage graphArea = new BufferedImage(280, 250, BufferedImage.TYPE_INT_RGB);
	private JTextField functionLabel = new JTextField(25);
	private FunctionManager functionManager;
	private Graph graph;
	private Function function;
	private String[] functionArray = {"abs()", "log()", "ln()", "sin()", "cos()", "tan()"};
	private String[] operatorArray = {"+", "-", "*", "/", "^", "x", "(", ")"};
	
	public Gui(){
		this.graph = new Graph(this);
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
		p.add(functionLabel);
		return p;
	}
	
	private Component numbers(){
		JPanel inner = new JPanel(new GridLayout(3, 2, 3, 3));//row, colomn
		for(String s : functionArray){
			inner.add(new Button(this.functionLabel, s));
		}
		return inner;
	}
	
	private Component operators(){
		JPanel p = new JPanel(new GridLayout(4,2,3,3));
		for(String s : operatorArray){
			p.add(new Button(this.functionLabel, s));
		}
		return p;
	}
	
	@SuppressWarnings("serial")
	private Component func(){
		JPanel p = new JPanel(new GridLayout(4,2,3,3));
		p.add(new JButton(new AbstractAction("Back"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = functionLabel.getText();
				functionLabel.setText(s.substring(0, s.length() - 1));
			}
		}));
		p.add(new JButton(new AbstractAction("Clear All"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				functionLabel.setText("");
				clearGraph();
				graph.init();
				frame.repaint();
			}
		}));
		p.add(new JButton(new AbstractAction("Clear Expression"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				functionLabel.setText("");
				frame.repaint();
			}
		}));
		p.add(new JButton(new AbstractAction("Graph"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!functionLabel.getText().equals("")){
				function = new Function(functionLabel.getText(), functionManager);
				graph.plot(function.getMap());
				frame.repaint();
				}
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
