package grapher.gui;

import grapher.Graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelBase{
	protected final BufferedImage graphArea = new BufferedImage(280, 250, BufferedImage.TYPE_INT_RGB);
	public Graph graph;
	protected final JPanel panel = new JPanel();

	public PanelBase() {
		this.graph = new Graph(this);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(graph());
		graph.init();
	}
	
	public JPanel getPanel(){
		return panel;
	}

	public void clearGraph() {
		graphArea.getGraphics().setColor(Color.WHITE);
		graphArea.getGraphics().fillRect(0, 0, graphArea.getWidth(), graphArea.getHeight());
	}

	protected Component graph() {
		JPanel p = new JPanel();
		JLabel label = new JLabel(new ImageIcon(this.graphArea));
		clearGraph();
		p.add(label);
		return p;
	}

	public BufferedImage getGraphArea() {
		return this.graphArea;
	}

	public Graphics getGraphics() {
		return this.graphArea.getGraphics();
	}
}
