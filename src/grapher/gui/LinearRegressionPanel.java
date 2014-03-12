package grapher.gui;

import grapher.managers.LinearRegression;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LinearRegressionPanel extends PanelBase {
	private final JFileChooser fileChooser = new JFileChooser();
	private final JTextField learnRate = new JTextField();

	public LinearRegressionPanel(JFrame frame) {
		panel.add(learnRate);
		panel.add(fileFinder());
		panel.add(options());
	}

	private JPanel fileFinder(){
		final JPanel p = new JPanel();
		p.add(new JButton(new AbstractAction("Data File"){
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(p);
			}
		}));
		return p;
	}
	
	private JPanel options(){
		JPanel p = new JPanel();
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		left.setLayout(new GridLayout(1, 2, 3, 3));
		left.add(new JLabel("Learn Rate:"));
		left.add(learnRate);
		right.add(new JButton(new AbstractAction("Submit"){
			@Override
			public void actionPerformed(ActionEvent e) {
				double lr = Double.parseDouble(learnRate.getText());
				if(fileChooser.getSelectedFile() != null){
					new LinearRegression(graph, LinearRegressionPanel.this).submitData(fileChooser.getSelectedFile()).drawLine(lr);
				}
			}
		}));
		p.add(left);
		p.add(right);
		return p;
	}
}
