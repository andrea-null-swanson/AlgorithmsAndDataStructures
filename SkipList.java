import java.util.ArrayList;

//import SinglyLinkedList.Node;
/*
  
* Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package net.datastructures;

/**
 * A basic doubly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */

/*

  Author:   Andrea Swanson
  Email:    aswanson2016@my.fit.edu
  Course:   CSE 2010
  Section:  14
  Description: 
 */

public class SkipList<E> {
	

  //---------------- nested Node class ----------------
  /**
   * Node of a doubly linked list, which stores a reference to its
   * element and to both the previous and next node in the list.
   */
	
	  
	  
	  //special left key for skip list start
	  private Node<Entry> negEnd;                    

	  //special right key for skip list right end
	  private Node<Entry> posEnd;                  
	  //keeps number of elements
	  private int size = 0;                      

	 //keeps current height of skip list
	  private int height = 0; 
	  //keeps track of the head of the list, so that we can traverse more easily
	  //same for tail
	  private Node<Entry> head;
	  private Node<Entry> tail; 
	  
	  private FakeRandomHeight fakeHeight = new FakeRandomHeight();

	
	
	
	
  static class Node<E> {

    /** The element stored at this node */
    private Entry element;               // reference to the element stored at this node

    /** A reference to the preceding node in the list */
    private Node<E> prev;            //previous node of element node

    /** A reference to the subsequent node in the list */
    private Node<E> next;            //next node of element node
    
    /** A reference to the subsequent node in the list */
    private Node<E> above;            // node above the node element
    
    /** A reference to the subsequent node in the list */
    
    private Node<E> below;            // node below the the element node
    /**
     * Creates a node with the given element and next node.
     *
     * @param e  the element to be stored
     * @param p  reference to a node that should precede the new node
     * @param n  reference to a node that should follow the new node
     * 
     * 
     */
    
    //when creating a new node, only assign the element, pointers must be manually assigned
    public Node(Entry e) {
      element = e;
      prev = null;
      next = null;
      above = null;
      below = null;
    }

    
    
    //methods to return the element of the given node
    
    public Entry getElement() { return element; }

    //returns the node that the current node points to previously
    public Node<E> getPrev() { return prev; }
    //returns the node that the current node points to next
    public Node<E> getNext() { return next; }
    //returns the node that the current node points to above
    public Node<E> getAbove() { return above; }
    //return the node that the current node points to below
    public Node<E> getBelow() { return below; }
   
    
    //change the above pointer for the given node for above
    public void setAbove(Node<E> a) { above = a; }
    //change the above pointer for the given node for below
    public void setBelow(Node<E> b) { below = b; }
    //change the above pointer for the given node for next 
    public void setNext(Node<E> n) { next = n; }
    //change the above pointer for the given node for prev 
    public void setPrev(Node<E> p) { prev = p; }

    public String toString() {
       return String.format("%d:%s", this.element.key, this.element.value);
    }
  } //----------- end of nested Node class -----------



  
  
  //constructor for new skip list
  //with empty top list with positive and negative special nodes only...
  public SkipList() {
      Entry neg = new Entry(Integer.MIN_VALUE, null); //special key values negative inf (left end)
      Entry pos = new Entry(Integer.MAX_VALUE, null); //special key value pos inf (right end)
      Node<Entry> negEnd = new Node<Entry>(neg);
      Node<Entry> posEnd = new Node<Entry>(pos);
      negEnd.setNext(posEnd); 
      posEnd.setPrev(negEnd);
      head = negEnd;	//head is the leftmost, topmost node in the map
      tail = posEnd;	//tail is the rightmost, topmost node in the map
      
  }

  // public accessor methods
  /**
   * Returns the number of elements in the linked list.
   * @return number of elements in the linked list
   */
  public int size() { return size; }

  //Return the height
  public int height() { return height; }
  /**
   * Tests whether the linked list is empty.
   * @return true if the linked list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the first element of the list.
   * @return element at the front of the list (or null if empty)
   */
  public Entry first() {
    if (isEmpty()) return null;
    return negEnd.getNext().getElement();  
  }

  /**
   * Returns (but does not remove) the last element of the list.
   * @return element at the end of the list (or null if empty)
   */
  public Entry last() {
    if (isEmpty()) return null;
    return posEnd.getPrev().getElement();    
  }

  // public update methods
  /**
   * Adds an element to the front of the list.
   * @param e   the new element to add
   */
  public void addFirst(Entry e) {
    addBetween(e, negEnd, negEnd.getNext());    // place just after the header since the header is a special key
  }

  /**
   * Adds an element to the end of the list.
   * @param e   the new element to add
   */
  public void addLast(Entry e) {
    addBetween(e, posEnd.getPrev(), posEnd);  // place just before the trailer since the trailer is the special key
  }

  /**
   * Removes and returns the first element of the list.
   * @return the removed element (or null if empty)
   */
  public Entry removeFirst() {
    if (isEmpty()) return null;                  // cannot remove if the list is empty
    return remove(negEnd.getNext());             // first element will be the next after the header
  }
  
  
  
  //named removal because doubly linked list already implements remove..
  public String removal(int key) {
	  Node<Entry> temp = search(key); //use the ndoe greatest key thath is less than the key we are searching for
	  if (temp.element.key != key) {
		  System.out.println("doesnt exist");  //for testing purposes
		  return "none: does not exist";		//for testing purposes
	  }
	  Node<Entry> p = search(key);				//holds the search node
	  Node<Entry> tempp = p;					//holds original search node so the p variable can be manipulated
	  int counter = 0;
	  
	  //count how high the tower is for this specific key (counter)
	  while (p != null && p.element.key == key) {
		  counter++;
		  p = p.above;
	  }
	  p = temp;
	  temp = p;
	  //for each level in the tower, see if the level would be empty without the element
	  //then change the pointers so that the elements to the left and right of the search node point to each other
	  for (int i = 0; i < counter; i++) {
		  Node<Entry> leftOf = p.prev;
		  Node<Entry> rightOf = p.next;
		  Node<Entry> holder = p;
		  //if the deletion of the node will create an empty level
		  if (leftOf.element.value == null && rightOf.element.value == null) {
			  leftOf.above.below = leftOf.below;
			  rightOf.above.below = rightOf.below;
			  leftOf.below.above = leftOf.above;
			  rightOf.below.above = rightOf.above;
			  height--;			//if deleting the node creates an empty level, delete the level and decrease the height of the tower
		  }  
		  leftOf.next = rightOf;
		  rightOf.prev = leftOf;
		  p = p.above;
		  
	  }
	  //if the tower is of height zero, the node with this key does not exist in the tower...
	  if (counter == 0) {
		  return "none";
	  }
	  //return the name of the deleted nodes value
	 return temp.element.value;
  }

  /**
   * Removes and returns the last element of the list.
   * @return the removed element (or null if empty)
   */
  public Entry removeLast() {
    if (isEmpty()) return null;                  // nothing to remove
    return remove(posEnd.getPrev());            // last element is before trailer
  }

  // private update methods
  /**
   * Adds an element to the linked list in between the given nodes.
   * The given predecessor and successor should be neighboring each
   * other prior to the call.
   *
   * @param predecessor   node just before the location where the new element is inserted
   * @param successor     node just after the location where the new element is inserted
   */
  private void addBetween(Entry e, Node<Entry> predecessor, Node<Entry> successor) {
    // create and link a new node
    Node<Entry> newest = new Node<>(e);
    predecessor.setNext(newest);
    successor.setPrev(newest);
    size++;
  }

  /**
   * Removes the given node from the list and returns its element.
   * @param node    the node to be removed (must not be a sentinel)
   */
  private Entry remove(Node<Entry> node) {
    Node<Entry> predecessor = node.getPrev();
    Node<Entry> successor = node.getNext();
    predecessor.setNext(successor);
    successor.setPrev(predecessor);
    size--;
    return node.getElement();
  }

  /**
   * Produces a string representation of the contents of the list.
   * This exists for debugging purposes only.
   */
  
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    Node<Entry> walk = negEnd.getNext();
    while (walk != posEnd) {
      sb.append(walk.getElement());
      walk = walk.getNext();
      if (walk != posEnd)
        sb.append(", ");
    }
    sb.append(")");
    return sb.toString();
  }
  ///////////// ----------  Skip list Implementation  -----------------------
  
  
  
//search through skip list to find the node with the key passed in  
  public Node<Entry> get(int key) { 
	  //start at the head
      Node<Entry> temp = head;
      //move down until we are at the lowest level
      while(temp.below!=null) {
          temp = temp.below;
      }
      //move through the bottom list, left to right until you reach the right end of level s0
      while(temp.next!=null) {
    	  //if the temp key matches the passed in key, return that element but do not remove it
          if(temp.element.key == key) {
              return temp; 
          }
          //try the next node in the list
          temp = temp.next;
      }
      //if there is no node with the matching key, it does not exist --> return null
      return null; 
  }
  
  
  //find the node position with the greatest key less than or equal to the given key, in the lowest list
  public Node<Entry> search (int key) { 
	  //start with the head
      Node<Entry> temp = head; 
      //move all the way to the bottom level
      while(temp.below !=null) { 
          temp = temp.below;
      }
     //move throught the list left to right until the key is found or the next key will be greater 
      while(temp.next.element.key <= key){		
          temp = temp.next; 
          }
      return temp; //return the node with the greatest key less than or equal to specified key
  } 
  
  public void putPointers(Node<Entry> pos, Node<Entry> ins) {
      ins.prev = pos; //change all links
      ins.next = pos.next;
      ins.above = null;
      pos.next.prev = ins;
      pos.next = ins;
      if(height==0) {
    	  newLevel();
      }
      
	  }


  //creates a new level
  public void newLevel () {
      Entry neg = new Entry(Integer.MIN_VALUE,null);
      Entry pos = new Entry(Integer.MAX_VALUE,null);
      Node<Entry> negEnd = new Node<Entry>(neg);
      Node<Entry> posEnd = new Node<Entry>(pos);
      negEnd.next = posEnd;
      negEnd.below = head;
      posEnd.prev = negEnd;
      posEnd.below = tail;
      head.above = negEnd;
      tail.above = posEnd;
      head = negEnd;
      tail = posEnd;
      height++;
  }
  
  public Entry put(int key, String value) {
    Node<Entry> position = get(key);  
    if(position!=null) {
        return position.element;	//if the key already exist, return the current value of that key
    } 
    
        position = search(key);  
        Node<Entry> insertionNode =  new Node<Entry>(new Entry(key,value)); //node to insert 
        putPointers(position, insertionNode);

    int level = 0;
    int coinToss = fakeHeight.get();
    
    while(level <= coinToss) {

        if(level>0) {
            if(level >= height) {
                //add level
            	newLevel();
            }
            //go backwards until a node have a non - empty upward pointer
            while(position.above == null) {
                position = position.prev;
            }
            //position holds the node that is the closest left position of the insertion node on the above level
            position = position.above;
            if(level>0) {
                Node<Entry> insert = new Node<Entry> (new Entry(key,value)); //inserting new node
                putPointers(position, insert);
                insertionNode.above = insert; 
                insertionNode = insert;
            } 
        }
        level++;
    }
    return insertionNode.element;  
  }
  
  
  
  
  //list of between and including the two key times 
  public ArrayList<Entry> subMap(int key1, int key2) {
      ArrayList<Entry> output = new ArrayList<Entry>();
      //list to store output
      //the start of the search
      Node<Entry> start = search(key1);
      
      //pos used to move through until key 2 is reached
      Node<Entry> pos = start;
      //if the start in the left special key, move to the right one to avoid contradicting while loop
      if(pos.element.value == null) {
        pos = pos.next;
      }
      while(pos.element.value != null) {
    	  //while moving through, if the pos node's key is between the parameters 
    	  //of the submap, add it to the output arraylist
          if(pos.element.key >= key1 && pos.element.key <= key2) {
              output.add(pos.element);
          }
          pos = pos.next;
      }
      return output; 
      //return arraylist
      //if the submap does not hold any nodes, the arraylist will be empty
  }
  

  
  //print the skip lists 
  //level by level
  public void printStack() {
      int count = height;
      Node<Entry> temp = head;
      Node<Entry> first;
      System.out.format("(S%d) empty%n", count);
      temp = temp.below;
      //descending print order
      for (int i = 0; i < height; i++){
          count--;
          first = temp;
          System.out.format("(S%d)", count);
          while(temp.next.next != posEnd) {
              temp = temp.next;
              System.out.format(" %08d: ", temp.getElement().key);
              System.out.print(temp.getElement().value);
          }
          System.out.format("%n");
          temp = first.below;  
  
      }
  }
  
} //----------- end of DoublyLinkedList class -----------

