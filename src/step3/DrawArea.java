package step3;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*
 * DrawArea - a simple JComponent for drawing.  The "offscreen" BufferedImage is 
 * used to draw to,  this image is then used to paint the component.
 * Eric McCreath 2009 2015, 2017
 */

public class DrawArea extends JComponent implements MouseMotionListener,
		MouseListener {

	private BufferedImage offscreen;
	Dimension dim;
	DrawIt drawit;

	//TODO step1
	int lastX;
	int lastY;

	//TODO step3
	Lines lines;




	
	public DrawArea(Dimension dim, DrawIt drawit) {
		this.setPreferredSize(dim);
		offscreen = new BufferedImage(dim.width, dim.height,
				BufferedImage.TYPE_INT_RGB);
		this.dim = dim;
		this.drawit = drawit;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		//TODO step3
		lines = new Lines();

		clearOffscreen();
	}

	public void clearOffscreen() {
		Graphics2D g = offscreen.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);
		repaint();
	}

	public Graphics2D getOffscreenGraphics() {
		return offscreen.createGraphics();
	}

	public void drawOffscreen() {
		repaint();
	}

	protected void paintComponent(Graphics g) {
		g.drawImage(offscreen, 0, 0, null);
	}

	
	public void mouseDragged(MouseEvent m) {
		//TODO step3
		clearOffscreen();
		Graphics2D g = offscreen.createGraphics();
		Color color = (Color) drawit.colorToolbar.getSelectCommand();
		g.setColor(color);
		g.setStroke(new BasicStroke(5));


		if(drawit.paintToolbar.getSelectCommand()=="line"){
			lines.setselect();
			g.draw(new Line2D.Double(lastX,lastY,m.getX(),m.getY()));
		}else {
			lines.moveline(m.getX()-lastX,m.getY()-lastY);
			lastX = m.getX();
			lastY = m.getY();
		}
		lines.draw(g,color);

		drawOffscreen();

	}

	public void mouseMoved(MouseEvent m) {
	}

	public void mouseClicked(MouseEvent e) {
		//TODO step3
		clearOffscreen();
		Graphics2D g = offscreen.createGraphics();
		Color color = (Color) drawit.colorToolbar.getSelectCommand();
		g.setColor(color);
		g.setStroke(new BasicStroke(5));
		if(drawit.paintToolbar.getSelectCommand()=="move"){
			lines.selectline(e.getX(),e.getY());
			lines.draw(g,color);
		}
		drawOffscreen();


	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();


	}

	public void mouseReleased(MouseEvent e) {
		//TODO step3
		if(drawit.paintToolbar.getSelectCommand()=="line") {
			lines.addLine(new MovableLine(lastX, lastY, e.getX(), e.getY()));
		}
	}

	public void export(File file) {
		try {
			ImageIO.write(offscreen, "png", file);
		} catch (IOException e) {
			System.out.println("problem saving file");
		}
	}
}
