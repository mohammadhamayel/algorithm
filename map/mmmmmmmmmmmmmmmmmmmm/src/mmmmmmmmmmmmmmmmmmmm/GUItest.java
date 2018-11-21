package mmmmmmmmmmmmmmmmmmmm;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;


public class GUItest implements ActionListener , MouseListener  {
	static String ImagePath = null;

	JMenuItem mntmExit, mntmPaste,mntmCopy_1,mntmDelet,mntmMove,mntmLabel,mntmCut,SourceVertex, DestinationVertex,cost_Edge,fontColor;
	JRadioButtonMenuItem rdbtnmntmDirectedweigthedGraph,rdbtnmntmUndirectedunweighted,rdbtnmntmMinSpanning;
	JMenu mnType;
	static JFrame frame;
	static ArrayList<Shape> list = new ArrayList<Shape>();
	static ArrayList<DrawEdge> Edgelist = new ArrayList<DrawEdge>();
	ArrayList<DrawEdge> changeSourceLocation = new ArrayList<DrawEdge>();
	ArrayList<DrawEdge> changeDisLocation = new ArrayList<DrawEdge>();
	JToggleButton tglbtnVetex,tglbtnLink,tglbtnLinkDirected,tglbtnText;
	static JPanel DrowPanel; 
	JPanel FindPathPanel,minSpanningTreePanell;
	JLabel lblMessage,lblTypeOfGraph,lblCost_minSpanningTree,lblCostMinResult;
	DrawEdge edge = new  DrawEdge(0); 
	boolean source = true, lastClickFromMove = false, mouseEventfired = false, textChanged = false, drawVertex = false    ; 
	JComboBox<String> ComboVerteciesStart,comboBoxVerticesEnd;
	JLabel lblCost,lblPath,lbCostResult,lblPathResult;
	JRadioButton rdbtnFindShortestPath ; 
	JButton btnShortestPath,btnFindMinCost;
	JPopupMenu popupMenu,popupMenu_Edge;
	static int popupX, popupY; 
	Shape copiedVertex, draggedShape; 
	DrawEdge newEdge;
	ButtonGroup type = new ButtonGroup();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new JFrame();
					GUItest window = new GUItest();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUItest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame.setBounds(100, 100, 900, 601);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Graph");
		
		DrowPanel = new JPanel();
		
		DrowPanel.addMouseListener(this);
		lblMessage = new JLabel("");
		
		FindPathPanel = new JPanel();
		FindPathPanel.setVisible(false );
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    // ask for saving before closing 
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if (list.size() > 0 ){
					int result = JOptionPane.showConfirmDialog(null, "Do You want to save changes to Untitled?", "Graph", JOptionPane.YES_NO_OPTION);
					switch (result ){
					case 0:  {// yes 
						save();
						// return the icon value 
						for (int i=0 ; i<list.size() ; i++)
							list.get(i).icon = Toolkit.getDefaultToolkit().getImage("ylw-blank.png");
						refresh();
					}
					case 1:// no
						break; 
						
					}
		    	}
		    		System.exit(0);
//		        if (JOptionPane.showConfirmDialog(frame, 
//		            "Are you sure to close this window?", "Really Closing?", 
//		            JOptionPane.YES_NO_OPTION,
//		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//		            System.exit(0);
//		        }
		    }
		});

		
		rdbtnFindShortestPath = new JRadioButton("Find Shortest Path ");
		rdbtnFindShortestPath.addActionListener(this);
		rdbtnFindShortestPath.setSelected(false);
		
		lblMessage.setForeground(Color.red);
		JToolBar toolBar = new JToolBar();
		
		lblTypeOfGraph = new JLabel("Undirected Unweighted Graph");
		
		minSpanningTreePanell = new JPanel();
		minSpanningTreePanell.setVisible(false);
		
		btnTopologicalSort = new JButton("Topological Sort");
		
		btnTopologicalSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// sort vertices 
				if (Edgelist.size()>=1){
					Graph g = new Graph((DrawEdge[])Edgelist.toArray(new DrawEdge[Edgelist.size()]), (Shape[]) list.toArray(new Shape[list.size()]),list.size()); 
					g.buildGraph();
					String Path = g.topologicalSort();
					lblMessage.setText(Path.substring(0,Path.length()-4));
					
				}
				else 
					lblMessage.setText("Draw some Verteices ");
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(42)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(DrowPanel, GroupLayout.PREFERRED_SIZE, 833, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(rdbtnFindShortestPath, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
							.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
											.addComponent(lblTypeOfGraph, GroupLayout.PREFERRED_SIZE, 364, GroupLayout.PREFERRED_SIZE)
											.addGap(39)
											.addComponent(btnTopologicalSort)
											.addGap(120))
										.addComponent(lblMessage, GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE))
									.addComponent(FindPathPanel, GroupLayout.PREFERRED_SIZE, 827, GroupLayout.PREFERRED_SIZE)
									.addComponent(minSpanningTreePanell, GroupLayout.PREFERRED_SIZE, 776, GroupLayout.PREFERRED_SIZE))
								.addGap(19)))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnTopologicalSort)
								.addComponent(lblTypeOfGraph, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(26))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(DrowPanel, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblMessage)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnFindShortestPath)
					.addGap(10)
					.addComponent(FindPathPanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(minSpanningTreePanell, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		btnFindMinCost = new JButton("Find Min Cost");
		
		lblCost_minSpanningTree = new JLabel("Cost");
		
		lblCostMinResult = new JLabel("");
		
		btnFindMinCost.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				for (int i=0 ; i<Edgelist.size();i++)
					Edgelist.get(i).color = ColorChooser.selected;
				
				lblCost_minSpanningTree.setVisible(true);
				lblCostMinResult.setText("");
				if (!(list.size() > 0 && Edgelist.size() > 0))
					lblMessage.setText("Draw Complete Graph (vertices with edges)");
				else {
					// create graph find min spanning tree 
					// draw it 
					// find min cost 
					Graph g = new Graph((DrawEdge[])Edgelist.toArray(new DrawEdge[Edgelist.size()]), (Shape[]) list.toArray(new Shape[list.size()]),list.size()); 
					g.buildGraph();
								
					double mincost = g.min_spanning_tree(list.get(0));
					
					lblCostMinResult.setText(mincost+"");
					lblCostMinResult.setForeground(Color.RED);			
					
					String splitPath[] = g.Path.split("\n");
					// print path 
					for (int j =0 ; j<splitPath.length ; j++){
						String commaSplit[] = splitPath[j].split(",");
						for (int i =0 ; i<Edgelist.size() ; i++){
							// color edges 
							if (Edgelist.get(i).source.number == Integer.parseInt(commaSplit[0])
							&& Edgelist.get(i).distination.number == Integer.parseInt(commaSplit[1]))
								Edgelist.get(i).color = Color.BLUE;
							
							if (Edgelist.get(i).distination.number == Integer.parseInt(commaSplit[0])
									&& Edgelist.get(i).source.number == Integer.parseInt(commaSplit[1]))
								Edgelist.get(i).color = Color.BLUE;
									
						}
					}	
				}
				refresh();
			}
				
			
		});
		
		GroupLayout gl_minSpanningTreePanell = new GroupLayout(minSpanningTreePanell);
		gl_minSpanningTreePanell.setHorizontalGroup(
			gl_minSpanningTreePanell.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_minSpanningTreePanell.createSequentialGroup()
					.addGap(124)
					.addComponent(lblCost_minSpanningTree, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(lblCostMinResult, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
					.addComponent(btnFindMinCost, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_minSpanningTreePanell.setVerticalGroup(
			gl_minSpanningTreePanell.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_minSpanningTreePanell.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_minSpanningTreePanell.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFindMinCost)
						.addComponent(lblCostMinResult)
						.addComponent(lblCost_minSpanningTree, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		minSpanningTreePanell.setLayout(gl_minSpanningTreePanell);
		
		
		tglbtnVetex = new JToggleButton("Vetex");
		tglbtnVetex.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\source.png"));
		toolBar.add(tglbtnVetex);
		
		tglbtnVetex.setToolTipText("Vertex");
		tglbtnLink = new JToggleButton("Link --");
		toolBar.add(tglbtnLink);
		tglbtnLink.setToolTipText("Use for undirected Graph");
		tglbtnLinkDirected = new JToggleButton("Link-->"); 
		toolBar.add(tglbtnLinkDirected);
		tglbtnLinkDirected.setToolTipText("Use for directed Graph");
		tglbtnText = new JToggleButton("Text");
		tglbtnText.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\text.png"));
		toolBar.add(tglbtnText);
		tglbtnText.setToolTipText("Change Name or cost");
		tglbtnText.addActionListener(this);
		tglbtnLinkDirected.addActionListener(this );
		tglbtnLink.addActionListener(this);
		
		
		tglbtnVetex.addActionListener(this);
		
		btnShortestPath = new JButton("Shortest Path");
		btnShortestPath.addActionListener(this);
		
		ComboVerteciesStart = new JComboBox();
		
		comboBoxVerticesEnd = new JComboBox();
		
		JLabel lblStart = new JLabel("Start ");
		
		JLabel lblEnd = new JLabel("End");
		
		lblCost = new JLabel("Cost");
		lblCost.setVisible(false);
		
		lbCostResult = new JLabel("h");
		
		lblPath = new JLabel("Path");
		lblPath.setVisible(false);
		
		lblPathResult = new JLabel("");
		GroupLayout gl_FindPathPanel = new GroupLayout(FindPathPanel);
		gl_FindPathPanel.setHorizontalGroup(
			gl_FindPathPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FindPathPanel.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_FindPathPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_FindPathPanel.createSequentialGroup()
							.addComponent(lblStart)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ComboVerteciesStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(41)
							.addComponent(lblEnd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxVerticesEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(27)
							.addComponent(lblCost, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lbCostResult, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_FindPathPanel.createSequentialGroup()
							.addComponent(lblPath)
							.addGap(18)
							.addComponent(lblPathResult, GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShortestPath)
					.addGap(56))
		);
		gl_FindPathPanel.setVerticalGroup(
			gl_FindPathPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_FindPathPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FindPathPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStart)
						.addComponent(ComboVerteciesStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEnd)
						.addComponent(comboBoxVerticesEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCost)
						.addComponent(btnShortestPath)
						.addComponent(lbCostResult))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_FindPathPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPath)
						.addComponent(lblPathResult))
					.addContainerGap())
		);
		FindPathPanel.setLayout(gl_FindPathPanel);
		frame.getContentPane().setLayout(groupLayout);

		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\new.png"));
		mnFile.add(mntmNew);
		
		//------------------------------------------------------------------------------------------------------
		mntmNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);
				saveCurrentWork();
				ImagePath = null;
			}
		});
		
		JMenuItem mntmOpen = new JMenuItem("Open File");
		mntmOpen.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\open.png"));
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				
				// ask to save 
				saveCurrentWork();
				
				// open file chooser 
				JFileChooser inputFile = new JFileChooser(); 
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				inputFile.setFileFilter(filter);
				int x = inputFile.showOpenDialog(mntmOpen);
				if ( x == JFileChooser.APPROVE_OPTION){
					File  selectedFile = inputFile.getSelectedFile();
					String File = selectedFile.getAbsolutePath().replace("\\", "\\\\");
					try{
						// read file accourding to the format 
						/*
						 * format : 
						 * number of vertices then vertices 
						 * number of edges then edges 
						 * int counter value
						 * int type of graph from 1-5 as follow 
						 * 1 --> undirected unweighted graph 
						 * 2 --> undirected weighed graph 
						 * 3 --> directed weighed graph (dijkstra)
						 * 4 --> directed unweighed graph 
						 * 5 --> minspanning tree 
						 */
						FileInputStream fileInput = new FileInputStream(File);
						ObjectInputStream input = new ObjectInputStream(fileInput);
						list = new ArrayList<Shape>(); 
						Edgelist = new ArrayList<DrawEdge>();
						int numOfVertices = input.readInt();
						for (int i =0 ; i <numOfVertices ; i++){
							// read vertices 
							Shape temp = (Shape) input.readObject();
							temp.icon = Toolkit.getDefaultToolkit().getImage("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\ylw-blank.png");
							list.add(temp);
						}
						int numOfEdges = input.readInt();
						for (int i =0 ; i <numOfEdges ; i++)
							// read edges 
							Edgelist.add((DrawEdge)input.readObject());
						counter = input.readInt();
						checkGraphType(input.readInt());
						ImagePath = (String)input.readObject();
						refresh();
						input.close();
					}catch (IOException ex ){
						ex.printStackTrace();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\save.png"));
		mnFile.add(mntmSave);
		
		mntmSave.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				save();
				// return the icon value 
				for (int i=0 ; i<list.size() ; i++)
					list.get(i).icon = Toolkit.getDefaultToolkit().getImage("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\ylw-blank.png");
				refresh();
			}
		});
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmImportImage = new JMenuItem("Import Image");
		mntmImportImage.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\import.png"));
		mnFile.add(mntmImportImage);
		
		mntmImportImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				
				JFileChooser inputFile = new JFileChooser(); 
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
				inputFile.setFileFilter(filter);
				int x = inputFile.showOpenDialog(mntmOpen);
				if ( x == JFileChooser.APPROVE_OPTION){
					File  selectedFile = inputFile.getSelectedFile();
					ImagePath = selectedFile.getAbsolutePath().replace("\\", "\\\\");
				}
				refresh();	
					
			}
		});
		
		mntmExportAsImage = new JMenuItem("Export as Image");
		mntmExportAsImage.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\export.png"));
		mnFile.add(mntmExportAsImage);
		mntmExportAsImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsPNG();
				
			}
		});
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\close-icon.png"));
		mnFile.add(mntmExit);
		mntmExit.addActionListener(this);
		
		JMenu mnColor = new JMenu("Color");
		menuBar.add(mnColor);
		
		JMenuItem mntmOpenColorList = new JMenuItem("Open color list ");
		mntmOpenColorList.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\color-wheel-icon.png"));
		mnColor.add(mntmOpenColorList);
		
		mntmOpenColorList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);
				ColorChooser tempFrame = new ColorChooser();
				ColorChooser.fromInside = false ; 
			}
		});
		
		
		
		mnType = new JMenu("Type");
		menuBar.add(mnType);
		mnType.addMenuListener(new MenuListener() {			
			public void menuSelected(MenuEvent arg0) {
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);
				changeType();
//				lblTypeOfGraph.setText(""); 
				refresh();
			}
			
			public void menuDeselected(MenuEvent arg0) {}
			public void menuCanceled(MenuEvent arg0) {}
		});
		
		mnUndirectedGraph = new JMenu("Undirected Graph");
		mnType.add(mnUndirectedGraph);
		
				
		rdbtnmntmUndirectedunweighted = new JRadioButtonMenuItem("Undirected Unweighted Graph");
		mnUndirectedGraph.add(rdbtnmntmUndirectedunweighted);
		//Default 
		rdbtnmntmUndirectedunweighted.setSelected(true);
		
		
		rdbtnmntmUndirectedunweighted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setVisible(true);
				minSpanningTreePanell.setVisible(false);
				tglbtnLinkDirected.setEnabled(false);
				tglbtnLink.setEnabled(true);
				lblTypeOfGraph.setText("Undirected Unweighted Graph ");
				refresh();
			}
		});
		type.add(rdbtnmntmUndirectedunweighted);
		
		rdbtnmntmUndirectedWiegthedGraph = new JRadioButtonMenuItem("Undirected wiegthed Graph");
		mnUndirectedGraph.add(rdbtnmntmUndirectedWiegthedGraph);
		
		rdbtnmntmUndirectedWiegthedGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setVisible(true);
				minSpanningTreePanell.setVisible(false);
				tglbtnLinkDirected.setEnabled(false);
				tglbtnLink.setEnabled(true);
				lblTypeOfGraph.setText("Undirected Weighed Graph ");
				refresh();
			}
		});
		
		mnDirectedGraph = new JMenu("Directed Graph");
		mnType.add(mnDirectedGraph);
		//gropu of types 
		rdbtnmntmDirectedweigthedGraph = new JRadioButtonMenuItem("Directed weighted Graph ");
		mnDirectedGraph.add(rdbtnmntmDirectedweigthedGraph);
		
		rdbtnmntmDirectedweigthedGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setVisible(true);
				minSpanningTreePanell.setVisible(false);
				tglbtnLink.setEnabled(false);
				tglbtnLinkDirected.setEnabled(true);
				lblTypeOfGraph.setText("Directed Weigthed Graph");
				refresh();
			}
		});
		
		type.add(rdbtnmntmDirectedweigthedGraph);
		
		rdbtnmntmDirectedUnweightedGraph = new JRadioButtonMenuItem("Directed Unweighted Graph ");
		mnDirectedGraph.add(rdbtnmntmDirectedUnweightedGraph);
		tglbtnLinkDirected.setEnabled(false);
		
		rdbtnmntmDirectedUnweightedGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setVisible(true);
				minSpanningTreePanell.setVisible(false);
				tglbtnLink.setEnabled(false);
				tglbtnLinkDirected.setEnabled(true);
				lblTypeOfGraph.setText("Directed Unweigted Graph");
				refresh();
				
			}
		});
		
		rdbtnmntmMinSpanning = new JRadioButtonMenuItem("Minimum Spanning Tree");
		mnType.add(rdbtnmntmMinSpanning);
		
		rdbtnmntmMinSpanning.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblTypeOfGraph.setText("Undirected Weighted Graph (Minimum Spanning tree)");
				rdbtnFindShortestPath.setVisible(false);
				minSpanningTreePanell.setVisible(true);
				tglbtnLinkDirected.setEnabled(false);
				tglbtnLink.setEnabled(true);
				refresh();
			}
		});
		type.add(rdbtnmntmMinSpanning);
		type.add(rdbtnmntmUndirectedWiegthedGraph);
		type.add(rdbtnmntmDirectedUnweightedGraph);
		
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHowToUse = new JMenuItem("How to use ");
		mnHelp.add(mntmHowToUse);
		
		mntmHowToUse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);
				JOptionPane.showMessageDialog(null, "How To Use:\n"
						+ "Choose Type of Graph\n "
						+ "Use tool bar to Draw vertices\n "
						+ "Enter Cost if needed\n "
						+ "Have fun finding shortest path\\ minimum Cost. ");
			}
		});
		
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		mntmAbout.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);
				JOptionPane.showMessageDialog(null, "Algorithms_Project#3_Graphs\n"
						+ "Made by: Saja Hazem \n"
						+ "1130823\n"
						+ "2015_2016.");
				
			}
		});
	//-------------------------------------------------------------------------------------------	
		DrowPanel.setBackground(Color.WHITE);
		
		popupMenu = new JPopupMenu();
		addPopup(DrowPanel, popupMenu);
		
		popupMenu_Edge = new JPopupMenu();
		addPopupEdge(DrowPanel, popupMenu_Edge);
		
		cost_Edge = new JMenuItem("Cost");
		popupMenu_Edge.add(cost_Edge);
		
		cost_Edge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DrawEdge temp = null;
				double cost = -1 ; 
				for (int i=0 ; i<Edgelist.size()   ; i++ ){
					temp = Edgelist.get(i);
					// get clicked vertex then clear it 
					if (popupX <= temp.xSource + (temp.xDis - temp.xSource) && popupX >= temp.xSource && 
						popupY <= (temp.yDis + temp.ySource)/2  + 10 &&  popupY >= (temp.yDis + temp.ySource)/2 - 10){
						EditCost(temp);
						// ADD cost 
						cost = temp.cost; 
					}
					// add cost to the other same vertex 
					if (popupX <= temp.xDis + (temp.xSource - temp.xDis) && popupX >= temp.xDis && 
							popupY <= (temp.yDis + temp.ySource)/2  + 10 &&  popupY >= (temp.yDis + temp.ySource)/2 - 10)
						if (cost != -1 )
							temp.cost = cost ; 
						
				}
				popupMenu_Edge.setVisible(false);
				refresh();
			}
		});
		
		JMenuItem Delete_Edge = new JMenuItem("Delete");
		popupMenu_Edge.add(Delete_Edge);
		
		Delete_Edge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Delete vertex 
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				popupMenu.setVisible(false);
				popupMenu_Edge.setVisible(false);

				DrawEdge temp = null;
				for (int i=0 ; i<Edgelist.size()   ; i++ ){
					temp = Edgelist.get(i);
					// get clicked vertex then clear it 
					if ((popupX <= temp.xSource + (temp.xDis - temp.xSource) && popupX >= temp.xSource && 
						popupY <= (temp.yDis + temp.ySource)/2  + 10 &&  popupY >= (temp.yDis + temp.ySource)/2 - 10 )
						|| 
						(popupX <= temp.xDis + (temp.xSource - temp.xDis) && popupX >= temp.xDis && 
						popupY <= (temp.yDis + temp.ySource)/2  + 10 &&  popupY >= (temp.yDis + temp.ySource)/2 - 10 ))
						removeExrtaEdges(temp);
				}
				// remove the popup menu 
				popupMenu_Edge.setVisible(false );
				refresh();
				
			}
				
		});
		
		
		JMenuItem Color_Edge = new JMenuItem("Color");
		popupMenu_Edge.add(Color_Edge);
		Color_Edge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open color chooser 
				popupMenu_Edge.setVisible(false);
				ColorChooser tempFrame = new ColorChooser(); 
				ColorChooser.fromInside = true ; 
	
			}
		});
		
		mntmLabel = new JMenuItem("Label");
		mntmLabel.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\text.png"));
		popupMenu.add(mntmLabel);
		
		mntmMove = new JMenuItem("Move");
		mntmMove.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\move.png"));
		popupMenu.add(mntmMove);
		
		mntmDelet = new JMenuItem("Delete");
		mntmDelet.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\delete.png"));
		popupMenu.add(mntmDelet);
		
		mntmCopy_1 = new JMenuItem("Copy");
		mntmCopy_1.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\copy.png"));
		popupMenu.add(mntmCopy_1);
		
		mntmPaste = new JMenuItem("Paste");
		mntmPaste.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\paste.png"));
		popupMenu.add(mntmPaste);
		
		mntmCut = new JMenuItem("Cut");
		mntmCut.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\cut.png"));
		popupMenu.add(mntmCut);
		
		SourceVertex =  new JMenuItem("Source");
		popupMenu.add(SourceVertex);
		SourceVertex.setVisible(false);
		SourceVertex.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\pin.png"));
		
		DestinationVertex =  new JMenuItem("Destination");
		popupMenu.add(DestinationVertex);
		DestinationVertex.setVisible(false);
		DestinationVertex.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\pin.png"));

		
		fontColor =  new JMenuItem("Font Color");
		fontColor.setIcon(new ImageIcon("D:\\MY STUDING\\Work space of Java\\mmmmmmmmmmmmmmmmmmmm\\color-wheel-icon.png"));
		popupMenu.add(fontColor);
		
		mntmLabel.setEnabled(false);
		mntmMove.setEnabled(false);
		mntmDelet.setEnabled(false);
		mntmCopy_1.setEnabled(false);
		mntmPaste.setEnabled(false);
		mntmCut.setEnabled(false);
		fontColor.setEnabled(false);
		
		fontColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ColorChooser.fontColor = true; 
				ColorChooser tempFrame = new ColorChooser(); 
				
				popupMenu.setVisible(false);
			}
		});
		
		DestinationVertex.addActionListener(new ActionListener() {
			// set selected vertex as destination vertex 
			public void actionPerformed(ActionEvent e) {
				Shape temp = null;
				for (int i=0 ; i<list.size() ; i++ ){
					temp = list.get(i);
					// get clicked vertex to record as destination 
					if(	popupX < temp.x + temp.width && popupX > temp.x && 
						popupY > temp.y && popupY <temp.y + temp.width ){
						comboBoxVerticesEnd.setSelectedIndex(i);
						break; 
					}
					
				}
				popupMenu.setVisible(false);
			}
		});
		
		SourceVertex.addActionListener(new ActionListener() {	
			// set selected vertex as source 
			public void actionPerformed(ActionEvent arg0) {
				Shape temp = null;
				for (int i=0 ; i<list.size() ; i++ ){
					temp = list.get(i);
					// get clicked vertex to record as source 
					if(	popupX < temp.x + temp.width && popupX > temp.x && 
						popupY > temp.y && popupY <temp.y + temp.width ){
						ComboVerteciesStart.setSelectedIndex(i);
						break; 
					}
					
				}
				popupMenu.setVisible(false);
				
			}
		});
		
		mntmCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);

				boolean draw = false ;
				Shape temp = null;
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					temp = list.get(i);
					// get clicked vertex then clear it 
					if(	popupX < temp.x + temp.width && popupX > temp.x && 
						popupY > temp.y && popupY <temp.y + temp.width ){
						temp.clear(DrowPanel.getGraphics());
						list.remove(i);
						copiedVertex = temp; 
						removeExrtaEdges(temp);
					}
				}
				
				// remove the popup menu 
				popupMenu.setVisible(false );
				refresh();
				
			}
		});
		
		mntmLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);

				boolean draw = false ;
				Shape temp = null;
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					temp = list.get(i);
					// get clicked vertex then clear it 
					if(	popupX < temp.x + temp.width && popupX > temp.x && 
						popupY > temp.y && popupY <temp.y + temp.width ){
						draw = true; 
					}
				} 
				popupMenu.setVisible(false);
				//change the name 
				Edit(draw,temp);

				
			}
		});
		
		mntmCopy_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);

				boolean draw = false ;
				Shape temp = null;
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					temp = list.get(i);
					// get clicked vertex then clear it 
					if(	popupX < temp.x + temp.width && popupX > temp.x && 
						popupY > temp.y && popupY <temp.y + temp.width ){
						try {
							copiedVertex = temp.clone();
						} catch (CloneNotSupportedException e1) {
							lblMessage.setText("Error!");	
						} 
						copiedVertex.VertexName = temp.VertexName + ".";
						mntmPaste.setEnabled(true);
					}
				}
				// remove the popup menu 
				popupMenu.setVisible(false );
					
			}
		});
		
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);

				// if there is a vertex copied .. paste it 
				if (copiedVertex != null ){
					copiedVertex.x = popupX;
					copiedVertex.y = popupY;
					copiedVertex.paintComponent(DrowPanel.getGraphics());
					list.add(copiedVertex); 
					copiedVertex = null ; 
					mntmPaste.setEnabled(false);
				}
				popupMenu.setVisible(false);
				refresh();
			}
		});

		DrowPanel.addMouseListener(this);
		mntmDelet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Delete vertex 
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);

				boolean draw = false ;
				Shape temp = null;
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					temp = list.get(i);
					// get clicked vertex then clear it 
					if(	popupX < temp.x + temp.width && popupX > temp.x && 
						popupY > temp.y && popupY <temp.y + temp.width ){
						temp.clear(DrowPanel.getGraphics());
						list.remove(i);
						removeExrtaEdges(temp);
//						counter -- ; 
					}
				}
				// remove the popup menu 
				popupMenu.setVisible(false );
				refresh();
				
			}

			
		});

		mntmMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);

				canContinue = true ; 
				boolean draw = false ;
				
				//decide the selected vertex 
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					move = list.get(i);
					// get the vertex to move it 
					if (popupX < move.x + move.width && popupX > move.x && 
							popupY > move.y && popupY <move.y + move.width ){
						draw = true ;
					}
				}
//------------------------------------------------------

				//coping it 
				if (canContinue){
					lastClickFromMove = true ; 
					canContinue = false ; 
					lblMessage.setText("Click new Location ");
				}
				
				popupMenu.setVisible(false );
				
				// clear changeDisLocation arraylist and changeSourceLocation
				for (int i=0 ; i<changeDisLocation.size() ; i++)
					changeDisLocation.remove(0);
				for (int i =0 ; i<changeSourceLocation.size() ; i++)
					changeSourceLocation.remove(0);
				
				// check for edges that move is its source 
				for (int i=0 ; i<Edgelist.size() ; i++)
					if (Edgelist.get(i).source == move )
						changeSourceLocation.add(Edgelist.get(i));
				
				// check for edges that move is its destination 
				for (int i=0 ; i<Edgelist.size() ; i++)
					if (Edgelist.get(i).distination == move)
						changeDisLocation.add(Edgelist.get(i));	
			}
		});
		
		
		btnShortestPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i=0 ; i<Edgelist.size();i++)
					Edgelist.get(i).color = ColorChooser.selected;
				lblPath.setVisible(true);
				
				// build graph 
				Graph g = new Graph((DrawEdge[])Edgelist.toArray(new DrawEdge[Edgelist.size()]), (Shape[]) list.toArray(new Shape[list.size()]),list.size()); 
				g.buildGraph();
				
				if (rdbtnmntmUndirectedunweighted.isSelected() == true){
					// find shortest path foe undirected unweighed 
					g.findShortestPath_unwiegthed(list.get(ComboVerteciesStart.getSelectedIndex()));
					g.printShortestPath_unweighted(list.get(ComboVerteciesStart.getSelectedIndex()), list.get(comboBoxVerticesEnd.getSelectedIndex()));
				}
				// find shortest path foe directed weighed 
				else if (rdbtnmntmDirectedweigthedGraph.isSelected() == true )// Dijekstra 
					g.findShortestPath_Dijkstra(list.get(ComboVerteciesStart.getSelectedIndex()) , list.get(comboBoxVerticesEnd.getSelectedIndex()) );
				//find shortest path for undirected weighed 
				else if (rdbtnmntmUndirectedWiegthedGraph.isSelected()){
					g.findShortestPath_Dijkstra(list.get(ComboVerteciesStart.getSelectedIndex()) , list.get(comboBoxVerticesEnd.getSelectedIndex()) );
				}
				else {
					//find shortest path foe directed unweighed 
					g.findShortestPath_unwiegthed(list.get(ComboVerteciesStart.getSelectedIndex()));
					g.printShortestPath_unweighted(list.get(ComboVerteciesStart.getSelectedIndex()), list.get(comboBoxVerticesEnd.getSelectedIndex()));
				
				}
				// print path 
				if (!g.Path.equals(""))
					g.Path += list.get(comboBoxVerticesEnd.getSelectedIndex()).VertexName;
				else
					g.Path = "No Path"; 
				lblPathResult.setText(g.Path);
				
				// color the path on the graph 
				String[] splitPath = g.Path.split(" --> ");
				double cost = 0 ; 
				for (int j =0 ; j<splitPath.length-1 ; j++)
					for (int i=0 ; i<Edgelist.size();i++)
						if (Edgelist.get(i).source.VertexName.equals(splitPath[j]) && Edgelist.get(i).distination.VertexName.equals(splitPath[j+1])
								||
							Edgelist.get(i).distination.VertexName.equals(splitPath[j]) && Edgelist.get(i).source.VertexName.equals(splitPath[j+1])	){
							Edgelist.get(i).color = Color.BLUE;
							cost += Edgelist.get(i).cost;
							DrawEdge.drawArrow(DrowPanel.getGraphics(), Edgelist.get(i).xSource, Edgelist.get(i).ySource, Edgelist.get(i).xDis, Edgelist.get(i).yDis, Color.BLUE);
						}
				
				if (rdbtnmntmDirectedweigthedGraph.isSelected() ){
					lblCost.setVisible(true);
					lbCostResult.setText(cost+"");
					lbCostResult.setForeground(Color.RED);
				}
				else if ( rdbtnmntmUndirectedWiegthedGraph.isSelected()){
					lblCost.setVisible(true);
					lbCostResult.setText(cost/2+"");
					lbCostResult.setForeground(Color.RED);
				}
					
				refresh();
				// draw arrows after refreshing 
				for (int j =0 ; j<splitPath.length-1 ; j++)
					for (int i=0 ; i<Edgelist.size();i++)
						if (Edgelist.get(i).source.VertexName.equals(splitPath[j]) && Edgelist.get(i).distination.VertexName.equals(splitPath[j+1]))
							DrawEdge.drawArrow(DrowPanel.getGraphics(), Edgelist.get(i).xSource, Edgelist.get(i).ySource, Edgelist.get(i).xDis, Edgelist.get(i).yDis,Color.BLUE);
						
			}
			
			
		});
	}
	
	
	

	private void addPopupEdge(Component component, final JPopupMenu popup) {
		// pop up menu for edges 
		
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			
			private void showMenu(MouseEvent e) {
				boolean draw = false ;
				DrawEdge temp = null;
				for (int i=0 ; i<Edgelist.size() && !draw  ; i++ ){
					temp = Edgelist.get(i);
					//decide which vertex clicked on 
					if (e.getX() <= temp.xSource + (temp.xDis - temp.xSource) && e.getX() >= temp.xSource && 
						e.getY() <= (temp.yDis + temp.ySource)/2  + 10 &&  e.getY() >= (temp.yDis + temp.ySource)/2 - 10 )
						draw = true ;
					// same vertex but in the other direction 
					if (e.getX() <= temp.xDis + (temp.xSource - temp.xDis) && e.getX() >= temp.xDis && 
						e.getY() <= (temp.yDis + temp.ySource)/2  + 10 &&  e.getY() >= (temp.yDis + temp.ySource)/2 - 10 )
						draw = true ;
					
				}
				
				if (temp != null && (rdbtnmntmUndirectedunweighted.isSelected() || rdbtnmntmDirectedUnweightedGraph.isSelected()) )
					cost_Edge.setEnabled(false);
				else 
					cost_Edge.setEnabled(true);
				if (draw && SwingUtilities.isRightMouseButton(e)){
					
					//add all of them to action listener .. then reset the location ^_^ 
					popupMenu.setVisible(false);
					popupMenu_Edge.setVisible(true);
					popupMenu_Edge.setLocation(DrowPanel.getX() + frame.getX() + e.getX()+10  , DrowPanel.getY() + frame.getY() + e.getY() + 10 );
					popupX = e.getX(); 
					popupY = e.getY();
					refresh();
				}
			}
		});
	}
	
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popupVertex(e, popup);
			}
		});
	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == mntmExit){
			// exit the system 
			if (list.size() > 0 ){
				int result = JOptionPane.showConfirmDialog(null, "Do You want to save changes to Untitled?", "Graph", JOptionPane.YES_NO_CANCEL_OPTION);
				switch (result ){
				case 0:  // yes 
					save();
				case 1:{ // no
					System.exit(0);
					break; 
					}
				case 2: { // cancel 
					refresh();
					break; 
					}
				}
			}
			else 
				System.exit(0);
			
		}
		else if (e.getSource() == tglbtnVetex ){
			for (int i =0 ; i<Edgelist.size() ; i++)
				Edgelist.get(i).color = ColorChooser.selected; 
			popupMenu_Edge.setVisible(false);
			popupMenu.setVisible(false);
			tglbtnLink.setSelected(false );
			tglbtnLinkDirected.setSelected(false);
			tglbtnText.setSelected(false);
			rdbtnFindShortestPath.setSelected(false);
			FindPathPanel.setVisible(false);
			DestinationVertex.setVisible(false);
			SourceVertex.setVisible(false);
			refresh();

		}
		else if (e.getSource() == tglbtnLink){
			for (int i =0 ; i<Edgelist.size() ; i++)
				Edgelist.get(i).color = ColorChooser.selected; 
			if (list.size() < 2 )
				lblMessage.setText("Draw at least 2 vertices ");
			else
				lblMessage.setText("Click Source ");
			popupMenu_Edge.setVisible(false);
			popupMenu.setVisible(false);
			tglbtnVetex.setSelected(false );
			tglbtnLinkDirected.setSelected(false);
			tglbtnText.setSelected(false);
			rdbtnFindShortestPath.setSelected(false);
			btnShortestPath.setEnabled(false);
			FindPathPanel.setVisible(false);
			DestinationVertex.setVisible(false);
			SourceVertex.setVisible(false);
			refresh();
			
		}
		else if (e.getSource() == tglbtnLinkDirected){
			for (int i =0 ; i<Edgelist.size() ; i++)
				Edgelist.get(i).color = ColorChooser.selected; 
			if (list.size() < 2 )
				lblMessage.setText("Draw at least 2 vertices ");
			else
				lblMessage.setText("Click Source ");
			popupMenu_Edge.setVisible(false);
			popupMenu.setVisible(false);
			tglbtnLink.setSelected(false );
			tglbtnVetex.setSelected(false);
			tglbtnText.setSelected(false);
			rdbtnFindShortestPath.setSelected(false);
			FindPathPanel.setVisible(false);
			DestinationVertex.setVisible(false);
			SourceVertex.setVisible(false);
			refresh();
		}
		else if (e.getSource() == tglbtnText){
			for (int i =0 ; i<Edgelist.size() ; i++)
				Edgelist.get(i).color = ColorChooser.selected; 
			popupMenu_Edge.setVisible(false);
			popupMenu.setVisible(false);
			tglbtnLink.setSelected(false );
			tglbtnLinkDirected.setSelected(false);
			tglbtnVetex.setSelected(false);
			rdbtnFindShortestPath.setSelected(false);
			FindPathPanel.setVisible(false);
			DestinationVertex.setVisible(false);
			SourceVertex.setVisible(false);
			
			if (list.size() >= 1 )
				lblMessage.setText("Click on vertex to change its name ");
			else 
				lblMessage.setText("Draw some vertices First ");
			refresh();
			
		}
		else if (e.getSource() == rdbtnFindShortestPath ){
			SourceVertex.setVisible(true);
			DestinationVertex.setVisible(true);
			
			// remove any strange colour from previous shortest Path
			for (int i=0 ; i<Edgelist.size();i++)
				Edgelist.get(i).color = ColorChooser.selected;
			refresh();
			
			btnShortestPath.setEnabled(true);
			if (rdbtnFindShortestPath.isSelected() == true){
				if (list.size() < 2 || Edgelist.size()+1 < list.size()){
					lblMessage.setText("Can't find Shortest length untill there is a map with links ");
					rdbtnFindShortestPath.setSelected(false);
				}
				else{
					FindPathPanel.setVisible(true);
					comboBoxVerticesEnd.removeAllItems();
					ComboVerteciesStart.removeAllItems();
					lbCostResult.setText("");
					lblCost.setVisible(false);
					lblPathResult.setText("");
					// fill combo boxes 
					for (int i =0 ; i <list.size() ; i++){
						comboBoxVerticesEnd.addItem(list.get(i).VertexName); 
						ComboVerteciesStart.addItem(list.get(i).VertexName);
				}
				}
			}
			else 
				FindPathPanel.setVisible(false );
		}
		
	}

	int counter = 1 ;
	Shape move = null; 
	boolean canContinue = false ;  
	private JMenu mnUndirectedGraph;
	private JRadioButtonMenuItem rdbtnmntmUndirectedWiegthedGraph;
	private JMenu mnDirectedGraph;
	private JRadioButtonMenuItem rdbtnmntmDirectedUnweightedGraph;
	private JButton btnTopologicalSort;
	private JMenuItem mntmExportAsImage;
	
	@Override
	public void mouseReleased(MouseEvent e) {
		lblMessage.setText(" ");		
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {	}
	public void mouseEntered(MouseEvent e) {}
	 
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 ){
			// change label of selected vertex 
			if (!textChanged){
				textChanged = !textChanged;
				rdbtnFindShortestPath.setSelected(false);
				FindPathPanel.setVisible(false);
				DestinationVertex.setVisible(false);
				SourceVertex.setVisible(false);
				popupMenu_Edge.setVisible(false);
				popupMenu.setVisible(false);
				boolean draw = false ;
				Shape temp = null;
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					// decide which vertex 
					temp = list.get(i);
					if (e.getX() < temp.x + temp.width && e.getX() > temp.x && 
						e.getY() > temp.y && e.getY() <temp.y + temp.width )
							draw = true ;
					
				} 
				//change the name 
				Edit(draw, temp);
			}
			
			else 
				textChanged = !textChanged; 
		}
		// move vertex 
		if (lastClickFromMove == true ){
			// new Location 
			move.x = e.getX() - move.height/2; // on the center  
			move.y = e.getY() - move.height/2; 
			move.paintComponent(DrowPanel.getGraphics());
			lastClickFromMove = false ;
			
			// change location to new location 
			for (int i=0 ;i < changeDisLocation.size() ; i++){
				if (changeDisLocation.get(i).distination == move){
					changeDisLocation.get(i).xDis = e.getX();
					changeDisLocation.get(i).yDis = e.getY();
				}
			}
			for (int i=0 ;i < changeSourceLocation.size() ; i++){
				if (changeSourceLocation.get(i).source == move){
					changeSourceLocation.get(i).xSource = e.getX();
					changeSourceLocation.get(i).ySource = e.getY();
				}
			}
			refresh();
			move = null ; 
				
		}
		
		// draw vertex 
		else if (tglbtnVetex.isSelected() && SwingUtilities.isLeftMouseButton(e)){
			if (!drawVertex)
					DrowVertex(e);
			else
				drawVertex = !drawVertex;
		}
		
		// Draw edge
		else if (tglbtnLink.isSelected())
			DrawEdge_Undirected(e);
		
		// draw edge with cost 
		else if (tglbtnLinkDirected.isSelected()){
			DrawEdge_Dijekstra(e);
		}
		
		// edit label 
		else if (tglbtnText.isSelected()){
			if (!textChanged){
				tglbtnText.setSelected(false);
				textChanged = !textChanged;
				boolean draw = false ;
				Shape temp = null;
				for (int i=0 ; i<list.size() && !draw  ; i++ ){
					// decide which vertex 
					temp = list.get(i);
					if (e.getX() < temp.x + temp.width && e.getX() > temp.x && 
						e.getY() > temp.y && e.getY() <temp.y + temp.width )
							draw = true ;
					
				} 
				//change the name 
				Edit(draw, temp);
			}
			
			else 
				textChanged = !textChanged; 
		}
		
	}// end of methode 
	

	public static void refresh (){
		// repaint the graph from the zero 
		
		// clear slide 
		Shape.newSlide(DrowPanel.getGraphics(), DrowPanel.getWidth(), DrowPanel.getHeight());

		// draw background
		if (ImagePath != null ){
		Image img =  Toolkit.getDefaultToolkit().getImage(ImagePath);
		DrowPanel.getGraphics().drawImage(img,(int)( DrowPanel.getAlignmentX()), (int)( DrowPanel.getAlignmentY()),
				DrowPanel.getWidth(), DrowPanel.getHeight(),null);
		}
		
		// draw vertices and edges 
		for (int i =0 ; i<list.size() ; i++)
			list.get(i).paintComponent(DrowPanel.getGraphics());
		for (int i =0 ; i<Edgelist.size() ; i++)
			Edgelist.get(i).paintComponent(DrowPanel.getGraphics());

	}
	
	public void saveAsPNG() {
		// create Buffered Image 
	    BufferedImage bImg = new BufferedImage(DrowPanel.getWidth(), DrowPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D cg = bImg.createGraphics();
		
	    // Draw your Graph on the new 2dGraph
	    if (ImagePath != null ){
	    	// Draw image if exit 
			Image img =  Toolkit.getDefaultToolkit().getImage(ImagePath);
			cg.drawImage(img,(int)( DrowPanel.getAlignmentX()), (int)( DrowPanel.getAlignmentY()),
					DrowPanel.getWidth(), DrowPanel.getHeight(),null);
		
	    }else {
	    	// otherwise paint white background
			cg.setColor(Color.white);
			cg.fillRect(0, 0,DrowPanel.getWidth(), DrowPanel.getHeight());
		}
	    
	    // paint all components
		for (int i =0 ; i<list.size() ; i++)
			list.get(i).paintComponent(cg);
		for (int i =0 ; i<Edgelist.size() ; i++)
			Edgelist.get(i).paintComponent(cg);
	    	    
	    try {
	    	//open file chooser to save 
	    	JFileChooser inputFile = new JFileChooser(); 
			int x = inputFile.showSaveDialog(frame);
			String FileName = ""; 
			if (x == JFileChooser.APPROVE_OPTION){
				File  selectedFile = inputFile.getSelectedFile();
				FileName = selectedFile.getAbsolutePath().replace("\\", "\\\\");
				if (!ImageIO.write(bImg, "PNG", new File(FileName + ".png")))
		            JOptionPane.showMessageDialog(null, "Something went wrong \n Try to save the Graph later.","Failed",JOptionPane.ERROR_MESSAGE);
			}
           
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
	}

	public void newWindow(){
		Shape.newSlide(DrowPanel.getGraphics(), DrowPanel.getWidth(), DrowPanel.getHeight());
		int size = list.size() ; 
		for (int i=0 ;i<size ; i++)
			list.remove(0);
		size = Edgelist.size(); 
		for (int i=0 ;i<size ; i++)
			Edgelist.remove(0);
		counter = 1 ; 
	}

	// save current graph 
	public void saveCurrentWork (){
		if (list.size() > 0 ){
			int result = JOptionPane.showConfirmDialog(null, "Do You want to save changes to Untitled?", "Graph", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (result ){
			case 0:  // yes 
				save();
				
			case 1:{ // no
				newWindow();
				break; 
				}
			case 2: { // cancel 
				refresh();
				break; 
				}
			}
		}
		else {
			newWindow();
		}
		
	}
	
	public void save (){
		// save graph 
		try {
			// open file choosr 
			JFileChooser inputFile = new JFileChooser(); 
			inputFile.setSelectedFile(new File("Untitled"));
			int x = inputFile.showSaveDialog(frame);
			if (x == JFileChooser.APPROVE_OPTION){
				File  selectedFile = inputFile.getSelectedFile();
				String File = selectedFile.getAbsolutePath().replace("\\", "\\\\");
				FileOutputStream fout = new FileOutputStream(File+".txt");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				// save data needed 
				/*
				 * format : 
				 * number of vertices then vertices 
				 * number of edges then edges 
				 * int counter value
				 * int type of graph from 1-5 as follow 
				 * 1 --> undirected unweighted graph 
				 * 2 --> undirected weighed graph 
				 * 3 --> directed weighed graph (dijkstra)
				 * 4 --> directed unweighed graph 
				 * 5 --> minspanning tree 
				 */
				oos.writeInt(list.size());
				Shape[] temparrayVertex = list.toArray(new Shape[list.size()]);
				for (int i =0 ; i < list.size() ; i++ ){
					temparrayVertex[i].icon = null; 
					oos.writeObject(temparrayVertex[i]);
				}
				oos.writeInt(Edgelist.size());
				DrawEdge[] temparray = Edgelist.toArray(new DrawEdge[Edgelist.size()]);
				for (int i =0 ; i < Edgelist.size() ; i++ ){
					oos.writeObject(temparray[i]);
				}
				oos.writeInt(counter);
				oos.writeInt(valValue());
				oos.writeObject(ImagePath);
				oos.close();
			}
			
		} catch (IOException e) {
			lblMessage.setText("Error!");
			e.printStackTrace();
		}
	}
	
	private int valValue() {
				/* 1 --> undirected unweighted graph 
				 * 2 --> undirected weighed graph 
				 * 3 --> directed weighed graph (dijkstra)
				 * 4 --> directed unweighed graph 
				 * 5 --> minspanning tree 
				 */
		if (rdbtnmntmDirectedUnweightedGraph.isSelected())
			return 4; 
		else if (rdbtnmntmDirectedweigthedGraph.isSelected())
			return 3; 
		else if (rdbtnmntmUndirectedunweighted.isSelected())
			return 1; 
		else if (rdbtnmntmUndirectedWiegthedGraph.isSelected())
			return 2;
		else
			return 5;
	}

	private void removeExrtaEdges(Shape temp ) {
		// remove all edges connected the the deleted vertex 
		for (int i=0 ; i<Edgelist.size() ; ){
			if (Edgelist.get(i).source == temp )
				Edgelist.remove(i);
			else
				i ++ ; 
		}
	
		// check for edges that temp is its destination 
		for (int i=0 ; i<Edgelist.size(); ){
			if (Edgelist.get(i).distination == temp)
				Edgelist.remove(i);
			else
				i ++ ; 
			}
		
		refresh();
	}
	
	private void removeExrtaEdges(DrawEdge temp ) {
		 
		for (int i=0 ; i<Edgelist.size() ; ){
			if (Edgelist.get(i) == temp )
				Edgelist.remove(i);
			else
				i ++ ; 
		}
	
		// check for edges that temp is its destination 
		for (int i=0 ; i<Edgelist.size(); ){
			if (Edgelist.get(i).source == temp.distination && temp.source == Edgelist.get(i).distination  )
				Edgelist.remove(i);
			else
				i ++ ; 
			}
		
		refresh();
	}
	
	private void Edit(boolean draw, Shape temp){	
		//change the name 
		if (draw){
			String newName = JOptionPane.showInputDialog("Enter Name of the vertex"); 
			for (int i=0 ; i<list.size() ; i++ )
				if (newName != null && newName.equals(list.get(i).VertexName) && list.get(i) != temp ){
					newName += "."; 
					i = -1; 
				}
			// if the user chose cancel .. don't change the name of the vertex
			if (newName != null)
				temp.VertexName = newName; 
					
		}
		refresh();
		
	}
	
	private void changeType(){
		// change the type after drawing hte graph may make problems occur ! 
		// check if type can be changeable 
		if (Edgelist.size() >=1 ){
			int answer = JOptionPane.showConfirmDialog(null, "Changing type of graph will delete all edges\n "
					+ "Do you want to Continue?");
			switch(answer){
			case JOptionPane.YES_OPTION:{
				int size = Edgelist.size();
				// delete all edges ! 
				for (int i =0 ; i <size; i++)
					Edgelist.remove(0);
				break; 
			}
			// don't open Type menu 
			case JOptionPane.NO_OPTION: javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
			case JOptionPane.CANCEL_OPTION: javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
			break ; 
			}
		}
	}

	private void DrowVertex (MouseEvent e ){
		boolean draw = true ;
		Shape temp = null;
		
		for (int i=0 ; i<list.size() && draw  ; i++ ){
			temp = list.get(i);
			// draw on another vertex 
			if (Math.sqrt(Math.pow(e.getX() - (temp.x+temp.width/2),2) + Math.pow( e.getY() - (temp.y+temp.width/2),2)) < temp.width ){
					draw = false ;	
			}
			
		}
		if (!draw  ) 
			lblMessage.setText("Can't drow becouse of " + temp.VertexName);
		else {
			// available area 
			// create new vertex , set it coordination 
			Shape shape = new Shape() ; 
			shape.x = e.getX() - shape.width/2; // Centre the vertex 
			shape.y = e.getY() - shape.height/2 ; 	
			shape.VertexName = "v" + counter ; 
			if (list.size() ==0 )
				shape.number = 1 ; 
			else 
				shape.number = list.get(list.size()-1).number +1 ;
			list.add(shape);
			shape.paintComponent(DrowPanel.getGraphics());
			counter ++ ; 
			
		}
		refresh();
		drawVertex = !drawVertex;
	}

	private void DrawEdge_Undirected(MouseEvent e){
		if (list.size() < 2 )
			lblMessage.setText("Draw two Vetex at least first ");
			 
		else {
			edge.color = ColorChooser.selected; 
			boolean draw = false ;
			Shape temp = null;
			for (int i=0 ; i<list.size() && !draw  ; i++ ){
				temp = list.get(i);
				if ( 
					// decide which node is clicked 
					e.getX() < temp.x + temp.width && e.getX() > temp.x && 
					e.getY() > temp.y && e.getY() <temp.y + temp.width 
						){
					draw = true ;	
					if(source ) {
						// add info about source node 
						if (rdbtnmntmMinSpanning.isSelected()){
							newEdge = new DrawEdge(2); 
							edge = new DrawEdge(2);
							newEdge.weighted = true ; 
							edge.weighted = true ; 
						}
						else if (rdbtnmntmUndirectedunweighted.isSelected()) {
							newEdge = new DrawEdge(0); 
							edge = new DrawEdge(0); 
						}
						else {
							newEdge = new DrawEdge(0); 
							edge = new DrawEdge(0);
							newEdge.weighted = true ; 
							edge.weighted = true ; 
						}
						// add info to edge 
						edge.xSource = temp.x + temp.width/2;
						edge.ySource = temp.y + temp.width/2;
						edge.source = temp; 
						source = !source ; 
						lblMessage.setText("Click Distination");
						// via verses node
						newEdge.distination = temp;
						
					}else{
						if (temp.x + temp.width/2 == edge.xSource )// same source is the same as destination 
							lblMessage.setText("Choose diffrenet Distination");
						else{
							// add info about destination node 
							edge.xDis = temp.x + temp.width/2;
							edge.yDis = temp.y + temp.width/2;
							edge.distination = temp; 
							source = !source ; 
							lblMessage.setText(" ");
							tglbtnLink.setSelected(false);
							
							// via verses node
							newEdge.xSource = edge.xDis; 
							newEdge.xDis = edge.xSource; 
							newEdge.yDis = edge.ySource;
							newEdge.ySource = edge.yDis;
							newEdge.source = temp ; 
							
						}
					}
						
				}
					
			}
			if (!draw) 
				lblMessage.setText("Click on a vetex  " );
			if (source && draw ){
				boolean add = checkIfEdgeExist(); 
				if (rdbtnmntmMinSpanning.isSelected() || rdbtnmntmUndirectedWiegthedGraph.isSelected())
					EditCost(edge);
				// add new edge to edge list 
				if (!add){
					newEdge.cost = edge.cost;
					Edgelist.add(edge);
					Edgelist.add(newEdge);
					edge.paintComponent(DrowPanel.getGraphics());
				}
				else 
					lblMessage.setText("Edge Already Exist");
				// add nodes to list of edges 
				if (rdbtnmntmMinSpanning.isSelected()){
					edge = new  DrawEdge(2);
					newEdge = new  DrawEdge(2);
				}
				else  {
					edge = new  DrawEdge(0);
					newEdge = new  DrawEdge(0);
				}
			}
		}
	}

	private void DrawEdge_Dijekstra(MouseEvent e) {
		if (list.size() < 2 )
			lblMessage.setText("Draw two Vetex at least first ");
		else {
			edge.color = ColorChooser.selected; 
			boolean draw = false ;
			Shape temp = null;
			for (int i=0 ; i<list.size() && !draw  ; i++ ){
				temp = list.get(i);
				if ( // decide which node is clicked 
					e.getX() < temp.x + temp.width && e.getX() > temp.x && 
					e.getY() > temp.y && e.getY() <temp.y + temp.width 
						){
					draw = true ;	
					if(source ) {
						// add info about source node 
						edge.xSource = temp.x + temp.width/2;
						edge.ySource = temp.y + temp.width/2;
						edge.source = temp; 
						edge.type = 1 ; 
						source = !source ; 
						lblMessage.setText("Click Distination");

					}else{
						if (temp.x + temp.width/2 == edge.xSource )// same source is the same as destination 
							lblMessage.setText("Choose diffrenet Distination");
						else{
							// add information about destination node 
							edge.xDis = temp.x + temp.width/2;
							edge.yDis = temp.y + temp.width/2;
							edge.distination = temp; 
							source = !source ; 
							lblMessage.setText(" ");
							tglbtnLinkDirected.setSelected(false);
							
						}
					}
						
				}
					
			}
			if (!draw) 
				lblMessage.setText("Click on a vetex  " );
			if (source  && draw){
				boolean add = checkIfEdgeExist(); 
				// add new edge to edge list 
				if (!add){
					
					if (rdbtnmntmDirectedweigthedGraph.isSelected()){
						EditCost(edge);
						edge.weighted = true ; 
					}
					else 
						edge.weighted = false ; 
					Edgelist.add(edge);
					edge.paintComponent(DrowPanel.getGraphics());
				}
				else 
					lblMessage.setText("Edge Already Exist");
				edge = new  DrawEdge(1);
			
			}
		}
		
		refresh();
	}

	private boolean checkIfEdgeExist() {
		for (int i=0 ;i<Edgelist.size() ; i++)
			if (edge.source == Edgelist.get(i).source && edge.distination == Edgelist.get(i).distination)
				return true ; 
		return false ;
	}

	@SuppressWarnings("null")
	private void EditCost(DrawEdge edge){
		boolean valid = true; 
		// its cost! 
		String cost = JOptionPane.showInputDialog("Enter cost");
		if (cost == null || cost.equals("."))
			cost = "0.0";
		// no special characters are entered 
		for (int i =0 ; i<cost.length() ; i++)
			if ( !Character.isDigit((cost.charAt(i))) && !(cost.charAt(i)+"").equals(".") ){
					lblMessage.setText("Invalid Cost!");
					edge.cost = 0.0; 
					valid = false ; 
			}
		if (valid )
			edge.cost = Double.parseDouble(cost);
		refresh();
	}
	
	protected void popupVertex(MouseEvent e, final JPopupMenu popup) {
	
		boolean draw = false ;
		Shape temp = null;
		for (int i=0 ; i<list.size() && !draw  ; i++ ){
			temp = list.get(i);
			//decide which vertex clicked on 
			if (e.getX() < temp.x + temp.width && e.getX() > temp.x && 
				e.getY() > temp.y && e.getY() <temp.y + temp.width ){
				draw = true ;
				// enable 
				mntmCopy_1.setEnabled(true);
				mntmDelet.setEnabled(true);
				mntmMove.setEnabled(true);
				mntmLabel.setEnabled(true);
				mntmCut.setEnabled(true);
				SourceVertex.setEnabled(true);
				DestinationVertex.setEnabled(true);
				fontColor.setEnabled(true);
				mntmPaste.setEnabled(false);
			}
		}
		
		if (!draw){
			mntmCopy_1.setEnabled(false);
			mntmDelet.setEnabled(false);
			mntmMove.setEnabled(false);
			mntmLabel.setEnabled(false);
			mntmCut.setEnabled(false);
			mntmPaste.setEnabled(false);
			fontColor.setEnabled(false);
			SourceVertex.setEnabled(false);
			DestinationVertex.setEnabled(false);
		}
		if (copiedVertex != null && !draw)
			mntmPaste.setEnabled(true);
		if (draw || SwingUtilities.isRightMouseButton(e)){
			//add all of them to action listener .. then reset the location ^_^ 
			popupMenu_Edge.setVisible(false);
			popup.setVisible(true);
			popup.setLocation(DrowPanel.getX() + frame.getX() + e.getX()+10  , DrowPanel.getY() + frame.getY() + e.getY() + 10 );
			popupX = e.getX(); 
			popupY = e.getY();
			refresh();
		}
		
	}
	
	void checkGraphType(int value) {
		/*
		 * 1 --> undirected unweighted graph 
		 * 2 --> undirected weighed graph 
		 * 3 --> directed weighed graph (dijkstra)
		 * 4 --> directed unweighed graph 
		 * 5 --> minspanning tree 
		 */
		switch (value){
		case 1: {
			rdbtnmntmUndirectedunweighted.setSelected(true);
			lblTypeOfGraph.setText("Undirected Unweighed Graph");
			break ; 
		}
		case 2: {
			rdbtnmntmUndirectedWiegthedGraph.setSelected(true);
			lblTypeOfGraph.setText("Undirected Weighed Graph");
			break ; 
		}
		case 3: {
			rdbtnmntmDirectedweigthedGraph.setSelected(true);
			lblTypeOfGraph.setText("Directed Weighed Graph");
			break; 
		}
		case 4: {
			rdbtnmntmDirectedUnweightedGraph.setSelected(true);
			lblTypeOfGraph.setText("Directed Unweighed Graph");
			break; 
		}
		case 5:{
			rdbtnmntmMinSpanning.setSelected(true);
			lblTypeOfGraph.setText("Undirected Weighed Graph (Minimum Spanning tree)");
		}
		}
		
	}
}
