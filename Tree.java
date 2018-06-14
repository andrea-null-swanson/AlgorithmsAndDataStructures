/*

Author:  Andrea Swanson	
Email: aswanson2016@my.fit.edu
Course: CSE 2010
Section: 14
Description: Create a tree for Olympics organization

*/
import java.util.ArrayList;
public class Tree<E>{
	static Node<String> root;
	 public static class Node<E> {
		     E element;
		     Node<E> next;
		     Node<E> parent;
		     Node<E> child;
		     ArrayList<Node<E>> children = new ArrayList<Node<E>>();
		     
		    public Node (E el, Node<E> theParent, ArrayList<Node<E>> theChildren) {
		      element = el;
		      parent = theParent;
		      children = theChildren;
		    }
		    public Node (E el) {
			      element = el;
			      parent = null;
			      children = new ArrayList<Node<E>>();
			    }
		    
		    public String getElement() {
		    	return (String) element;
		    }

		    
	 }
	 //END OF NODE CLASS
	 //
		 public static void createRoot (String year) {
			 root = new Node<String>(year);
			 }
		 public static void appendChild(Node<String> toInsert, Node<String> parent) {			 
			 parent.children.add(toInsert);
		 }
		public static <E> void insertChild(Node<String> toInsert, Node<String> parent) {
			parent.children.add(toInsert);
			for (int i = 0; i < parent.children.size() - 1; i++) {
				if (i + 1 != parent.children.size()) {
					while (parent.children.get(i).element.compareTo(parent.children.get(i + 1).element) > 0) {
						Node<String> temp = parent.children.get(i);
						parent.children.set(i, parent.children.get(i + 1));
						parent.children.set(i + 1, temp);
						//System.out.println(parent.children.get(i).element);
						if (i > 0) {
						i--;	
						}
					}
				}			
			}
		}
		public static ArrayList<Node<String>> getChildren(Node<String> given) {
			return given.children;
			
		}
		public static <E> Node<String> getParent(Node<String> given) {
			return given.parent;
		}
}
