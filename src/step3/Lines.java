package step3;


import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Lines {
    ArrayList<MovableLine> lines;

    public Lines(){
        lines = new ArrayList<MovableLine>();
    }

    public void addLine(MovableLine line){
        lines.add(line);
    }

    public void draw(Graphics2D g, Color c){
        g.setStroke(new BasicStroke(5));
        g.setColor(c);
        double r=2;

        for(MovableLine l:lines){
            g.draw(l.line);
            if(l.select){
                g.setColor(Color.black);
                g.draw(new Ellipse2D.Double(l.line.getX1()-r,l.line.getY1()-r,r*2,r*2));
                g.draw(new Ellipse2D.Double(l.line.getX2()-r,l.line.getY2()-r,r*2,r*2));
                g.setColor(c);
            }
        }
    }

    public void selectline(double x, double y){
        for(MovableLine l:lines)
        {
            l.select=false;
        }
        int index=-1;
        double min = 40;

        for(int i=0;i<lines.size();i++){
            double dis = lines.get(i).line.ptLineDist(x,y);
            if(dis<min){
                index=i;
                min = dis;
            }
        }
        if(index!=-1){
            lines.get(index).select=true;
        }
    }

    public void moveline(double x,double y){
        for(MovableLine l:lines){
            if(l.select){
                l.line.setLine(l.line.getX1()+x,l.line.getY1()+y,l.line.getX2()+x,l.line.getY2()+y);
            }
        }
    }

    public void setselect(){
        for(MovableLine l:lines){
            l.select = false;
        }
    }
}
