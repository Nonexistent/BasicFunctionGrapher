package grapher.gui;

import grapher.managers.Parametric;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ParametricPanel extends PanelBase {
	private final JTextField x = new JTextField(17);
	private final JTextField y = new JTextField(17);
	private final JTextField min = new JTextField(5);
	private final JTextField max = new JTextField(5);

	public ParametricPanel() {
		panel.add(settingButton());
		panel.add(textFields());
		min.setText("0");
		max.setText("7");
	}

	private JPanel settingButton() {
		JPanel p = new JPanel();
		p.add(new JButton(new AbstractAction("Settings") {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings();
			}
		}));
		return p;
	}

	private void settings() {
		final JFrame f = new JFrame();
		final JPanel main = new JPanel();
		final JPanel top = new JPanel();
		final JPanel middle = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		top.add(new JLabel("t-min ="));
		top.add(min);
		middle.add(new JLabel("t-max ="));
		middle.add(max);
		main.add(top);
		main.add(middle);
		main.add(new JButton(new AbstractAction("Done") {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.dispose();
			}
		}));
		f.getContentPane().add(main);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private JPanel textFields() {
		final JPanel main = new JPanel();
		final JPanel labels = new JPanel();
		final JPanel texts = new JPanel();
		labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
		texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
		labels.add(new JLabel("x(t) ="));
		labels.add(new JPanel());
		labels.add(new JLabel("y(t) ="));
		texts.add(x);
		texts.add(new JPanel());
		texts.add(y);
		main.add(labels);
		main.add(texts);
		main.add(new JButton(new AbstractAction("Graph") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Parametric(graph, x.getText(), y.getText(), Double.parseDouble(min.getText()), Double.parseDouble(max.getText())).draw();
				panel.repaint();
			}
		}));
		return main;
	}
}
