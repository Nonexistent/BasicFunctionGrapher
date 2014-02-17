package grapher;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputForm {
	private String[] color = { "#59c3e2", "#ff0000", "#00A300", "#751975" };
	private ImageIcon[] icons = new ImageIcon[color.length];
	private JPanel[] formList = new JPanel[color.length];
	
	@SuppressWarnings("unchecked")
	public InputForm(){
		for(int i = 0; i < color.length; i++){
			BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.decode(color[i]));
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			icons[i] = new ImageIcon(image);
			g.dispose();
		}
		for(int i = 0; i < formList.length; i++){
			JPanel p = new JPanel();
			p.add(new JLabel("y" + (i +1))); //name, index 0
			p.add(new JLabel(" = "));        //equals sign, index 1
			p.add(new JTextField(8));        //field. index 2
			p.add(new JComboBox<ImageIcon>(icons)); //color, index 3
			((JComboBox<ImageIcon>)p.getComponent(3)).setSelectedIndex(i);
			formList[i] = p;
		}
	}
	
	public JPanel[] getFormList(){
		return this.formList;
	}
}
