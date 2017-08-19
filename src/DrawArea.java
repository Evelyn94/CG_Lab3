import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

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

	//TODO step2
	boolean fill=false;
	int cX;
	int cY;
	int bc;

	
	public DrawArea(Dimension dim, DrawIt drawit) {
		this.setPreferredSize(dim);
		offscreen = new BufferedImage(dim.width, dim.height,
				BufferedImage.TYPE_INT_RGB);
		this.dim = dim;
		this.drawit = drawit;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

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

	//TODO step2
	public void myFill(BufferedImage image, int x, int y, int bc, int nc){

		Queue<Point> Q = new LinkedList<Point>();
		Q.add(new Point(x,y));
		while (Q.size()>0){
			Point p = Q.remove();
			if(image.getRGB(p.x,p.y) !=-1  || image.getRGB(p.x,p.y) == nc) continue;
			image.setRGB(p.x,p.y,nc);
			Point[] ps = {new Point(p.x-1,p.y), new Point(p.x+1,p.y), new Point(p.x,p.y-1), new Point(p.x,p.y+1)};
			for(int i=0;i<4;i++){
				if(image.getRGB(ps[i].x,ps[i].y)==-1 && ps[i].x>0 && ps[i].x<dim.width-3 && ps[i].y>0 && ps[i].y<dim.height-3){
					Q.add(ps[i]);
				}
			}
		}


	}
	
	public void mouseDragged(MouseEvent m) {
		Graphics2D g = offscreen.createGraphics();

		//TODO step3 mix color
		Color temp = (Color) drawit.colorToolbar.getSelectCommand();
		Color base = new Color(offscreen.getRGB(m.getX(),m.getY()));
		Color mix;
		if (base.getRGB()==-1){
			mix = temp;
		}else {
			mix = new Color(temp.getRGB()+base.getRGB());
		}
		System.out.println("temp:"+temp);
		System.out.println("base:"+base);

		System.out.println("mix:"+mix);
		Color color = new Color(mix.getRed(),mix.getGreen(),mix.getBlue(),drawit.trpSlider.getValue());
		bc = color.getRGB();
		g.setColor(color);

		//System.out.println("Color:"+drawit.colorToolbar.getSelectCommand());
		//TODO step1 thickness
		g.setStroke(new BasicStroke(drawit.thkSlider.getValue()/10));
		//`System.out.println("Slide Value is : " + drawit.aSlider.getValue());
	
		//g.fill(new Ellipse2D.Double(m.getX() - 1.0, m.getY() - 1.0, 2.0, 2.0));

		//TODO step2 spray
		if(drawit.paintToolbar.getSelectCommand()=="spray"){
			for(int i=0;i<10;i++){
				Random random = new Random();
				int r1 = random.nextInt(5);
				int r2 = random.nextInt(5);
				g.draw(new Line2D.Double(m.getX()+r1,m.getY()+r2,m.getX()+r1,m.getY()+r2));
			}
		}

		if(drawit.paintToolbar.getSelectCommand()=="line"){
			g.draw(new Line2D.Double(lastX,lastY,m.getX(),m.getY()));
		}

		drawOffscreen();

		lastX = m.getX();
		lastY = m.getY();

	}

	public void mouseMoved(MouseEvent m) {
	}

	public void mouseClicked(MouseEvent e) {

		//TODO step2
		if(drawit.paintToolbar.getSelectCommand()=="fill"){
			fill=true;
			cX = e.getX();
			cY = e.getY();
		}

		if(fill){
			myFill(offscreen, cX, cY,bc, ((Color) drawit.colorToolbar.getSelectCommand()).getRGB());
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
	}

	public void export(File file) {
		try {
			ImageIO.write(offscreen, "png", file);
		} catch (IOException e) {
			System.out.println("problem saving file");
		}
	}
}
