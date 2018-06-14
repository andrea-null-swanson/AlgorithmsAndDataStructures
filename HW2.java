import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HW2 {
	public static class course {
		String courseID;
		ArrayList<String> classTimes = new ArrayList<String>();
		int numOfTimes;
		int initialSize = 0;
		public course (String[] inputArray) {
			initialSize = inputArray.length - 1;
			courseID = inputArray[0];
			for (int i = 1; i < inputArray.length; i++) {
				classTimes.add(inputArray[i]);
				
			}
			
		}
	}
	public static class entry {
		String course;
		String time;
		int score1;
		int score2;
		public entry(String theCourse, String theTime, int scoreOne, int scoreTwo){
			course = theCourse;
			time = theTime;
			score1 = scoreOne;
			score2 = scoreTwo;
		}
	}
	
	public static void makeSchedules(ArrayList<course> courses,
			ArrayList<String> possibleSchedules,
			String currentList, int placeHolder) {
		//base case
		if (placeHolder == courses.size()) {
			possibleSchedules.add(currentList);
			return;
		}
		//recursively enumerates all the way through to the bottom of the first set
		//of possibilities and moves back up only once all the 
		//possibilities have been created
		for (int i = 0; i <courses.get(placeHolder).classTimes.size(); i++) {
			makeSchedules(courses, possibleSchedules, currentList + courses.get(placeHolder).courseID + " " + courses.get(placeHolder).classTimes.get(i) + " " , placeHolder + 1);
		//uses i to move through courses to get only one from each class list
		}
		
	}
		
		public static void findBest (ArrayList<ArrayList<entry>> allSchedules, ArrayList<entry> conflicts,
				ArrayList<ArrayList<entry>> conflictList) {
			ArrayList<ArrayList<entry>> remaining = new ArrayList<ArrayList<entry>>();
			ArrayList<ArrayList<entry>> remainingConflicts = new ArrayList<ArrayList<entry>>();
			int largestSize = 0;
			//the best schedule will have the largest size in the arrayList
			
			for (int i = 0; i < allSchedules.size(); i++) {
				if (allSchedules.get(i).size() > largestSize) {
					largestSize = allSchedules.get(i).size();
				}
			}
			for (int i = 0; i < allSchedules.size(); i++) {
				if (allSchedules.get(i).size() == largestSize) {
					remaining.add(allSchedules.get(i));
					remainingConflicts.add(conflictList.get(i));
					
				}
			}
			System.out.println("----- Course Schedule -----");
			for (int i = 0; i < remaining.get(0).size(); i++) {
				System.out.println(remaining.get(0).get(i).course + " " +remaining.get(0).get(i).time);
			}
			System.out.println("------Courses with time Conflicts-------");
			for (int i = 0; i < remainingConflicts.get(0).size(); i++) {
				System.out.println(remainingConflicts.get(0).get(i).course + " " +remainingConflicts.get(0).get(i).time);
			}
			
			
		}
		
	
	
public static void main(String[] args) throws FileNotFoundException {
	File inFile = new File(args[0]);
	Scanner in = new Scanner(inFile);
	ArrayList<course> courses = new ArrayList<course>();
	//takes in the input
	while (in.hasNextLine()) {
		String wholeInput = in.nextLine();
		String[] inputArray = wholeInput.split(" ");
		course toAdd = new course(inputArray);
		courses.add(toAdd);
		//stores in a list of courses
	} //all information needed is there
	//input complete
	ArrayList<String> possibleSchedules = new ArrayList<String>();
	String emptyString = "";
	makeSchedules(courses, possibleSchedules, emptyString, 0);
	//recursive call made to make all possible schedules
	//sepearte lists made for working schedules and
	//conflicts with corresponding indices
	ArrayList<entry> conflicts = new ArrayList<entry>();
	ArrayList<ArrayList<entry>> allSchedules = new ArrayList<ArrayList<entry>>();
	ArrayList<entry> holderList = new ArrayList<entry>();
	ArrayList<ArrayList<entry>> conflictList = new ArrayList<ArrayList<entry>>();
	
	
	for(int i = 0; i < possibleSchedules.size(); i++) {
		String[] addString = possibleSchedules.get(i).split(" ");
		ArrayList<entry> temp = new ArrayList<entry>();
		temp.clear();
		holderList.clear();
		int k= 0;
		for (int j = 0; j < addString.length; j+=2) {
			int score1 = i;
			int score2 = j;
			//create a new object for entry that holds 
			//both the course and the time for that schedule
			entry holder = new entry (addString[j],addString[j+1], score1, score2);
			holderList.add(k, holder);
			k++;
		}

		for (int k1 = 0; k1 < addString.length / 2; k1 ++) {
				temp.add(holderList.get(k1));
		}
		allSchedules.add(temp);
		//once the temp has all the objects in it, add it to 
		//the list of completed schedules
		
	}
	for (int k = 0; k < allSchedules.size(); k ++) {
		//start of conflict search
		ArrayList<entry> holdConflicts = new ArrayList<entry>();
		holdConflicts.clear();		
		int lastIndex = allSchedules.get(k).size() - 1;
		int mover = 1;
		//finds conflicts by starting at the last time and 
		//removing the class with lower priority because it is stored 
		//at a higher index
		for (int i = 0; i < allSchedules.get(k).size() - 1; i++) {
			String lastEntry = allSchedules.get(k).get(lastIndex).time;
			if (lastEntry.equals(allSchedules.get(k).get(lastIndex - mover).time)) {
				holdConflicts.add(allSchedules.get(k).remove(lastIndex));
					lastIndex = lastIndex - 1;
					mover = 1;
					i--;
				} else {
					lastIndex--;
				}
			}
		//adds empty to conflict list if there is no conflict list to 
		//facilitate organization and indices
		if (holdConflicts.size() == 0) {
			holdConflicts.add(new entry (" ", " ", 0, 0));
		}
		conflictList.add(holdConflicts);	
	}
	//end of conflicts
	
	//method to find best schedule
	findBest(allSchedules, conflicts, conflictList);

	
	}
}
