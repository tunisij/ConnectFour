package package1;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ConnectFour {

	//-----------------------------------------------------------------
	//  Creates and displays the main program frame.
	//-----------------------------------------------------------------
	public static void main (String[] args)
	{
		JMenuBar menus;
		JMenu fileMenu;
		JMenuItem quitItem;
		JMenuItem gameItem;
		JMenuItem statsItem;
		
		fileMenu = new JMenu ("File:");
		statsItem = new JMenuItem("Stats");
		gameItem = new JMenuItem("Reset");
		quitItem = new JMenuItem("Quit");
		
		JFrame frame = new JFrame ("Connect Four");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		fileMenu.add(statsItem);
		fileMenu.add(gameItem);
		fileMenu.add(quitItem);
		menus = new JMenuBar();
		menus.add(fileMenu);
		
		frame.setJMenuBar(menus);
		
		ConnectFourPanel panel = new ConnectFourPanel
				(quitItem, gameItem, statsItem);
		
		frame.getContentPane().add(panel);

//		      frame.pack();
		frame.setSize(500, 300);
		frame.setVisible(true);
	}
}