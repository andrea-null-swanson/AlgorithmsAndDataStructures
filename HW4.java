
	/*

	  Author:  Andrea Swanson	
	  Email: aswanson2016@my.fit.edu
	  Course: CSE 2010
	  Section: 14
	  Description: use two priority queues to match encrypted currency

	 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HW4 {
	
	//order is the value for the entry 
	//contains all needed information to match orders
	public static class Order{
		String time;
		String name;
		double price;
		int quantity;
		Order(String theTime, String theName, double thePrice, int theQuantity){
			time = theTime;
			name = theName;
			price = thePrice;
			quantity = theQuantity;
		}
	}
	//seller key uses the price and time for the sellers 
	//we want the lowest priced seller to be the minimum of the heap
	//if prices are equal, the earlier time stamp has priority
	public static class SellerKey implements Comparable<SellerKey> {
		public double price;
		public String time;
		
		SellerKey(String timeStamp, double thePrice){
			price = thePrice;
			time = timeStamp;
		}

		@Override
		public int compareTo(SellerKey o1) {
			int i = 0;
			if (o1.price < this.price) {
				i = 10;
			}
			if (o1.price > this.price) {
				i = -10;
			}
			if (o1.price == this.price){
					return this.time.compareTo(o1.time);

			}
			return i;
		}
	}
	//buyer key uses the price and time for the sellers 
	//the compare to for the seller key is the opposite of the sellers
	//earlier times still have priority
	public static class BuyerKey implements Comparable<BuyerKey> {
		public double price;
		public String time;
		
		BuyerKey(String timeStamp, double thePrice){
			price = thePrice;
			time = timeStamp;
		}

		@Override
		public int compareTo(BuyerKey o1) {
			int i = 0;
			if (o1.price < this.price) {
				i = -10;
			}
			if (o1.price > this.price) {
				i = 10;
			}
			if (o1.price == this.price){
					return this.time.compareTo(o1.time);
			}
			return i;
		}
		
	}
	//looks for the name that the user wants to enter and sees if any other buyer value has that name
	public static boolean isBuyerRepeat(HeapAdaptablePriorityQueue<BuyerKey, Order> buyers, String name) {
		boolean repeat = false;
		for (int i = 0;i < buyers.size(); i ++) {
			if (buyers.get(i).getValue().name.equals(name)) {
				repeat = true;
			}
		}
		return repeat;
	}
	//looks for the name that the user wants to enter and sees if any other seller value has that name
	public static boolean isSellerRepeat(HeapAdaptablePriorityQueue<SellerKey, Order> sellers, String name) {
		boolean repeat = false;
		for (int i = 0;i < sellers.size(); i ++) {
			if (sellers.get(i).getValue().name.equals(name)) {
				repeat = true;
			}
		}
		return repeat;		
	}
	public static void executeBuySellOrders(HeapAdaptablePriorityQueue<BuyerKey, Order> buyers,
			HeapAdaptablePriorityQueue<SellerKey, Order> sellers) {
		int buyerSize = buyers.size();
		loopy:
		for (int i = 0; i < buyerSize; i++) {
			if (buyers.isEmpty() || sellers.isEmpty()) {
				break loopy;
			}else {
			//when prices of the buyer and seller are equal or the buying price is greater than the selling price
			if (sellers.min().getValue().price <= buyers.min().getValue().price) {
				//the boolean tells us whether if we needed to compute an average
				//which requires a double rather than an integer
				boolean avg = false;
				double doublePrice = 0.0;
				double intPrice = 0;
				//unlikely event of seller price lower than buyer price
				if (sellers.min().getValue().price < buyers.min().getValue().price) {
					avg = true;
					double sPrice = sellers.min().getValue().price;
					double bPrice = buyers.min().getValue().price;
					doublePrice = (sPrice + bPrice) / 2.0;
				}
				doublePrice = sellers.min().getValue().price;
				int numPurchased = 0;
				int buyRemaining = 0;
				int sellRemaining = 0;
				String buyer =  buyers.min().getValue().name;
				String seller =  sellers.min().getValue().name;
				//if the seller wants to buy more than the seller can offer
				if (sellers.min().getValue().quantity > buyers.min().getValue().quantity) {
					sellers.min().getValue().quantity -= buyers.min().getValue().quantity;
					numPurchased = buyers.min().getValue().quantity;
					buyers.min().getValue().quantity = 0;
					buyRemaining = 0;
					sellRemaining = sellers.min().getValue().quantity;
					//since the seller quantity is zero, it gets removed
					buyers.removeMin();
					//if the buyer has quantity left over after the order
				} else if (sellers.min().getValue().quantity < buyers.min().getValue().quantity) {
					buyers.min().getValue().quantity -= sellers.min().getValue().quantity;
					numPurchased = sellers.min().getValue().quantity;
					sellers.min().getValue().quantity = 0;
					sellRemaining = 0;
					buyRemaining = buyers.min().getValue().quantity;
					//since the seller quantity is zero, it gets removed
					sellers.removeMin();
					//if the quantities for both orders are equal, both new quantites are zero, and get deleted
				}else if (sellers.min().getValue().quantity == buyers.min().getValue().quantity) {
					numPurchased =  buyers.min().getValue().quantity;
					buyRemaining = 0;
					sellRemaining = 0;
					buyers.removeMin();
					sellers.removeMin();
				}
				//if there was an average computed, print the double value computed for average
				if (avg) {
					//print
					System.out.println("ExecuteBuySellOrders " + doublePrice + " " + numPurchased);
					System.out.println("Buyer: " + buyer + " " + buyRemaining);
					System.out.println("Seller: " + seller + " " + sellRemaining);
				}
				//otherwise, print the price of either the seller or the buyer order because they are the same
				if (!avg) {
				System.out.println("ExecuteBuySellOrders " + intPrice + " " + numPurchased);
				System.out.println("Buyer: " + buyer + " " + buyRemaining);
				System.out.println("Seller: " + seller + " " + sellRemaining);
				}
			}
		}
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		File inFile = new File(args[0]);
		@SuppressWarnings("resource")
		Scanner in = new Scanner(inFile);		
		HeapAdaptablePriorityQueue<BuyerKey, Order> buyers = new HeapAdaptablePriorityQueue<BuyerKey, Order>();
		HeapAdaptablePriorityQueue<SellerKey, Order> sellers = new HeapAdaptablePriorityQueue<SellerKey, Order>();		
		while (in.hasNextLine()) {
			String command = in.next();
			
			//create key and value for a buy order and add to the appropriate heap
			if (command.equals("EnterBuyOrder")) {
				String time = in.next();
				String buyerName = in.next();
				double price = Double.parseDouble(in.next());
				int quantity = in.nextInt();
				System.out.print(command +  " " + time + " "+ buyerName + " " +
				price + " " + quantity);
				Order currentOrder = new Order(time, buyerName, price, quantity);
				BuyerKey currentKey = new BuyerKey(time, price);
				//checks that the buyer does not already exist in the heap
				if (!isBuyerRepeat(buyers, buyerName)) {
					buyers.insert(currentKey, currentOrder);
					System.out.println();
				}else {
					System.out.println(" ExistingBuyerError");
				}
				}
			else if (command.equals("CancelBuyOrder")) {
				//method to find and delete order
				String time = in.next();
				String buyerName = in.next();
				System.out.println(command +  " " + time + " "+ buyerName);
				for (int i = 0; i < buyers.size(); i++) {
					if (buyers.get(i).getValue().name.equals(buyerName)) {
						buyers.remove(buyers.get(i));
						break;
					}
				}
			}
			//find the buy order with the same name and replace the key and value with the appropriate changes
			else if (command.equals("ChangeBuyOrder")) {
				String time = in.next();
				String buyerName = in.next();
				double price = Double.parseDouble(in.next());
				int quantity = in.nextInt();
				System.out.print(command +  " " + time + " "+ buyerName + " " +
				price + " " + quantity);
				for (int i = 0; i < buyers.size(); i++) {
					if (buyers.get(i).getValue().name.equals(buyerName)) {
						String oldTime = buyers.get(i).getValue().time;
						BuyerKey newKey = new BuyerKey(time, price);
						if (!isBuyerRepeat(buyers, buyerName)) {
							Entry<BuyerKey, Order> entry = buyers.get(i);
							Order value = new Order(oldTime, buyerName, price, quantity);
							buyers.replaceValue(entry, value);
							buyers.replaceKey(entry, newKey);
							System.out.println();
							break;
						}else {
							System.out.println("ExistingBuyerError");
						}
					}
				}
				//create a sell order with the key and value and add it to the seller heap
			}else if (command.equals("EnterSellOrder")) {
				String time = in.next();
				String sellerName = in.next();
				double price = Double.parseDouble(in.next());
				int quantity = in.nextInt();
				System.out.print(command +  " " + time + " "+ sellerName + " " +
				price + " " + quantity);
				Order currentOrder = new Order(time, sellerName, price, quantity);
				SellerKey currentKey = new SellerKey(time, price);
				//checks that the seller does not already exit
				if (!isSellerRepeat(sellers, sellerName)) {
					sellers.insert(currentKey, currentOrder);
					System.out.println();
				}else {
					System.out.println(" ExistingBuyerError");
				}
			}
			//find and delete sell order
			else if (command.equals("CancelSellOrder")) {
				String time = in.next();
				String sellerName = in.next();
				if (!isSellerRepeat(sellers, sellerName)) {
								for (int i = 0; i < sellers.size(); i++) {
					if (sellers.get(i).getValue().name.equals(sellerName)) {
						sellers.remove(sellers.get(i));
						break;
					}else {
					System.out.println(" ExistingSellerError");
				}
				}	
				}
				
			}
			else if (command.equals("ChangeSellOrder")) {
				String time = in.next();
				String sellerName = in.next();
				double price = Double.parseDouble(in.next());
				int quantity = in.nextInt();
				System.out.println(command +  " " + time + " "+ sellerName + " " +
				price + " " + quantity);
				if (!isSellerRepeat(sellers, sellerName)) {
									for (int i = 0; i < sellers.size(); i++) {
					if (sellers.get(i).getValue().name.equals(sellerName)) {
						String oldTime = sellers.get(i).getValue().time;
						SellerKey newKey = new SellerKey(time, price);
						Entry<SellerKey, Order> entry = sellers.get(i);
						Order value = new Order(oldTime, sellerName, price, quantity);
						sellers.replaceValue(entry, value);
						sellers.replaceKey(entry, newKey);
						break;
					}
				}
				} else {
					System.out.print(" ExistingBuyerError");
				}

			}
			//display min
			else if (command.equals("DisplayHighestBuyOrder")) {
				String time = in.next();
				if (!buyers.isEmpty()) {
				String name = buyers.min().getValue().name;
				String oldTime = buyers.min().getValue().time;
				double price = buyers.min().getValue().price;
				int quantity = buyers.min().getValue().quantity;
				System.out.println("DisplayHighestBuyOrder " + time + " " + 
				name + " " + oldTime + " " + price + " " + quantity);
				}else {
					System.out.println("DisplayHighestBuyOrder " + time);
				}
			}
			//display min
			else if (command.equals("DisplayLowestSellOrder")) {
				String time = in.next();
				if (!sellers.isEmpty()) {
				String name = sellers.min().getValue().name;
				String oldTime = sellers.min().getValue().time;
				double price = sellers.min().getValue().price;
				int quantity = sellers.min().getValue().quantity;
				System.out.println("DisplayLowestSellOrder " + time + " " + 
				name + " " + oldTime + " " + price + " " + quantity);
				}else {
					System.out.println("DisplayLowestSellOrder " + time);
				}
			}else {
				System.out.print("invalid input: ");
				System.out.println(command);
			}
			//after each command, check if anything can be executed
			if (!buyers.isEmpty() && !sellers.isEmpty()) {
				executeBuySellOrders(buyers, sellers);
			}
		}	
}
}
