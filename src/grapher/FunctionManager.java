package grapher;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FunctionManager {
	private Graph graph;
	public double xImageIncrement;
	public double Xmin;
	public int xImageLength;
	public double yImageOrigin = 0;
	public double yImageIncrement = 0;

	public FunctionManager(Graph graph) {
		this.graph = graph;
		this.xImageIncrement = graph.getxImageIncrement();
		this.Xmin = graph.getXmin();
		this.xImageLength = graph.getxImageLength();
		this.yImageIncrement = graph.getYImageIncrement();
		this.yImageOrigin = graph.getYImageOrigin();
	}

	@SuppressWarnings("unchecked")
	public void completeFunctions(InputForm form, final JFrame frame, final JTextArea statusArea) {
			for(JPanel p : form.getFormList()){
				final JTextField field = (JTextField) p.getComponent(2);
				if(!field.getText().equals("")){
					final String name = ((JLabel) p.getComponent(0)).getText();
					final int color = ((BufferedImage)((ImageIcon)((JComboBox<ImageIcon>) p.getComponent(3)).getSelectedItem()).getImage()).getRGB(0, 0);
					(new Thread(new Runnable(){
						@Override
						public void run() {
							new Function(name, field.getText(), new Color(color), FunctionManager.this, graph, statusArea);
							frame.repaint();
						}
						
					})).start();
				}
			}
	}

	public void updateValues() {
		this.Xmin = graph.getXmin();
		this.xImageIncrement = graph.getxImageIncrement();
		this.yImageIncrement = graph.getYImageIncrement();
		this.yImageOrigin = graph.getYImageOrigin();
	}
}
