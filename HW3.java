	/*

	  Author:  Andrea Swanson	
	  Email: aswanson2016@my.fit.edu
	  Course: CSE 2010
	  Section: 14
	  Description: Create a tree for Olympics organization

	 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HW3<E> extends Tree<E> {
	
	//class to hold both sport and event 
	public static class Category{
		String sport;
		String event;
		Category (String theSport, String theEvent){
			sport = theSport;
			event = theEvent;
		}
	}
	//class holds object athlete with attribute to be accessed for comparison methods
	public static class Athlete {
		int medals = 0;
		int goldMedals = 0;
		String name;
		ArrayList<Category> sports = new ArrayList<Category>();
		//pass in boolean to determine action for goldMedals attribute 
		Athlete(String theName, boolean gold, String sport, String event){
			String[] array = theName.split(":");
			name = array[0];
			medals++;
			if (gold == true) {
				goldMedals++;
			}
			//keep list of sports with the event for sorting
			Category theNew = new Category(sport, event);
			sports.add(theNew);
		}
	}
	//class holds object athlete with attribute to be accessed for comparison methods
	public static class Country{
		int medals = 0;
		int goldMedals = 0;
		String name;
		//only need the name of the country for this object for sorting
		//pass in boolean to determine action for goldMedals attribute
		Country(String theName, boolean gold){
			String[] array = theName.split(":");
			name = array[1];
			medals++;
			if (gold == true) {
				goldMedals++;
			}
		}
	}
	
	//method to combine duplicated country and athlete statistics
	public static void consolidate (ArrayList<Athlete> athleteList, ArrayList<Country> countryList) {
		for (int i = 0; i < athleteList.size(); i ++) {
			String current = athleteList.get(i).name;
			for (int j = i + 1; j < athleteList.size(); j++) {
				//if the athlete already exits in the list, combine the stats and delete the old one
				if (current.equals(athleteList.get(j).name)) {
					athleteList.get(i).goldMedals += athleteList.get(j).goldMedals;
					athleteList.get(i).medals += athleteList.get(j).medals;
					athleteList.get(i).sports.add(athleteList.get(j).sports.get(0));
					athleteList.remove(j);
				}
			}
		}
		//do the same process to combine the country statistics
		for (int i = 0; i < countryList.size(); i ++) {
			String current = countryList.get(i).name;
			for (int j = i + 1; j < countryList.size(); j++) {
				if (current.equals(countryList.get(j).name)) {
					countryList.get(i).goldMedals += countryList.get(j).goldMedals;
					countryList.get(i).medals += countryList.get(j).medals;
					countryList.remove(j);
				}
				
			}
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		File dataFile = new File (args[0]);
		File queries = new File (args[1]);
		Scanner in = new Scanner(dataFile);
		int sports = 0;
		ArrayList<Athlete> athleteList = new ArrayList<Athlete>();
		ArrayList<Country> countryList = new ArrayList<Country>();	
		//initialize the root to the name of the olympics
		String lineOne = in.nextLine();
		String[] lineOneArray = lineOne.split(" ");
		//lineArray holds all the sports
		Node<String> root =  new Node<String>(lineOneArray[0]);
		//insert all the sports
		for (int i = 1; i < lineOneArray.length; i++) {
			appendChild(new Node<String>(lineOneArray[i]), root);
			sports++;
		}
		//insert all the events for the sports
		for (int i = 0; i < sports; i++) {
			String line = in.nextLine();
			String[] lineArray = line.split(" ");
			ArrayList<Node<String>> children = getChildren(root);
			//find the sport of the event and insert the child as a child of that sport, alphabetically
				if (children.get(i).element.equals(lineArray[0])) {
					for (int j = 1; j < lineArray.length; j++) {
						appendChild(new Node<String>(lineArray[j]), children.get(i));
					}
				}
		}
		//insert winners
		while (in.hasNext()) {
			String line = in.nextLine();
			String[] lineArray = line.split(" ");
			outer:
			for (int k = 0; k < sports; k++) {
				ArrayList<Node<String>> sportsList = getChildren(root);
				ArrayList<Node<String>> eventsList = getChildren(sportsList.get(k));
				int eventsPerSport = eventsList.size();
				//find the sport to insert the winners children into
				for (int q = 0; q < eventsPerSport; q++) {
				if (lineArray[0].equals(eventsList.get(q).element)) {
					//once it is found, insert the gold, silver, and bronze children in that order
					appendChild(new Node<String>(lineArray[1]), eventsList.get(q));
					appendChild(new Node<String>(lineArray[2]), eventsList.get(q));
					appendChild(new Node<String>(lineArray[3]), eventsList.get(q));
					//create objects for athlete and countries of the correct node to be sorted for later methods
					Athlete gold = new Athlete(lineArray[1], true, sportsList.get(k).element, eventsList.get(q).element);
					Athlete silver = new Athlete(lineArray[2], false, sportsList.get(k).element, eventsList.get(q).element);
					Athlete bronze = new Athlete(lineArray[3], false, sportsList.get(k).element, eventsList.get(q).element);
					Country goldC = new Country(lineArray[1], true);
					Country silverC = new Country(lineArray[2], false);
					Country bronzeC = new Country(lineArray[3], false);
					athleteList.add(gold);
					athleteList.add(silver);
					athleteList.add(bronze);			
					countryList.add(goldC);
					countryList.add(silverC);
					countryList.add(bronzeC);
					break outer;

				}
			}
		}
			//method to merge duplicate countries or athletes
			consolidate(athleteList, countryList);
			
	}
		
		//organize and alphabet
		Node<String> temp = null;
		int size = getChildren(root).size();
		Node<String> tempRoot= new Node<String>(root.element);
		for (int i = 0; i < size; i++) {
			insertChild(getChildren(root).get(i), tempRoot);
		}
		//use insertion to ensure order of node of depth one
		//reassign root to the new sorted tree root
			root = tempRoot;
		//enter queries files
		Scanner fin = new Scanner(queries);
		while(fin.hasNext()) {
			String command = fin.next();
			System.out.print(command + " ");
			if (command.equals("GetEventsBySport")) {
				String sport = fin.next();
				System.out.print(sport + " ");
				ArrayList<Node<String>> allSports = getChildren(root);
				ArrayList<Node<String>> eventsForSport = new ArrayList<Node<String>>();
				//find the sport in the tree
				for (int i = 0; i < allSports.size(); i++) {
					if (allSports.get(i).element.equals(sport)) {
						eventsForSport = getChildren(allSports.get(i));
					}
				}
				ArrayList<String> output = new ArrayList<String>();
				//add all the sports to a list
				for (int i = 0; i < eventsForSport.size(); i++) {
					output.add(eventsForSport.get(i).element);
				}
				//sort the list of strings
				Collections.sort(output);
				//output
				for (String e : output) {
					System.out.print(e + " ");
				}
				
				System.out.println();
			} else if (command.equals("GetWinnersAndCountriesBySportAndEvent")) {
				String sport = fin.next();
				String event = fin.next();
				System.out.print(sport + " " + event + " ");
				ArrayList<Node<String>> allSports = getChildren(root);
				ArrayList<Node<String>> eventsForSport = new ArrayList<Node<String>>();
				//find the sport
				for (int i = 0; i < allSports.size(); i++) {
					if (allSports.get(i).element.equals(sport)) {
						eventsForSport = getChildren(allSports.get(i));
					}
				}
				//find the event
				for (int i = 0; i < eventsForSport.size(); i++) {
					if(event.equals(eventsForSport.get(i).element)){
						ArrayList<Node<String>> winners = getChildren(eventsForSport.get(i));
						//output the list of winners
						for (int j = 0; j < winners.size(); j++) {
							System.out.print(winners.get(j).element + " ");
						}
					
					}
				}	
				System.out.println();
			}else if (command.equals("GetGoldMedalistAndCountryBySportAndEvent")) {
				//same as previous method, but only output the first winner in the winners list (gold)
				String sport = fin.next();
				String event = fin.next();
				System.out.print(sport + " " + event + " ");
				ArrayList<Node<String>> winners = new ArrayList<Node<String>>();
				ArrayList<Node<String>> allSports = getChildren(root);
				ArrayList<Node<String>> eventsForSport = new ArrayList<Node<String>>();
				for (int i = 0; i < allSports.size(); i++) {
					if (allSports.get(i).element.equals(sport)) {
						eventsForSport = getChildren(allSports.get(i));

					}
				}	
				for (int i = 0; i < eventsForSport.size(); i++) {
					if(event.equals(eventsForSport.get(i).element)){
						winners = getChildren(eventsForSport.get(i));
					}
				}
				System.out.println(winners.get(0).element);
			}else if (command.equals("GetAthleteWithMostMedals")) {
				int most = 0;
				ArrayList<String> nameReturn = new ArrayList<String>();
				//go through list of athletes and find the most medals earned
				for (int i = 0; i < athleteList.size(); i++) {
					if (athleteList.get(i).medals > most) {
						most = athleteList.get(i).medals;
					}
				}
				System.out.print(most + " ");
				//make a list of all the athletes that have that amount of medals
				for (int i = 0; i < athleteList.size(); i++) {
					if (athleteList.get(i).medals == most) {
						nameReturn.add(athleteList.get(i).name);
					}
				}
				//sort and output
				Collections.sort(nameReturn);
				for (String e: nameReturn) {
					System.out.print(e + " ");	
				}
				System.out.println();
			} else if (command.equals("GetAthleteWithMostGoldMedals")) {
				int most = 0;
				ArrayList<String> nameReturn = new ArrayList<String>();
				//find the most gold medals earned
				for (int i = 0; i < athleteList.size(); i++) {
					if (athleteList.get(i).goldMedals > most) {
						most = athleteList.get(i).goldMedals;
					}
				}
				System.out.print(most + " ");
				//find the athletes that earned that many gold medals
				for (int i = 0; i < athleteList.size(); i++) {
					if (athleteList.get(i).goldMedals == most) {
						nameReturn.add(athleteList.get(i).name);
					}
				}
				//sort and output
				Collections.sort(nameReturn);
				for (String e: nameReturn) {
					System.out.print(e + " ");	
				}
				System.out.println();
			}else if (command.equals("GetCountryWithMostMedals")) {
				int most = 0;
				ArrayList<String> countryReturn = new ArrayList<String>();
				//find most medals earned by a country
				for (int i = 0; i < countryList.size(); i++) {
					if (countryList.get(i).medals > most) {
						most = countryList.get(i).medals;
					}
				}
				System.out.print(most + " ");
				//find all countries that earned that many medals
				for (int i = 0; i < countryList.size(); i++) {
					if (countryList.get(i).medals == most) {
						countryReturn.add(countryList.get(i).name);
					}
				}
				//sort and output
				Collections.sort(countryReturn);
				for (String e: countryReturn) {
					System.out.print(e + " ");	
				}
				System.out.println();
				
			}else if (command.equals("GetCountryWithMostGoldMedals")) {
				int most = 0;
				ArrayList<String> countryReturn = new ArrayList<String>();
				//find most gold medals earned by a country
				for (int i = 0; i < countryList.size(); i++) {
					if (countryList.get(i).goldMedals > most) {
						most = countryList.get(i).goldMedals;
					}
				}
				System.out.print(most + " ");
				//find countries that earned that many gold medals
				for (int i = 0; i < countryList.size(); i++) {
					if (countryList.get(i).goldMedals == most) {
						countryReturn.add(countryList.get(i).name);
					}
				}
				//sort and output
				Collections.sort(countryReturn);
				for (String e: countryReturn) {
					System.out.print(e + " ");	
				}
				System.out.println();
				
			}else if (command.equals("GetSportAndEventByAthlete")) {
				String theAthlete = fin.next();
				System.out.print(theAthlete + " ");
				for (Athlete e : athleteList) {
					int j = 0;
					//find athlete and list the sports and events the object contain
					if (e.name.equals(theAthlete)) {
						String currentSport = e.sports.get(j).sport;
						for (int i = 0; i < e.sports.size(); i++) {
							if (currentSport.equals(e.sports.get(i).sport)) {
								System.out.print(e.sports.get(i).sport + ":");
								System.out.print(e.sports.get(i).event + " ");
							}
						}
						
						
					}
				}
				System.out.println();
			}else{
				System.out.println("invalid command");
			}
		}
		
	}
}
