package grapher;

import javax.swing.UIManager;

public class Main {
	
	/*
	 * Author: Nonexistent
	 * Started on: January 16th 2014
	 * Last Update: http://www.powerbot.org/community/topic/1142029-a-basic-function-grapher/
	 * 
	 */
	
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			//DO.NOTHING.....!
		}
		Gui g = new Gui();
		g.go();
	}
	 
	
}
