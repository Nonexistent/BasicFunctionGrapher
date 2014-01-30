package grapher;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Button extends JButton{
	
	public Button(final JTextField functionLabel, final String input){
		super(new AbstractAction(input){
			@Override
			public void actionPerformed(ActionEvent e) {
				functionLabel.setText(new StringBuilder(functionLabel.getText()).insert(functionLabel.getCaretPosition(), input).toString());
				functionLabel.requestFocusInWindow();
			}
		});
	}

}
