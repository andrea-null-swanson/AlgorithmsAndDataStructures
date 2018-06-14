import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*

  Author: Andrea Swanson
  Email: aswanson2016@my.fit.edu	
  Course: CSE 2010
  Section: 14
  Description:

 */


public class HW6 {
	
	
	//prints graph
	static void printGraph(Vertex[][] array, int rows, int cols) {
		System.out.print(" ");
		for (int k = 0; k < cols; k++) {
			System.out.print(k);
		}
		System.out.println();
		for(int i = 0; i < rows; i++) {
			System.out.print(i);
			for(int j = 0; j < cols; j++) {
				System.out.print(array[i][j].element);
			}
			System.out.println();
		}
	}
	
	//updates the grid to restore the edges to any updated position of tron
	static void updateGrid(Vertex[][] array, int rows, int columns) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				Vertex current = array[row][col];
				if (row > 0 && row < rows - 1 && col > 0 && col < columns - 1) {
					char upChar =  array[row + 1][col].element;
					if (upChar == ' ') {

						array[row + 1][col].invalid = false;
					}else {
						array[row + 1][col].invalid = true;
					}
					
					
					char downChar =  array[row - 1][col].element;
					if (downChar == ' ') {
						array[row - 1][col].invalid = false;
					}else {
						array[row - 1][col].invalid = true;
					}
					
					
					char rightChar =  array[row][col + 1].element;
					if (rightChar == ' ') {
						array[row][col + 1].invalid = false;
					}else {
						array[row][col + 1].invalid = true;
					}
					
					
					char leftChar =  array[row][col - 1].element;
					if (leftChar == ' ') {
						array[row][col - 1].invalid = false;
					}else {
						array[row][col - 1].invalid = true;
					}
				

							
				}
				
			}
		}
		
		
		//updates edges for any new position of tron
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				Vertex current = array[row][col];
				if (row > 0 && row < rows - 1 && col > 0 && col < columns - 1) {
				Edge up = new Edge(array[row][col], array[row - 1][col]);
				Edge down = new Edge(array[row][col], array[row + 1][col]);
				Edge left = new Edge(array[row][col], array[row][col - 1]);
				Edge right = new Edge(array[row][col], array[row][col + 1]);
				up.discovered = false;
				down.discovered = false;
				left.discovered = false;
				right.discovered = false;
				array[row][col].row = row;
				array[row][col].col = col;
				array[row][col].up = up;
				array[row][col].down = down;
				array[row][col].right = right;
				array[row][col].left = left;
				array[row][col].incidentEdges = new ArrayList<Edge>();
				array[row][col].incidentEdges.add(up);
				array[row][col].incidentEdges.add(down);
				array[row][col].incidentEdges.add(left);
				array[row][col].incidentEdges.add(right);
				}
			}
		}
		
		

	}
	//algorithm to find the shortest path to tron
	//assigns parent to each visit so that when tron is found, the vertex is returned
	//and the path can be traced by the parent until the path is backtracked to the bug
	static Vertex BFS(Graph g, Vertex bug, Vertex Tron, Vertex[][]array){
		for (Vertex v : g.verts) {
			v.explored = false;
		}
		for (Edge e : g.edges) {
			e.discovered = false;
		}
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		array[bug.row][bug.col].explored = true;
		bug.explored = true;
		queue.addLast(array[bug.row][bug.col]);
		while (!queue.isEmpty()) {
			Vertex step = queue.removeFirst();
			
			for (int i = 0; i < step.incidentEdges.size(); i++) {
				//tron is the next destination
					if (array[step.row][step.col].incidentEdges.get(i).destination.element == 'T') {
						array[array[step.row][step.col].incidentEdges.get(i).destination.row][array[step.row][step.col].incidentEdges.get(i).destination.col].parent = 
								array[step.row][step.col].incidentEdges.get(i).source; 
						return array[step.row][step.col].incidentEdges.get(i).destination;
					}

				//a valid edge destination is added to the queue
				if (!array[step.row][step.col].incidentEdges.get(i).discovered &&
						!array[step.row][step.col].incidentEdges.get(i).destination.invalid &&
						!array[step.row][step.col].incidentEdges.get(i).destination.explored) {				
					array[step.row][step.col].incidentEdges.get(i).discovered = true;
					array[step.row][step.col].incidentEdges.get(i).destination.explored = true;
					array[array[step.row][step.col].incidentEdges.get(i).destination.row][array[step.row][step.col].incidentEdges.get(i).destination.col].parent = 
							array[step.row][step.col].incidentEdges.get(i).source; 
					queue.addLast(array[array[step.row][step.col].incidentEdges.get(i).destination.row][array[step.row][step.col].incidentEdges.get(i).destination.col]);
				}
				
			}
		}
		//Tron was never found.....
		System.out.println("out of while loop");
		return null;
	}

	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		File fileIn = new File(args[0]);
		Scanner in = new Scanner(fileIn);
		int rows = in.nextInt();
		int columns = in.nextInt();
		in.nextLine();
		boolean playing= true;
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Vertex Tron = null;
		Vertex Tower = null;
		Vertex Empty = null;
		Vertex Wall = null;
		Vertex[][] array = new Vertex[rows][columns];
		String [] inputArr = new String[rows];
		ArrayList<Character> bugs = new ArrayList<Character>();
		String wholeLine;
		int xTron = 0;
		int yTron = 0;

		
		//take in grid input
		for (int i = 0; i < rows; i++) {
			wholeLine = in.nextLine();
			inputArr[i] = wholeLine;
		}
		
		//assign input to 2d array
		for(int i = 0; i < rows; i++) {
			wholeLine = inputArr[i];
			for (int j = 0; j < columns; j++) {
				if (wholeLine.charAt(j)=='T') {
					Tron = new Vertex(wholeLine.charAt(j),i,j);
					xTron = i;
					yTron = j;
					array[i][j] = Tron;
					vertices.add(Tron);
				}else if (wholeLine.charAt(j)=='I') {
					Tower = new Vertex(wholeLine.charAt(j),i,j);
					array[i][j] = Tower;
					vertices.add(Tower);
				}else if (wholeLine.charAt(j)==' ') {
					Empty = new Vertex(wholeLine.charAt(j),i,j);
					array[i][j] = Empty;
					vertices.add(Empty);
				}else if (wholeLine.charAt(j)=='#') {
					Wall = new Vertex(wholeLine.charAt(j),i,j);
					array[i][j] = Wall;
					vertices.add(Wall);
				}
				else {
					//keep a list of bugs so that they can be moved in alphabetical order
					Vertex bug = new Vertex(wholeLine.charAt(j),i,j);
					array[i][j] = bug;
					vertices.add(bug);
					bugs.add(wholeLine.charAt(j));
				}
			}
		}
		
		//make changes to grid and print
		updateGrid(array, rows, columns);
		printGraph(array, rows, columns);
		
		//create graph
		Graph graph = new Graph(vertices, edges);
		
		//put the bugs in alphabetical order
		Collections.sort(bugs);
		ArrayList<Vertex> bugVerts = new ArrayList<Vertex>();
		//find vertices that are bugs and store them in list
		for (int i = 0; i < bugs.size(); i++) {
			for (int j = 0; j < vertices.size(); j++) {
				if (vertices.get(j).element == bugs.get(i)) {
					bugVerts.add(vertices.get(j));
					}
			}
		}
		
		//table of locations is stored.
		Scanner play = new Scanner(System.in);
		
		
		//while the program in waiting for a valid move, prompt for input
		while (playing) {
			System.out.println("please enter your move [u(p), d(own), l(elf), or r(ight)]: ");
			String input = play.next();
			char move = input.charAt(0);
			//for each possible move update the tron location
			//if the move is invalid promt again
			
			
			if(move == 'u') {
				xTron = xTron - 1;
				if (array[xTron][yTron].element == ' ') {	
					playing = false;
					array[xTron][yTron].element = 'T';
					updateGrid(array, rows, columns);
					printGraph(array, rows, columns);

				
				} else if (array[xTron][yTron].element == 'I') {
					System.out.println("WINNER");
				}else {
					xTron++;
					System.out.println("invalid input: please try again");
				}
			}else if(move == 'd') {
				xTron = xTron + 1;
				if (array[xTron][yTron].element == ' ') {
					playing = false;
					array[xTron][yTron].element = 'T';
					array[xTron - 1][yTron].element = ' ';
					updateGrid(array, rows, columns);
					printGraph(array, rows, columns);


				} else if (array[xTron][yTron].element == 'I') {
					System.out.println("WINNER");
				}else {
					xTron--;
					System.out.println("invalid input: please try again");
				}
			}else if(move == 'l') {
				yTron = yTron - 1;
				if (array[xTron][yTron].element == ' ') {
					playing = false;
					array[xTron][yTron].element = 'T';
					array[xTron][yTron + 1].element = ' ';
					updateGrid(array, rows, columns);
					printGraph(array, rows, columns);
				} else if (array[xTron][yTron].element == 'I') {
					System.out.println("WINNER");
				}else {
					yTron++;
					System.out.println("invalid input: please try again");
					}
			}else if(move == 'r') {
				yTron = yTron + 1;
				if (array[xTron][yTron].element == ' ') {
					playing = false;
					array[xTron][yTron].element = 'T';
					array[xTron][yTron - 1].element = ' ';
					updateGrid(array, rows, columns);
					printGraph(array, rows, columns);
				} else if (array[xTron][yTron].element == 'I') {
					System.out.println("WINNER");
					playing = false;
				}else {
					yTron--;
					System.out.println("invalid input: please try again");
				}
			}else {
				System.out.println("invaid input: please try again");
			}
		}
			Vertex theTron = array[xTron][yTron];
			printGraph(array, rows, columns);
			for (Vertex bug : bugVerts) {
				LinkedList<Vertex> output = new LinkedList<Vertex>();
				Vertex temp = BFS(graph, bug, theTron, array);
				while (temp != null) {
					output.addFirst(temp);
					temp = temp.parent;
				}
				char mover = 'z';
				if (output.get(0).row > output.get(1).row) {
					mover = 'u';
				}else if (output.get(0).row < output.get(1).row) {
					mover = 'd';
				}else if (output.get(0).col < output.get(1).col) {
					mover = 'r';
				}else if (output.get(0).col > output.get(1).col) {
					mover = 'l';
				}
				System.out.print("Bug   " + bug.element + ":    " + 
			"move: (" + mover +
			")   shortest path length: " + (output.size() - 1) + "   path: ");
				for (Vertex v : output) {
					System.out.print("(" + v.row + ", " + v.col + ") ");
				}
				System.out.println();

			}
		}
		

	}

