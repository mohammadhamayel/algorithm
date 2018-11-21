package mmmmmmmmmmmmmmmmmmmm;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Graph class represent the graph that user draw on the screen 
 * Graph is array of linked list .. each index represent a vertex and the linked list is its adjacent 
 * table is the table that contain Known, distance and path which is created while finding shortest path 
 * Path : is the shortest path from vertex to another 
 * 
 */

public class Graph {
	LinkedList<Shape> graph[];
	DrawEdge edges[];
	Shape vertices[];
	int numOfVertices ; 
	int table[][];
	String Path = "" ; 
	ArrayList<DrawEdge> pathVerteices = new ArrayList<DrawEdge>();
	
	@SuppressWarnings("unchecked")
	public Graph(int numOfVertices) {
		this.numOfVertices = numOfVertices ; 
		graph = new LinkedList [numOfVertices]; 
		edges = new DrawEdge[numOfVertices];
		vertices = new Shape[numOfVertices];
		
		for (int i=0 ; i<graph.length ; i++)
			graph[i] = new LinkedList<Shape>();
		for (int i=0 ; i<vertices.length ; i++)
			vertices[i] = new Shape("v" + (i+1));
	}
	
	public Graph( DrawEdge edges[], Shape vertices[], int numOfVertices) {
		graph = new LinkedList [numOfVertices]; 
		for (int i=0 ; i<graph.length ; i++)
			graph[i] = new LinkedList<Shape>();
		this.numOfVertices = numOfVertices; 
		this.edges = edges ; 
		this.vertices = vertices ; 
	}
	
	
	 public String topologicalSort() {
		int table[][] = new int [numOfVertices][2];// two coulmns (known and index) 
		
		//read number of edges enter to each vertex 
		for (int j=0; j<vertices.length ; j++)
			for (int i=0 ; i<edges.length ; i++)
				if (edges[i].distination == vertices[j])
					table[j][0] ++;
		
		int counter = 0 ; // used for known vertices
		Shape v ; 
		for (counter = 1 ; counter <= numOfVertices ; counter ++){
			 int minIndex = findMinVertex( table );
			
			 if (minIndex == -1 ){
				 Path = "Error: Graph has a cycle, Can't be ordered." ;
				 break ;
			 }
			 
			 table[minIndex][0] = -1 ; 	 
			 v = vertices[minIndex];
			  
			 table[minIndex][1] = counter ; //set as known 
			 for (int i=0 ; i<graph[minIndex].size() ; i++)
				 table[graph[minIndex].get(i).number-1][0] --;
			 
		 }
		 if (!Path.equals("Error: Graph has a cycle, Can't be ordered."))
			 // print order of vertices 
			 for (int count = 1  ; count <= numOfVertices; count ++ )
				 for (int j=0 ; j<numOfVertices ; j++)
					 if (table[j][1] == count ){
						 Path += vertices[j].VertexName + " --> ";
						 break ; 
						 }
//		System.out.println(Path);
		
		return Path ;
		 
	}
	
	private int findMinVertex( int table[][]) {
		//return index of next zero value to continue topological sort 
		for (int i=0 ; i<vertices.length ; i++)
			if (table[i][0] == 0  ) // maybe min have to equal 0 
				return  i ; 
			
		//find vertex in graph 
		return -1;
					
	}
	
	public double min_spanning_tree(Shape startVertex){
		double table[][] = new double [numOfVertices][3];// three columns (known distance path) 
		//known = 0 = false , path = 0 = no path yet , dis = maxInt 
		
		//initiate the table 
		table[startVertex.number-1][0] =1 ; table[startVertex.number-1][1] = 0 ; table[startVertex.number-1][2] = 0;
		for (int i=0 ; i <table.length ; i++){
			if ( i != startVertex.number-1){
				table[i][0] = 0 ; 
				table[i][1] = Integer.MAX_VALUE ; 
				table[i][2] = 0 ; 
			}
		}
		
		// Initialise table for first instance 
		for (int i=0 ;i<graph[startVertex.number-1].size(); i++)
			for (int j =0 ; j<edges.length ; j++)
				if (edges[j].source == startVertex && edges[j].distination == graph[startVertex.number-1].get(i)){
					table[graph[startVertex.number-1].get(i).number-1][1] =  edges[j].cost; ; 
					table[graph[startVertex.number-1].get(i).number-1][2] = startVertex.number;
		}
			
		
		Shape v,w ; 
		double cost =0.0 ; 
		for (;;){
			// find min distance vertex unknown 
			int minIndex = findMinVertex_Dijkstra(table);
			
			if (minIndex == -1)
				break ; 
			table[minIndex][0] = 1 ; // set as known 
			v = vertices[minIndex];
			
			//for each w adjacent to v 
			for (int j =0 ; j< graph[minIndex].size(); j++){
				int adjsentIndex =  graph[minIndex].get(j).number -1 ; 
				w = graph[minIndex].get(j);
				for (int i =0 ; i<edges.length ; i++)
					if (edges[i].source == v && edges[i].distination == w)
						cost = edges[i].cost;
				// cost represents the cost between v, w
				if (table[adjsentIndex][0] == 0)
					if (table[adjsentIndex][1] > cost ){
						table[adjsentIndex][1] = cost ;
						table[adjsentIndex][2] = v.number ; 
					}
			}
		}
//		System.out.println("k\td\tp");
//		for (int i =0 ; i<table.length ; i++)
//			System.out.println(table[i][0] + "\t" + table[i][1] + "\t" + (int)(table[i][2]) );
		Path_min_spanning_tree(table);
		return find_min_cost(table);
		
	}
	
	private void Path_min_spanning_tree (double table[][]){
		//print result 
		for (int i =0 ; i <table.length ; i++)
			Path += vertices[i].number + "," + (int )(table[i][2]) + "\n";
	}
	
	private double find_min_cost(double table[][]){
		// fins min cost 
		int sum = 0 ; 
		for (int i =0 ; i<table.length ; i++)
			sum += table[i][1]; 
		return sum ; 
	}
	
	
	//find shortest path between two vertices  
	public void findShortestPath_unwiegthed(Shape startVertex) {
		table= new int [numOfVertices][3];// three columns (known distance path) 
		//known = 0 = false , path = 0 = no path yet , dis = maxInt 
		
		//initiate the table 
		table[startVertex.number-1][0] =0 ; table[startVertex.number-1][1] = 0 ; table[startVertex.number-1][2] = 0;
		for (int i=0 ; i <table.length ; i++){
			if ( i != startVertex.number-1){
				table[i][0] = 0 ; 
				table[i][1] = Integer.MAX_VALUE ; 
				table[i][2] = 0 ; 
			}
		}
		
		int current_distance; 
		Shape v ; 
		for (current_distance =0 ; current_distance < numOfVertices ; current_distance ++)
			// for each vertex v 
			for (int i=0 ; i < vertices.length ; i++){
				v = vertices[i]; 
				int indexOfVetex = v.number-1 ; 
				if ( table[ indexOfVetex][0] == 0
				&&	 table[ indexOfVetex ][1] == current_distance )
				{
					table[ indexOfVetex ][0] =1 ; // set as known 
					//for each w adjacent to v 
					for (int j=0 ; j<graph[ indexOfVetex ].size() ; j++)
						// use number from shape 
						if (table[graph[ indexOfVetex ].get(j).number-1][1] == Integer.MAX_VALUE){
							table[graph[ indexOfVetex ].get(j).number-1][1] = current_distance + 1; 
							table[graph[ indexOfVetex ].get(j).number-1][2] = v.number ; 
						}
				}
				
			}
	}

	//print the shortest path 
	public void printShortestPath_unweighted( Shape start, Shape end) {
	
		if (table[end.number-1][2] == 0 )
			return; // return bc the dis is already added in the last call for the function 
		else {
			printShortestPath_unweighted(start, vertices[table[end.number-1][2] -1] );
			Path += vertices[table[end.number-1][2] -1].VertexName + " --> "; // add it to the path 
		}
	}
	
	public void printShortestPath_dijekstra( double table[][], Shape start, Shape end) {
		int index = end.number-1; 
		if (table[index][2] == 0 )
			return; // return bc the dis is already added in the last call for the function 
		else {
			
			printShortestPath_dijekstra(table,start, vertices[(int) (table[index][2])-1] );
			Path += vertices[(int) (table[index][2])-1].VertexName + " --> "; // add it to the path 
//			System.out.println(Path);
		}
	}

	//build the graph using linked list 
	public void buildGraph() {

		for (int i=0 ; i<edges.length ; i++){
			int vertexIndex = edges[i].source.number;
			int graphIndex =0  ;
			for (int j=0; j<vertices.length ; j++)
				if (vertices[j].number == vertexIndex)
					graphIndex = j;
			// add it 
			graph[graphIndex ].add(edges[i].distination); 
		}
			
		
//		for (int i=0 ; i<graph.length ; i++){
//			System.out.print(vertices[i].VertexName + " --> ");
//			for (int j=0 ; j<graph[i].size() ; j++)
//				System.out.print(graph[i].get(j).VertexName + " --> ");
//			System.out.println("null");
//		}
					
	}

	public void findShortestPath_Dijkstra(Shape startVertex, Shape DestinationVertex) {
		double table[][] = new double [numOfVertices][3];// three columns (known distance path) 
		//known = 0 = false , path = 0 = no path yet , dis = maxInt 
		
		//initiate the table 
		table[startVertex.number-1][0] =1 ; table[startVertex.number-1][1] = 0 ; table[startVertex.number-1][2] = 0;
		for (int i=0 ; i <table.length ; i++){
			if ( i != startVertex.number-1){
				table[i][0] = 0 ; 
				table[i][1] = Integer.MAX_VALUE ; 
				table[i][2] = 0 ; 
			}
		}
		// initiate first instance 
		for (int i=0 ;i<graph[startVertex.number-1].size(); i++)
			for (int j =0 ; j<edges.length ; j++)
				if (edges[j].source == startVertex && edges[j].distination == graph[startVertex.number-1].get(i)){
					table[graph[startVertex.number-1].get(i).number-1][1] =  edges[j].cost; ; 
					table[graph[startVertex.number-1].get(i).number-1][2] = startVertex.number;
		}
			
		Shape v,w ; 
		double cost =0.0 ; 
		for (;;){
			int minIndex = findMinVertex_Dijkstra(table );
			
			if (minIndex == -1)
				break ; 
			table[minIndex][0] = 1 ; // set as known 
			v = vertices[minIndex];
			//for each w adjacent to v 
			for (int j =0 ; j< graph[minIndex].size(); j++){
				int adjsentIndex =  graph[minIndex].get(j).number -1  ; 
				w = graph[minIndex].get(j);
				for (int i =0 ; i<edges.length ; i++)
					if (edges[i].source == v && edges[i].distination == w)
						cost = edges[i].cost;
				// cost is the cost between v,w 
				for (int k=0; k<vertices.length ; k++)
					if (vertices[k].number == adjsentIndex+1)
						adjsentIndex = k;
				if (table[adjsentIndex][0] == 0)// unknown 
					if (table[minIndex][1] + cost < table[adjsentIndex][1]){
						table[adjsentIndex][1] = table[minIndex][1] + cost ;
						table[adjsentIndex][2] = v.number ; 
					}
			}
			
		}
		// find path 
		printShortestPath_dijekstra(table,startVertex,DestinationVertex );


	}

	private int findMinVertex_Dijkstra(double table[][]) {
		// return min unknown vertex
		double min = Integer.MAX_VALUE ; 
		int minIndex = -1 ; 
		for (int i=0 ; i<vertices.length ; i++)
			if (table[i][1] < min  && table[i][0] == 0 ){ 
				min = table[i][1] ; 
				minIndex = i ; 
			}
		//find vertex in graph 
		return minIndex;
					
	}

	
} 
