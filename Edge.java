import java.util.Queue;

public class Edge {
	Vertex source;
	Vertex destination;
	Boolean discovered;
	int distance;
	
	Edge(Vertex start, Vertex finish){
		source = start;
		destnation = finish;
	}
}
