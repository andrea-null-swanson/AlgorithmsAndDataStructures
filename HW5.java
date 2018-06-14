import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*

  Author: Andrea Swanson
  Email: aswanson2016@my.fit.edu	
  Course: CSE 2010
  Section: 14
  Description:

 */

public class HW5{	
	
    public static void main(String[] args) throws FileNotFoundException {
    	
    	
    	SkipList<Entry> skipList = new SkipList<Entry>();
    	File fileIn = new File(args[0]);
    	Scanner in = new Scanner(fileIn);
    	while (in.hasNextLine()) {
    		String command = in.next();
    		//System.out.println();
    		if (command.equals("AddActivity")) {
    			int time = in.nextInt();
    			String activity = in.next();
    			skipList.put(time, activity);
    			System.out.print("AddActivity ");
    			System.out.printf("%08d", time);
    			System.out.println(" "+ activity);
    			
    		}else if (command.equals("RemoveActivity")) {
    			int time = in.nextInt();
    			System.out.print("Remove Activity ");
        		System.out.printf("%08d: ", time);        
    			System.out.println(skipList.removal(time));
    		}else if (command.equals("GetActivity")) {
    			int time = in.nextInt();
    			System.out.print("GetActivity ");
        		System.out.printf("%08d: ",time);        
    			if (skipList.get(time)== null) {
    				System.out.println("none");
    			}else {
    			System.out.println(skipList.get(time).getElement().value);
    			}
    		
    		}else if (command.equals("GetActivitiesBetweenTimes")) {
    			int timeFloor = in.nextInt();
    			int timeCeiling = in.nextInt();
    			System.out.print(command + " ");
				System.out.printf("%08d %08d ", timeFloor, timeCeiling);
				//System.out.printf("%08d", timeCeiling);


    			ArrayList<Entry> list = skipList.subMap(timeFloor, timeCeiling);
    			for (Entry e : list) {
    				System.out.printf("%08d", e.key);
    				System.out.print(": " + e.value + " ");
    			}
    			System.out.println();
    			
    			//submap.......
    		}else if (command.equals("GetActivitiesForOneDay")) {
    			int date = in.nextInt();
    			int minTime = date*10000;
    			int maxTime = (date + 1) * 10000;
    			ArrayList<Entry> allEntries = skipList.subMap(minTime, maxTime);
        		System.out.print(command + " ");
        		System.out.printf("%04d ", date);        	
    	        for (Entry e : allEntries) {
            		System.out.printf("%08d: ", e.key);        
    	        	System.out.print(e.value);
    	        }		
        		System.out.println();
    			//submap.............
    		}else if (command.equals("GetActivitiesFromEarlierInTheDay")) {
    			int currentTime = in.nextInt();
    			System.out.print("GetActivitiesFromEarlierInTheDay ");
    			System.out.printf("%08d ", currentTime);
    			int subtract = currentTime % 10000;
    			int earliest = currentTime - subtract;
    			ArrayList<Entry> allEntries = skipList.subMap(earliest, currentTime);
    			if (allEntries.size() == 0) {
    				System.out.print("none");
    			}
    			for (Entry e : allEntries) {
            		System.out.printf("%08d: ", e.key);       
    				System.out.print(e.value + " ");
    			}
    			System.out.println();
    			 //submap..............
    		}else if (command.equals("PrintSkipList")) {
    			System.out.println("Print Skip List");
    			skipList.printStack();
    		}
    		else {
    			System.out.println("invalid input: " + command);
    		}
    	}

    }

}