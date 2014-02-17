package grapher;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Button extends JButton {

	public Button(final JFrame frame, final String input) {
		super(new AbstractAction(input) {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (frame.getMostRecentFocusOwner().getClass().equals(JTextField.class)) {
					JTextField functionField = (JTextField) frame.getMostRecentFocusOwner();
					functionField.setText(new StringBuilder(functionField.getText()).insert(functionField.getCaretPosition(), input).toString());
					functionField.requestFocusInWindow();
				}
			}
		});
		setFocusable(false);
	}

}
