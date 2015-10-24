import java.io.*;
import java.util.*;


public class Test {


	public static void main(String[] args) throws FileNotFoundException{


		File file = new File("appData.txt");
		Scanner input = new Scanner(file);
		List<String> myList = null;
		while(input.hasNext()){
			myList = new ArrayList<String>(Arrays.asList(input.nextLine().split(","))); 
			//set as app 1 and app id 
			// define all developers S
			System.out.println(myList);

		}

	}
}


