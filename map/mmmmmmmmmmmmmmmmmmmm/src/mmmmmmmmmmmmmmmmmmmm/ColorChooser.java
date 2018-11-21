package mmmmmmmmmmmmmmmmmmmm;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;


public class ColorChooser {
	/*
	 * selected is the colour which is selected when the frame is open 
	 * fromInside decides if the color chosen to change all edges color or just some edges 
	 * 
	 */
	
	
	private JFrame frame;
	static Color selected = Color.BLACK;
	static boolean fromInside = true ; 
	static boolean fontColor = false ; 
	/**
	 * Create the application.
	 */
	public ColorChooser() {
		initialize();
	}
	
	

	public Color selectedColor(){
		return selected;
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 624, 430);
		frame.setVisible(true);
		frame.setTitle("Color");
		JButton btnOk = new JButton("Ok");
		
		JColorChooser colorchooser = new JColorChooser();
		
		colorchooser.getSelectionModel().setSelectedColor(Color.BLACK);	
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Color temp1 =colorchooser.getSelectionModel().getSelectedColor();
				if (fontColor){
					//change font colour 
					Shape temp = null;
					for (int i=0 ; i<GUItest.list.size()   ; i++ ){
						temp = GUItest.list.get(i);
						// get clicked vertex then clear it 
						if(	GUItest.popupX < temp.x + temp.width && GUItest.popupX > temp.x && 
							GUItest.popupY > temp.y && GUItest.popupY <temp.y + temp.width ){
							temp.fontColor = temp1;
							GUItest.refresh();
							break ; 
						}
					}
					fontColor = false ; 
					
				}
				else if (!fromInside){
					//change all edges colour 
					selected = temp1; 
					for (int i=0 ; i<GUItest.Edgelist.size() ; i++)
						GUItest.Edgelist.get(i).color = selected; 
					fromInside = true ;
					
				}
				else{
					//change cretin edge color 
					DrawEdge temp ;
					for (int i=0 ; i<GUItest.Edgelist.size()   ; i++ ){
						temp = GUItest.Edgelist.get(i);
						// get clicked vertex then clear it 
						if ((GUItest.popupX <= temp.xSource + (temp.xDis - temp.xSource) && GUItest.popupX >= temp.xSource && 
								GUItest.popupY <= (temp.yDis + temp.ySource)/2  + 10 &&  GUItest.popupY >= (temp.yDis + temp.ySource)/2 - 10 )
								|| (GUItest.popupX <= temp.xDis + (temp.xSource - temp.xDis) && GUItest.popupX >= temp.xDis && 
									GUItest.popupY <= (temp.yDis + temp.ySource)/2  + 10 &&  GUItest.popupY >= (temp.yDis + temp.ySource)/2 - 10 ))
							GUItest.Edgelist.get(i).color = temp1; 
						
						fromInside = false ; 
						
						
						}
							
				}
				frame.setVisible(false);
				GUItest.refresh();
			}
		});
		
		JPanel ColorPanel = new JPanel();
		ColorPanel.add(colorchooser); 
		

		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(308, Short.MAX_VALUE)
					.addComponent(btnOk)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addContainerGap())
				.addComponent(ColorPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(ColorPanel, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOk)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
