import java.util.ArrayList;
//creates graph given vertices and edges
public class Graph {
	ArrayList<Vertex> verts;
	ArrayList<Edge> edges;	
	Graph(ArrayList<Vertex> v, ArrayList<Edge> e) {
		verts = v;
		edges = e;
		
		for (int i = 0; i < edges.size(); i++) {
			Edge current = edges.get(i);
		}
		
		
	}
}
