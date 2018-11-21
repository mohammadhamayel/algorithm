package mmmmmmmmmmmmmmmmmmmm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.Serializable;


public class DrawEdge implements Serializable{
	int xSource, ySource , xDis ,yDis  ; 
	Color color = ColorChooser.selected;
	Double cost = 0.0  ; 
	Shape source , distination ; 
	int type ; 
	boolean weighted = false ; 
	/* 
	 * type 0 for undirected links
	 * type 1 for directed links 
	 * type 3 for minimum spanning tree  
	*/
	
	
	
	public DrawEdge() {
		
	}
	
	public DrawEdge(int type) {
		this.type = type ; 
	}
	
	
	public DrawEdge(int x1, int  y1 ,int x2,int y2 ){
		this.xSource = x1 ; 
		this.xDis = x2 ; 
		this.ySource = y1 ; 
		this.yDis = y2 ; 
	}
	
	public static void drawArrow(Graphics g1, int x1, int y1, int x2, int y2, Color color) {
	   final int ARR_SIZE = 8;
       Graphics2D g = (Graphics2D) g1.create();

       double dx = x2 - x1, dy = y2 - y1;
       double angle = Math.atan2(dy, dx);
       int len = (int) Math.sqrt(dx*dx + dy*dy) ;// distance between two points 
       AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
       at.concatenate(AffineTransform.getRotateInstance(angle));
       g.transform(at);

       // Draw horizontal arrow starting in (0, 0)
       g.setColor(color);
       g.drawLine(0, 0, len, 0);
       // draw the direction shape
       g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                     new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
       }
	
	public void paintComponent (Graphics g ){
		g.setColor(color);
		// draw thick line using stock 
		((Graphics2D)g).setStroke(new BasicStroke(3));
		((Graphics2D)g).draw(new Line2D.Float(xSource, ySource, xDis, yDis));
		
		// for directed graphs 
		if (this.type == 1 )
			drawArrow(g, xSource, ySource, xDis, yDis,color);

		g.setFont(new Font("Serif", Font.BOLD, 20));
		if ( this.weighted )
			g.drawString(this.cost+"", (xSource + xDis) /2 - 5 , (ySource + yDis) /2 - 5  );
	}
}
