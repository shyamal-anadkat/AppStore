///////////////////////////////////////////////////////////////////////////////
//Title:            AppStore
//Files:            AppStore.java,AppStoreDB.java,User.java,
//                  App.java,AppScoreComparator.java,AppRating.java
//Semester:         Jim Skrentny Fall 2015
//
//Author:           Shyamal Anadkat
//Email:            anadkat@wisc.edu
//CS Login:         shyamal
//Lecturer's Name:  Jim Skrentny 
//Section:          LEC 002
//////////////////////////////////////////////////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Represents an AppStore. Mimics the basic functionalities of an app store.
 *
 * Bugs: none known
 *
 * @author    Shyamal Anadkat
 * @version   1.0
 */
public class AppStore {

	// Initializing App Store Database 
	private static AppStoreDB appStoreDB = new AppStoreDB();
	private static User appUser = null;
	private static Scanner scanner = null;

	/**
	 * Main method for AppStore. 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length < 4) {			
			System.err.println("Bad invocation! Correct usage: "
					+ "java AppStore <UserDataFile> <CategoryListFile> "
					+ "<AppDataFile> <AppActivityFile>");
			System.exit(1);
		}
		boolean didInitialize = 
				initializeFromInputFiles(args[0], args[1], args[2], args[3]);

		if(!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}
		System.out.println("Welcome to the App Store!\n"
				+ "Start by browsing the top free and the top paid apps "
				+ "today on the App Store.\n"
				+ "Login to download or upload your favorite apps.\n");
		processUserCommands();
	}
	/**
	 * Initializes given txt data files, processes them, 
	 * and sets respective data structures.
	 * @param userDataFile
	 * @param categoryListFile
	 * @param appDataFile
	 * @param appActivityFile
	 * @return true if initialized; false if failed. 
	 */
	private static boolean initializeFromInputFiles(String userDataFile, String 
			categoryListFile, String appDataFile, String appActivityFile)   {

		//Initializing user data
		try {
			File userData = new File(userDataFile);
			scanner = new Scanner(userData);
		}
		catch (FileNotFoundException fnfe1) {
			System.out.println("File "+userDataFile+"+ not found");
			return false;
		}
		try {
			while(scanner.hasNextLine()) {
				String nextToken = scanner.nextLine();
				List<String> list = new ArrayList<String>
				(Arrays.asList(nextToken.split(",")));
				appStoreDB.addUser(list.get(0),list.get(1),list.get(2),
						list.get(3),list.get(4),list.get(5));	
			} 
			scanner.close();
		}
		catch(Exception e) {
			return false; 
		}
		//Initializing categories 
		try {
			File categoryList = new File(categoryListFile);
			scanner = new Scanner(categoryList);
		}
		catch (FileNotFoundException fnfe2) {
			System.out.println("File "+categoryListFile+"+ not found");
			return false;
		}
		try{
			while(scanner.hasNextLine()) {
				String nextToken = scanner.nextLine();
				appStoreDB.addCategory(nextToken);
			}
			scanner.close();
		}
		catch(Exception e) {
			return false;
		}

		// Initializing appDataFile 
		try {
			File appData = new File(appDataFile);
			scanner = new Scanner(appData);
		}
		catch(FileNotFoundException fnfe3) {
			System.out.println("File "+appDataFile+"+ not found");
			return false;
		}
		try {
			while(scanner.hasNextLine()) {
				String nextToken = scanner.nextLine();
				String[] ar = nextToken.split(",");
				if(ar.length!=6) {
					return false;
				}
				double d = Double.parseDouble(ar[4]);
				long l = Long.parseLong(ar[5]);		
				appStoreDB.uploadApp
				(appStoreDB.findUserByEmail(ar[0]), ar[1], ar[2],ar[3],d,l);
			}
			scanner.close();
		}
		catch(Exception e) {
			return false;
		}
		//Initializing App Activity file 
		try {
			File appActivity = new File(appActivityFile);
			scanner = new Scanner(appActivity);
		}
		catch(FileNotFoundException fnfe4) {
			System.out.println("File "+appActivityFile+"+ not found");
			return false;
		}
		try{
			while(scanner.hasNextLine()) {
				String nextToken = scanner.nextLine().trim();
				List<String> list = new ArrayList<String>
				(Arrays.asList(nextToken.split(",")));
				if (list.size()!=4 && list.size()!=3) {
					return false;
				}
				String char1 = "d".toLowerCase();
				String char2 = "r".toLowerCase();

				if(list.get(0).equals(char1)) {
					appStoreDB.downloadApp
					(appStoreDB.findUserByEmail(list.get(1)),
							appStoreDB.findAppByAppId(list.get(2)));
				}
				if(list.get(0).equals(char2)) {
					Short rating = Short.parseShort(list.get(3));
					appStoreDB.rateApp
					(appStoreDB.findUserByEmail(list.get(1)),
							appStoreDB.findAppByAppId(list.get(2)),rating);
				}
			}
		}
		catch(Exception e) {
			return false;
		}
		scanner.close();
		return true; 
	}
	/**
	 * Helps user with varied functionalities of a basic app store. 
	 */
	private static void processUserCommands() {
		scanner = new Scanner(System.in);
		String command = null;		
		do {
			if (appUser == null) {
				System.out.print("[anonymous@AppStore]$ ");
			} else {
				System.out.print("[" + appUser.getEmail().toLowerCase() 
						+ "@AppStore]$ ");
			}
			command = scanner.next();
			switch(command.toLowerCase()) {
			case "l":
				processLoginCommand();
				break;

			case "x": 
				processLogoutCommand();
				break;

			case "s":
				processSubscribeCommand();
				break;

			case "v":
				processViewCommand();
				break;

			case "d":
				processDownloadCommand();
				break;

			case "r":
				processRateCommand();
				break;

			case "u":
				processUploadCommand();
				break;

			case "p":
				processProfileViewCommand();
				break;								

			case "q":
				System.out.println("Quit");
				break;
			default:
				System.out.println("Unrecognized Command!");
				break;
			}
		} while (!command.equalsIgnoreCase("q"));
		scanner.close();
	}
	/**
	 * Helps user/developer to login the app store. 
	 */
	private static void processLoginCommand() {
		if (appUser != null) {
			System.out.println("You are already logged in!");
		} else {
			String email = scanner.next();
			String password = scanner.next();
			appUser = appStoreDB.loginUser(email, password);
			if (appUser == null) {
				System.out.println("Wrong username / password");
			}
		}
	}
	/**
	 * Helps user log out of app store. 
	 */
	private static void processLogoutCommand() {
		if (appUser == null) {
			System.out.println("You are already logged out!");
		} else {
			appUser = null;
			System.out.println("You have been logged out.");
		}
	}
	/**
	 * Helps user subscribe as a developer. 
	 */
	private static void processSubscribeCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			if (appUser.isDeveloper()) {
				System.out.println("You are already a developer!");
			} else {
				appUser.subscribeAsDeveloper();
				System.out.println("You have been promoted as developer");
			}
		}
	}
	/**
	 * View command with varied functionalities. 
	 */
	private static void processViewCommand() {
		String restOfLine = scanner.nextLine();
		Scanner in = new Scanner(restOfLine);
		String subCommand = in.next();
		int count;
		String category;
		switch(subCommand.toLowerCase()) {
		case "categories":
			System.out.println("Displaying list of categories...");
			List<String> categories = appStoreDB.getCategories();
			count = 1;
			for (String categoryName : categories) {
				System.out.println(count++ + ". " + categoryName);
			}
			break;
		case "recent":				
			category = null;
			if (in.hasNext()) {
				category = in.next();
			} 
			displayAppList(appStoreDB.getMostRecentApps(category));				
			break;
		case "free":
			category = null;
			if (in.hasNext()) {
				category = in.next();
			}
			displayAppList(appStoreDB.getTopFreeApps(category));
			break;
		case "paid":
			category = null;
			if (in.hasNext()) {
				category = in.next();
			}
			displayAppList(appStoreDB.getTopPaidApps(category));
			break;
		case "app":
			String appId = in.next();
			App app = appStoreDB.findAppByAppId(appId);
			if (app == null) {
				System.out.println("No such app found with the given app id!");
			} else {
				displayAppDetails(app);
			}
			break;
		default: 
			System.out.println("Unrecognized Command!");
		}
		in.close();
	}
	/**
	 * Helps download an app. 
	 */
	private static void processDownloadCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			String appId = scanner.next();
			App app = appStoreDB.findAppByAppId(appId);
			if (app == null) {
				System.out.println("No such app with the given id exists. "
						+ "Download command failed!");
			} else {
				try {
					appStoreDB.downloadApp(appUser, app);
					System.out.println("Downloaded App " + app.getAppName());
				} catch (Exception e) {				
					System.out.println("Something went wrong. "
							+ "Download command failed!");
				}
			}
		}

	}
	/**
	 * Helps rate an app. 
	 */
	private static void processRateCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			String appId = scanner.next();
			App app = appStoreDB.findAppByAppId(appId);
			if (app == null) {
				System.out.println("No such app with the given id exists. "
						+ "Rating command failed!");
			} else {
				try {
					short rating = scanner.nextShort();
					appStoreDB.rateApp(appUser, app, rating);
					System.out.println("Rated app " + app.getAppName());
				} catch (Exception e) {
					System.out.println("Something went wrong. "
							+ "Rating command failed!");
				}
			}
		}

	}
	/**
	 * Helps developer upload an app. 
	 */
	private static void processUploadCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			String appName = scanner.next();
			String appId = scanner.next();
			String category = scanner.next();
			double price = scanner.nextDouble();
			long uploadTimestamp = Instant.now().toEpochMilli();
			try {
				appStoreDB.uploadApp(appUser, appId, appName, category, 
						price, uploadTimestamp);
			} catch (Exception e) {
				System.out.println("Something went wrong. "
						+ "Upload command failed!");
			}
		}
	}

	/**
	 * Helps view the profile of a user. 
	 */
	private static void processProfileViewCommand() {		
		String restOfLine = scanner.nextLine();
		Scanner in = new Scanner(restOfLine);
		String email = null;
		if (in.hasNext()) {
			email = in.next();
		}
		if (email != null) {
			displayUserDetails(appStoreDB.findUserByEmail(email));
		} else {
			displayUserDetails(appUser);
		}
		in.close();

	}

	/**
	 * Displays list of apps.
	 * @param apps
	 */
	private static void displayAppList(List<App> apps) {
		if (apps.size() == 0) {
			System.out.println("No apps to display!");
		} else {
			int count = 1;
			for(App app : apps) {
				System.out.println(count++ + ". " 
						+ "App: " + app.getAppName() + "\t" 
						+ "Id: " + app.getAppId() + "\t" 
						+ "Developer: " + app.getDeveloper().getEmail());
			}	
		}
	}
	/**
	 * Displays app details. 
	 * @param app
	 */
	private static void displayAppDetails(App app) {
		if (app == null) {
			System.out.println("App not found!");
		} else {
			System.out.println("App name: " + app.getAppName());
			System.out.println("App id: " + app.getAppId());
			System.out.println("Category: " + app.getCategory());
			System.out.println("Developer Name: " 
					+ app.getDeveloper().getFirstName() + " " 
					+ app.getDeveloper().getLastName());
			System.out.println("Developer Email: " 
					+ app.getDeveloper().getEmail());
			System.out.println("Total downloads: " + app.getTotalDownloads());
			System.out.println("Average Rating: " + app.getAverageRating());

			// show revenue from app if the logged-in user is the app developer
			if (appUser != null && 
					appUser.getEmail()
					.equalsIgnoreCase(app.getDeveloper().getEmail())) {
				System.out.println("Your Revenue from this app: $" 
						+ app.getRevenueForApp());
			}

		}		
	}
	/**
	 * Displays user details. 
	 * @param user
	 */
	private static void displayUserDetails(User user) {		
		if (user == null) {
			System.out.println("User not found!");
		} else {
			System.out.println("User name: " + user.getFirstName() + " "
					+ user.getLastName());
			System.out.println("User email: " + user.getEmail());
			System.out.println("User country: " + user.getCountry());

			// print the list of downloaded apps
			System.out.println("List of downloaded apps: ");			
			List<App> downloadedApps = user.getAllDownloadedApps();
			displayAppList(downloadedApps);

			// print the list of uploaded app
			System.out.println("List of uploaded apps: ");
			List<App> uploadedApps = user.getAllUploadedApps();
			displayAppList(uploadedApps);

			// show the revenue earned, if current user is developer
			if (appUser!= null 
					&& user.getEmail().equalsIgnoreCase(appUser.getEmail()) 
					&& appUser.isDeveloper()) {
				double totalRevenue = 0.0;
				for (App app : uploadedApps) {
					totalRevenue += app.getRevenueForApp();
				}
				System.out.println("Your total earnings: $" + totalRevenue);
			}

		}
	}
}

