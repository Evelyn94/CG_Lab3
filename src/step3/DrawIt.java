package step3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/*
 * DrawIt - Simple Drawing Program
 * Eric McCreath 2009, 2017
 */

public class DrawIt implements Runnable {

	static final Dimension dim = new Dimension(800,600);
	
	JFrame jf;
	DrawArea da;
	JMenuBar bar;
	JMenu jmfile;
	JMenuItem jmiquit, jmiexport;
	ToolBar colorToolbar;

	//TODO step2
	ToolBar paintToolbar;


	JPanel toolsJPanel;
	
	public DrawIt() {
		SwingUtilities.invokeLater(this);
	}
	
	public void run() {
		jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		da = new DrawArea(dim,this);
		//da.setFocusable(true);
		jf.getContentPane().add(da,BorderLayout.CENTER);
		
		
		toolsJPanel = new JPanel();
		toolsJPanel.setLayout(new BoxLayout(toolsJPanel, BoxLayout.PAGE_AXIS));
		
		// create a toolbar
		colorToolbar = new ToolBar(BoxLayout.Y_AXIS);
		colorToolbar.addbutton("Red", Color.RED);
		colorToolbar.addbutton("Blue", Color.BLUE);
		colorToolbar.addbutton("Green", Color.GREEN);
		toolsJPanel.add(colorToolbar);

		
		jf.getContentPane().add(toolsJPanel,BorderLayout.LINE_END);

		// TODO step3
		paintToolbar = new ToolBar(BoxLayout.Y_AXIS);
		paintToolbar.addbutton("Vector Line","line");
		paintToolbar.addbutton("Move Line","move");
		toolsJPanel.add(paintToolbar);
		
		// create some menus
		bar = new JMenuBar();
		jmfile = new JMenu("File");
		jmiexport = new JMenuItem("Export");
		jmfile.add(jmiexport);
		jmiexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				da.export(new File("export.png"));
			}});
		
		jmiquit = new JMenuItem("Quit");
		jmfile.add(jmiquit);
		jmiquit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}});
		bar.add(jmfile);
		jf.setJMenuBar(bar);
		
		jf.pack();
		jf.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		DrawIt sc = new DrawIt();
	}
}
