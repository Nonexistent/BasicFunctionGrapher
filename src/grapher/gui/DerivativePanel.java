package grapher.gui;

import grapher.Function;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class DerivativePanel {
	private final JPanel panel = new JPanel();
	private final JTextArea output = new JTextArea();
	
	public DerivativePanel(){
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(components());
	}
	
	@SuppressWarnings("serial")
	private JPanel components(){
		JPanel main = new JPanel();
		JPanel p = new JPanel();
		JPanel i = new JPanel();
		JPanel o = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		final JTextField input = new JTextField(25);
		input.setText("(8*x)/x");
		i.add(new JLabel("f(x) = "));
		i.add(input);
		p.add(i);
		o.add(new JButton(new AbstractAction("Derive"){
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = input.getText();
				if(!s.equals("")){
					output.setText(new Function(s).getDerivative());
				}
			}
		}));
		p.add(o);
		p.add(output());
		main.add(p);
		return main;
	}
	
	private JPanel output(){
		JPanel p = new JPanel();
		JScrollPane scroll = new JScrollPane(output);
		p.setBorder(BorderFactory.createTitledBorder("f '(x) = "));
		scroll.setPreferredSize(new Dimension(230, 100));
		output.setEditable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		output.setFont(new Font("Arial", Font.PLAIN, 12));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		((DefaultCaret)output.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.add(scroll);
		return p;
	}
	
	public JPanel getPanel(){
		return panel;
	}
}
