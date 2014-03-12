package grapher.managers;

import grapher.Function;
import grapher.Graph;
import grapher.InputForm;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FunctionManager extends ManagerBase {

	public FunctionManager(Graph graph) {
		super(graph);
	}

	@SuppressWarnings("unchecked")
	public void completeFunctions(InputForm form, final JFrame frame, final JTextArea statusArea) {
		for (JPanel p : form.getFormList()) {
			final JTextField field = (JTextField) p.getComponent(2);
			if (!field.getText().equals("")) {
				final String name = ((JLabel) p.getComponent(0)).getText();
				final int color = ((BufferedImage)((ImageIcon)((JComboBox<ImageIcon>)p.getComponent(3)).getSelectedItem()).getImage()).getRGB(0, 0);
				(new Thread(new Runnable() {
					@Override
					public void run() {
						new Function(name, field.getText(), new Color(color), FunctionManager.this, graph, statusArea);
						frame.repaint();
					}

				})).start();
			}
		}
	}
}
