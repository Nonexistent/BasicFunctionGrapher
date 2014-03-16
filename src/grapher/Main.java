package grapher;

import grapher.gui.GraphingPanel;
import grapher.gui.LinearRegressionPanel;
import grapher.gui.ParametricPanel;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/*
 * Author: Nonexistent
 * Started on: January 16th 2014
 * Last Update: http://www.powerbot.org/community/topic/1142029-a-basic-function-grapher/
 * 
 */

public class Main {
	
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			//DO.NOTHING.....!
		}
		final JFrame frame = new JFrame("Graph");
		final JTabbedPane tabs = new JTabbedPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());
		GraphingPanel graphingPanel = new GraphingPanel(frame);
		final JPanel functionPanel = graphingPanel.getFunctionPanel();
		frame.getContentPane().add(functionPanel);
		functionPanel.setVisible(false);
		tabs.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(functionPanel.isVisible()){
					functionPanel.setVisible(false);
					frame.pack();
				}
			}
		});
		tabs.add("Graph", graphingPanel.getPanel());
		tabs.add("Linear Regression", new LinearRegressionPanel(frame).getPanel());
		tabs.add("Parametric", new ParametricPanel().getPanel());
		frame.getContentPane().add(tabs);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	 
	
}
