package grapher.gui;

import grapher.Button;
import grapher.InputForm;
import grapher.managers.FunctionManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class GraphingPanel extends PanelBase {
	private JPanel functionPanel = new JPanel();
	private JTextArea statusArea = new JTextArea("Hi there.");
	private JScrollPane scroll;
	private final InputForm form = new InputForm();
	private FunctionManager functionManager;
	private JFrame frame;
	private final String[] functionArray = {"abs()", "log()", "ln()", "sin()", "cos()", "tan()"};
	private final String[] operatorArray = {"+", "-", "*", "/", "^", "x", "(", ")"};
	
	public GraphingPanel(JFrame frame){
		this.frame = frame;
		this.functionManager = new FunctionManager(graph);
		panel.add(buttons());
	}
	public JPanel getFunctionPanel(){
		JPanel p = new JPanel();
		functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createTitledBorder("Status:"));
		scroll = new JScrollPane(statusArea);
		statusArea.setEditable(false);
		statusArea.setOpaque(false);
		statusArea.setLineWrap(true);
		statusArea.setWrapStyleWord(true);
		statusArea.setFont(new Font("Arial", Font.PLAIN, 9));
		for(JPanel o : form.getFormList()){
			functionPanel.add(o);
		}
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		((DefaultCaret)statusArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.add(scroll);
		functionPanel.add(p);
		return functionPanel;
	}
	
	private Component numbers(){
		JPanel inner = new JPanel(new GridLayout(3, 2, 3, 3));//row, colomn
		for(String s : functionArray){
			inner.add(new Button(frame, s));
		}
		return inner;
	}
	
	private Component operators(){
		JPanel p = new JPanel(new GridLayout(4,2,3,3));
		for(String s : operatorArray){
			p.add(new Button(frame, s));
		}
		return p;
	}
	
	private Component func(){
		JPanel p = new JPanel(new GridLayout(4,2,3,3));
		p.add(new JButton(new AbstractAction("Settings"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				settings();
			}
		}));
		p.add(new JButton(new AbstractAction("Clear All"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearGraph();
				for(JPanel p : form.getFormList()){
					((JTextField) p.getComponent(2)).setText("");
				}
				graph.init();
				statusArea.setText("");
				frame.repaint();
			}
		}));
		p.add(new JButton(new AbstractAction("Show Functions"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(functionPanel.isVisible()){
					this.putValue(NAME, "Show Functions");
					functionPanel.setVisible(!functionPanel.isVisible());
					frame.pack();
				}else{
					this.putValue(NAME, "Hide Functions");
					functionPanel.setVisible(!functionPanel.isVisible());
					frame.pack();
					scroll.setPreferredSize(new Dimension(functionPanel.getWidth() - 30, 100));
					scroll.revalidate();
				}
			}
		}));
		p.add(new JButton(new AbstractAction("Graph"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				functionManager.completeFunctions(form, frame, statusArea);
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
	
	private Component settings(){
		final JFrame f = new JFrame("Settings");
		JPanel top = new JPanel();
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		JPanel x = new JPanel();
		JPanel y = new JPanel();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.setSize(240, 160);
		final JTextField xMin = new JTextField(Double.toString(graph.getXmin()), 8);
		final JTextField xMax = new JTextField(Double.toString(graph.getXmax()), 8);
		final JTextField yMin = new JTextField(Double.toString(graph.getYmin()), 8);
		final JTextField yMax = new JTextField(Double.toString(graph.getYmax()), 8);
		x.add(new JLabel("xMin:")); x.add(xMin); x.add(new JLabel("xMax:")); x.add(xMax); 
		y.add(new JLabel("yMin:")); y.add(yMin); y.add(new JLabel("yMax:")); y.add(yMax);
		top.add(x); middle.add(y);
		bottom.add(new JButton(new AbstractAction("Ok"){
			@Override
			public void actionPerformed(ActionEvent e) {
				graph.setXmin(Double.parseDouble(xMin.getText()));
				graph.setXmax(Double.parseDouble(xMax.getText()));
				graph.setYmin(Double.parseDouble(yMin.getText()));
				graph.setYmax(Double.parseDouble(yMax.getText()));
				clearGraph();
				graph.init();
				functionManager.updateValues();
				functionManager.completeFunctions(form, frame, statusArea);
				frame.repaint();
				f.dispose();
			}
		}));
		f.getContentPane().add(top);
		f.getContentPane().add(middle);
		f.getContentPane().add(bottom);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		return f;
	}
}
