import java.util.ArrayList;

public class Vertex{
	char element;
	int row;
	int col;	
	boolean explored;
	boolean invalid;
	Vertex parent;
	ArrayList<Vertex> parents;
	ArrayList<Edge> incidentEdges;
	 
	Edge left, right, up, down;
		Vertex(char j, int r, int c){
			
		element = j;
		row = r;
		col = c;
	}
	
}