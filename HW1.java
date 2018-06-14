import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/*

  Author:  Andrea Swanson	
  Email: aswanson2016@my.fit.edu
  Course: CSE 2010
  Section: 04
  Description: sort and products using a Singly Linked List class 

 */

public class HW1<E> extends SinglyLinkedList<E> //class extends the modified SinglyLinkedListClass
{  
	public static class Product{ //class product creates objects stored in the each product list
		String product, seller;
		double price, shipCost, shipMin, totalCost;
		int quantity;
		Boolean available;
			public Product(String theProduct, String theSeller, double thePrice) {
			product = theProduct;
			seller = theSeller;
			price = thePrice;
			}
		
	}
	public static class Seller { //class seller creates objects to store in list of sellers
		
		String seller;
		double cost, min;		
			public Seller(String theSeller, double theCost, double theMin) {
			this.seller = theSeller;
			cost = theCost;
			min = theMin;
		}
		
	}
	
	public static void printList(String product, SinglyLinkedList<Product> ipods, //method to display seller list
			SinglyLinkedList<Product> hdmis, SinglyLinkedList<Product> usbs) {
		if (product.equals(" appleIphone")) { //if the product from input is the apple iphone 
			Node<Product> temp = ipods.firstNode(); //temp node created to move through list of iphones
			System.out.printf("%10s%14s%14s%11s%n" ,"seller", //printformat to satisfy the format of the output
					"productPrice", "shippingCost",
					"totalCost");
			for (int i = 0; i < ipods.size(); i++) { //moves through list of iphones 
				if (temp.getElement().quantity > 0 ) { //only if the inventory of the product is at least one, the information gets displayed
					System.out.printf("%10s%14.2f%14.2f%11.2f%n" ,temp.getElement().seller, //printformat the correct output for the product object with inventory > 0
							temp.getElement().price, temp.getElement().shipCost,
							temp.getElement().totalCost);
				}
				temp = temp.getNext();	//the temporary node gets reassigned to the next node in the linked list
			}
		}
		if (product.equals(" hdmi2VgaAdapter")) { //repeats same process from iphones, but for hdmis
			Node<Product> temp = hdmis.firstNode();
			System.out.printf("%10s%14s%14s%11s%n" ,"seller",
					"productPrice", "shippingCost",
					"totalCost");
			for (int i = 0; i < hdmis.size(); i++) {
				if (temp.getElement().quantity > 0 ) {
					System.out.printf("%10s%14.2f%14.2f%11.2f%n" ,temp.getElement().seller,
							temp.getElement().price, temp.getElement().shipCost,
							temp.getElement().totalCost);
				}
				temp = temp.getNext();
			}	
		}
		if (product.equals(" USBdrive")) { //repeats same process from iphones, but for usbs
			Node<Product> temp = usbs.firstNode();
			System.out.printf("%10s%14s%14s%11s%n" ,"seller",
					"productPrice", "shippingCost",
					"totalCost");
			for (int i = 0; i < usbs.size(); i++) {
				if (temp.getElement().quantity > 0 ) {
					System.out.printf("%10s%14.2f%14.2f%11.2f%n" ,temp.getElement().seller,
							temp.getElement().price, temp.getElement().shipCost,
							temp.getElement().totalCost);			
				}
				temp = temp.getNext();
			}	
		}
	}
	
	
	
	public static void sortNewProduct(String product, String seller, double price,
			SinglyLinkedList<Product> ipods, SinglyLinkedList<Product> hdmis,
			SinglyLinkedList<Product> usbs, SinglyLinkedList<Seller> sellerInfo) { //sort new product dynamically sorts the lists as the products are inserted into the list
		
		
		if (product.equals("appleIphone")) { //if the new product is an iphone, use the list of iphones 
			Product current = new Product(product, seller, price); //create new product object with the given product, seller, price
			Node<Seller> tempSeller = sellerInfo.firstNode();  //creates a temporary node to move through the seller list to find the appropriate shipping cost
			 //move through entire seller list
			while(tempSeller != null) {
				if(tempSeller.getElement().seller.equals(current.seller)) { 
					//if the seller in the seller list matches the seller of the product
					//and the object price allows for free shipping, total cost is the price and shipping is zero
					if (current.price >= tempSeller.getElement().min) { 
						current.totalCost = current.price;
						current.shipCost = 0; 
					}
					// if the cost of the product is below the minimum for free shipping, the total cost is the price and the shipping cost
					else {
						current.totalCost = current.price + tempSeller.getElement().cost; 
						current.shipCost = tempSeller.getElement().cost; 
					}
					
				}
				tempSeller = tempSeller.getNext(); //reassign the tempSeller to the next node in the list 
			}
			//sort accordingly within the list of iphones
			if (ipods.isEmpty() || (current.totalCost <= ipods.firstNode().getElement().totalCost)) { //if the list is empty, or the total price is the lowest in the list,
				ipods.addFirst(current); //add the current node to be the first in the list
			} else {
				//create temporary nodes to find the right spot to insert the new product in the list of iphones
				SinglyLinkedList.Node<Product> peek1 = ipods.firstNode();
				SinglyLinkedList.Node<Product> holder = null; 
				double insertCost = current.totalCost;
				double nextCost = ipods.firstNode().getElement().totalCost; 
				boolean used = false;
				//while the cost of the product is greater than the next cost, continue to move through the list
				while (insertCost > nextCost) {
					holder = peek1;
					peek1 = peek1.getNext();
					if (peek1 == null) { //if peek is null, you have reached the end of the list and the product belongs at the end
						ipods.addLast(current);
						used = true;
						break;
						
					}
					nextCost = peek1.getElement().totalCost;					
				}
				//if the insert cost is not greater than the cost in the next node
				//insert the element between the previous and the next element
				if (used == false) { 
					ipods.insertAfter(holder, current);  //self defined method to insert the node current after the node holder
				}
			}
				
			}
		
		
		//repeat whole process from above but for hdmis
		if (product.equals("hdmi2VgaAdapter")) {
			Product current = new Product(product, seller, price);
			Node<Seller> tempSeller = sellerInfo.firstNode();
			
			while(tempSeller != null) {
				if(tempSeller.getElement().seller.equals(current.seller)) { 
					if (current.price >= tempSeller.getElement().min) {
						current.totalCost = current.price;
						current.shipCost = 0;					}
					else {
						current.totalCost = current.price + tempSeller.getElement().cost;
						current.shipCost = tempSeller.getElement().cost;
					}
					
				}
				tempSeller = tempSeller.getNext();
			}
			if (hdmis.isEmpty() || (current.totalCost <= hdmis.firstNode().getElement().totalCost)) {
				hdmis.addFirst(current);
			} else {
				SinglyLinkedList.Node<Product> peek1 = hdmis.firstNode();
				SinglyLinkedList.Node<Product> holder = null;
				double insertCost = current.totalCost;
				double nextCost = hdmis.firstNode().getElement().totalCost;
				boolean used = false;
				while (insertCost > nextCost) {
					holder = peek1;
					peek1 = peek1.getNext();
					if (peek1 == null) {
						hdmis.addLast(current);
						used = true;
						break;
					}
					nextCost = peek1.getElement().totalCost;
				}
				if (used == false) {
					hdmis.insertAfter(holder, current);
				}
			}
				
			}
		//repeat the whole process from above but for USBs instead of hdmis
		
		if (product.equals("USBdrive")) {
			Product current = new Product(product, seller, price);
			Node<Seller> tempSeller = sellerInfo.firstNode();
			
			while(tempSeller != null) {
				if(tempSeller.getElement().seller.equals(current.seller)) { 
					if (current.price >= tempSeller.getElement().min) {
						current.totalCost = current.price;
						current.shipCost = 0;
					}
					else {
						current.totalCost = current.price + tempSeller.getElement().cost;
						current.shipCost = tempSeller.getElement().cost;					}
					
				}
				tempSeller = tempSeller.getNext();
			}
			if (usbs.isEmpty() || (current.totalCost <= usbs.firstNode().getElement().totalCost)) {
				usbs.addFirst(current);
			} else {
				SinglyLinkedList.Node<Product> peek1 = usbs.firstNode();
				SinglyLinkedList.Node<Product> holder = null;
				double insertCost = current.totalCost;
				double nextCost = usbs.firstNode().getElement().totalCost;
				boolean used = false;
				while (insertCost > nextCost) {
					holder = peek1;
					peek1 = peek1.getNext();
					if (peek1 == null) {
						usbs.addLast(current);
						used = true;
						break;
					}
					nextCost = peek1.getElement().totalCost;
				}
				if (used == false) {
					usbs.insertAfter(holder, current);
				}
			}
			}		
	} 
	
	
	
    public static <E> void main(String[] args) throws FileNotFoundException {
    	File inFile = new File(args[0]);
    	Scanner in = new Scanner(inFile);
    	SinglyLinkedList <Seller> sellerInfo = new SinglyLinkedList<> (); //holds all the sellers and their shipping info
    	SinglyLinkedList <Product> ipods = new SinglyLinkedList<> (); //holds all the info for any iphone product
    	SinglyLinkedList <Product> hdmis = new SinglyLinkedList<> (); //holds all the info for any hdmi product
    	SinglyLinkedList <Product> usbs = new SinglyLinkedList<> ();  //holds all the info for any usbs
    	//while the input file still has input
    	while (in.hasNext()) {
    		//take in the first string and hold it as a command
    		String command = in.next();
    		if (command.equals("SetProductPrice")) {
    			String product = in.next();
    			String seller = in.next();
    			double price = in.nextDouble();
    			//display required output
    			System.out.println("SetProductPrice " + product + " " +  seller + " " + price );
    			//call method to create object and insert into list in correct order
    			sortNewProduct(product, seller, price, ipods, hdmis, usbs, sellerInfo);    		
    		}
    		else if (command.equals("SetShippingCost")) {
    			String seller = in.next();
    			double shipCost = in.nextDouble();
    			double min = in.nextDouble();
    			//display required output
    			System.out.println("SetShippingCost " +  seller + " " + shipCost + " " + min );
    			//create new seller object and add to list of sellers
    			Seller current = new Seller(seller, shipCost, min);     			
    			sellerInfo.addFirst(current); 
    		}   		
    		else if (command.equals("IncreaseInventory")) {
    			String productToInc = in.next();
    			String sellerToInc = in.next();
    			int increment = in.nextInt();
    			//if the product that we want to increase is an iphone, maniupulate objects in the list of iphones
    			if (productToInc.equals("appleIphone")) {
    				//use a temporary node to move through iphones to find the node that 
    				//contains the same seller
    				Node<Product> temp = ipods.firstNode();
    				if (ipods.isEmpty()) {
    					System.out.println("empty");
    				}
    				if (temp.getNext() == null) {
    					//if there is only one node in the list
    					//update the quantity in that node by the increment
    					if (temp.getElement().seller.equals(sellerToInc)) {
    						temp.getElement().quantity += increment;
    						//display required output
    						System.out.println("IncreaseInventory " + temp.getElement().product + " " + 
    						temp.getElement().seller + " " + increment +  " " + 
    						temp.getElement().quantity);
    					}
    					
    				}else {
    				while (temp!=null) {
    					//move through the list to find the correct node and update the inventory
    					//by the increment
    					if (temp.getElement().seller.equals(sellerToInc)) {    						
    						temp.getElement().quantity += increment;
						System.out.println("IncreaseInventory " + temp.getElement().product + " " + 
						temp.getElement().seller + " " + increment +  " " +
						temp.getElement().quantity);    					}
    					temp = temp.getNext();
    				}
    				}
    			}
    			//same process for usbs 
    			if (productToInc.equals("USBdrive")) {
    				Node<Product> temp = usbs.firstNode();
    				if (usbs.isEmpty()) {
    					System.out.println("empty");
    				}
    				if (temp.getNext() == null) {
    					if (temp.getElement().seller.equals(sellerToInc)) {
    						temp.getElement().quantity += increment;
    						System.out.println("IncreaseInventory " + temp.getElement().product + " " + 
    						temp.getElement().seller + " " + increment +  " " +
    						temp.getElement().quantity);
    						}
    				}
    				else {
    				while (temp!=null) {
    					if (temp.getElement().seller.equals(sellerToInc)) {
    						temp.getElement().quantity += increment;
    						System.out.println("IncreaseInventory " + temp.getElement().product + " " + 
    						temp.getElement().seller + " " + increment +  " " +
    						temp.getElement().quantity);
    						}
    					temp = temp.getNext();
    				}
    				}
    			}
    			//same process for hdmis
    			 if(productToInc.equals("hdmi2VgaAdapter")) {
    				Node<Product> temp = hdmis.firstNode();
    				if (hdmis.isEmpty()) {
    					System.out.println("empty");
    				}
    				if (temp.getNext() == null) {
    					if (temp.getElement().seller.equals(sellerToInc)) {
    						temp.getElement().quantity += increment;
    						System.out.println("IncreaseInventory " + temp.getElement().product + " " + 
    						temp.getElement().seller + " " + increment +  " " +
    						temp.getElement().quantity);
    						}
    				}
    				else {
    				while (temp!=null) {
    					if (temp.getElement().seller.equals(sellerToInc)) {
    						temp.getElement().quantity += increment;
    						System.out.println("IncreaseInventory " + temp.getElement().product + " " + 
    						temp.getElement().seller + " " + increment + " " +
    						temp.getElement().quantity);
    						}
    					temp = temp.getNext();
    					}
    				}
    			}
    		} 
    		//all customer purchase processes are the same, but there is a decrement that is subtracted
    		//rather than an increment that is added
    		else if (command.equals("CustomerPurchase")) {
    			String productToDec = in.next();
    			String sellerToDec = in.next();
    			int decrement = in.nextInt();    			
    			if (productToDec.equals("appleIphone")) {
    				Node<Product> temp = ipods.firstNode();
    				while (temp != null) {
    					if (temp.getElement().seller.equals(sellerToDec)) {
    						if (decrement > temp.getElement().quantity) {
    							System.out.println("CustomerPurchase " + temp.getElement().product + 
    									" " + temp.getElement().seller + " " + 
    									decrement + " NotEnoughInventoryError");
    							} else {
	    						temp.getElement().quantity -= decrement;
	    						System.out.println("CustomerPurchase " + temp.getElement().product + " " +
	    						temp.getElement().seller + " " + decrement +
	    						" " + temp.getElement().quantity);
	    						}
    						}
    					temp = temp.getNext();
    					}
    				}
    			if (productToDec.equals("hdmi2VgaAdapter")) {
    				Node<Product> temp = hdmis.firstNode();
    				while (temp!= null) {
    					if (temp.getElement().seller.equals(sellerToDec)) {
    						if (decrement > temp.getElement().quantity) {
    							System.out.println("CustomerPurchase " + temp.getElement().product + 
    									" " + temp.getElement().seller + " " + 
    									decrement + " NotEnoughInventoryError");
    						} else {
    						temp.getElement().quantity -= decrement;
    						System.out.println("CustomerPurchase " + temp.getElement().product + " " +
    						temp.getElement().seller + " " + decrement +
    						" " + temp.getElement().quantity);
    						}
    						}
    					temp = temp.getNext();
    					}
    				}
    			if (productToDec.equals("USBdrive")) {
    				Node<Product> temp = usbs.firstNode();
    				while (temp != null) {
    					if (temp.getElement().seller.equals(sellerToDec)) {
    						if (decrement > temp.getElement().quantity) {
    							System.out.println("CustomerPurchase " + temp.getElement().product + 
    									" " + temp.getElement().seller + " " + 
    									decrement + " NotEnoughInventoryError");
    							} else {
    						temp.getElement().quantity -= decrement;
    						System.out.println("CustomerPurchase " + temp.getElement().product + " " +
    						temp.getElement().seller + " " + decrement +
    						" " + temp.getElement().quantity);
    						}
    						}
    					temp = temp.getNext();
    					}
    				}
    		}
    		else if (command.equals("DisplaySellerList")) {
    			String product = in.nextLine(); 
    			System.out.println("DisplaySellerList" + product);
    			//method to diplay the list of available products 
    			printList(product, ipods, hdmis, usbs);
    		}
    		//if a command is invalid, nothing happens
    	}
    	}
    }
