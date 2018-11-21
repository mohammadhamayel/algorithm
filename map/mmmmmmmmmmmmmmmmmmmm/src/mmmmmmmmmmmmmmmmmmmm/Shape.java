package mmmmmmmmmmmmmmmmmmmm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.Serializable;

/*
 * Shape is the same as draw vertex 
 * x,y are the coordination of the vertex 
 * Vertex name represents the name of the vertex 
 * number represents vertex number or count according to other vertices 
 * icon is the image which is drawn on the screen 
 */


public class Shape implements Serializable{
	int x , y , height = 64 , width = 64 ; 
	String VertexName ;
	Color color = Color.BLACK ; 
	Color fontColor = Color.RED; 
	int number ;
	Image icon = Toolkit.getDefaultToolkit().getImage("ylw-blank.png");
	

	public Shape() {}
	
	private Shape(Shape that ) {
		this.VertexName = that.VertexName;
		this.color = that.color ; 
		this.height = that.height; 
		this.icon = that.icon ; 
		this.number = that.number; 
		this.width = that.width; 
		this.x= that.x; 
		this.y = that.y; 	
	}
	
	public Shape(String name ) {
		this.VertexName = name ; 
		number = Integer.parseInt(name.substring(1));
	}
	
	
	public void paintComponent (Graphics g ){
		/*
		 * Draw items to screen 
		 */
		
//		g.fillOval(x, y, width , height);
		g.setColor(color);
		g.drawImage(icon, x, y, null);
		
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.setColor(fontColor);
		if (VertexName == null)
			VertexName = "" ; 
		g.drawString(VertexName,( x + width/3 - VertexName.length() ) , y+width/3); 
		g.finalize();
	}
	
	// draw new Slide 
	public void clear (Graphics g ){
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		
	}
	
	
	@Override
	protected Shape clone() throws CloneNotSupportedException {
		Shape shape = new Shape(this);
		return shape;
	}

	// new Slide 
	public static void newSlide (Graphics g  , int x , int y ){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, x, y);
	}
}
