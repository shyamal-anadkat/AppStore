import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * It stores the list of all the apps and the users at the app store. 
 * Also implements functionalities to display rankings of top apps 
 * in different categories.
 *
 * Bugs: none known
 *
 * @author       Shyamal Anadkat (2015)
 * @version      1.0
 */
public class AppStoreDB {

	private List<String> categories;
	private List<User> users;
	private List<App> apps;

	public AppStoreDB() {
		this.users = new ArrayList<User>();
		this.apps = new ArrayList<App>();
		this.categories = new ArrayList<String>();
	}
	/**
	 * Adds user to the list of users 
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param country
	 * @param type
	 * @return the new user 
	 * @throws IllegalArgumentException
	 */
	public User addUser(String email, String password, String firstName,
			String lastName, String country, String type)
					throws IllegalArgumentException {

		User newUser = null; 
		try {
			newUser = new User(email.toLowerCase()
					,password,firstName,lastName,country,type);
		}
		catch(IllegalArgumentException iae) {
			System.out.println("Failed to add user. Missing/bad fields.");
		}
		for (int i = 0; i < users.size(); i++) {
			if(users.get(i).getEmail().toLowerCase().
					equals(email.toLowerCase()))
			{
				throw new IllegalArgumentException
				("Failed to add user. User already exists.");
			}
		}
		this.users.add(newUser);
		return newUser;
	}
	/**
	 * Adds category to a list of categories.
	 * @param category
	 */
	public void addCategory(String category) {
		this.categories.add(category);
	}
	/**
	 * Gets categories from a list of categories.  
	 * @return
	 */
	public List<String> getCategories() {
		return this.categories;	
	}
	/**
	 * Finds user by unique email. 
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email) {
		//Returns the user object for a given email. If not found,
		//returns null.
		for (int i = 0; i < users.size(); i++) {
			if(email.equals(users.get(i).getEmail())) {
				return users.get(i);
			}
		}
		return null;	
	}

	/**
	 * Finds a particular app by App ID. 
	 * @param appId
	 * @return
	 */
	public App findAppByAppId(String appId) {
		for(int i = 0; i < apps.size(); i++) {
			if(apps.get(i).getAppId().equals(appId)) {
				return apps.get(i);
			}
		}
		return null;
	}
	/**
	 * Logins the user with correct email and password. 
	 * @param email
	 * @param password
	 * @return
	 */
	public User loginUser(String email, String password) {
		for (int i = 0; i < users.size(); i++) {
			if(users.get(i).getEmail().equals(email)) {
				if(users.get(i).verifyPassword(password)) {
					return users.get(i);
				}
			}
		}
		return null;
	}
	/**
	 * Uploads a new App to the AppStore Database.
	 * @param uploader
	 * @param appId
	 * @param appName
	 * @param category
	 * @param price
	 * @param timestamp
	 * @return the new app.
	 * @throws IllegalArgumentException
	 */
	public App uploadApp(User uploader, String appId, String appName,
			String category, double price, 
			long timestamp) throws IllegalArgumentException {

		App newApp = null;
		try {
			newApp = new App(uploader,appId,appName,category,price,timestamp);
		}
		catch(IllegalArgumentException iae) {
			System.out.println("Failed to upload App. ");
		}

		Iterator<String> allCategories = categories.iterator();
		//Check is app already exists. 
		for(int i = 0; i < apps.size(); i++) {
			//Input validation : existing AppID
			if(appId.equals(apps.get(i).getAppId())){
				throw new IllegalArgumentException
				("Failed uploading.App already exits.");
			}
		}
		//Input validation : negative price. 
		if(price < 0.0) {
			throw new IllegalArgumentException("Price cannot be negative.");
		}
		//Checks is uploader is developer.
		if(!uploader.isDeveloper()) {
			throw new IllegalArgumentException
			("You need developer permission.");
		}
		int k = 0;
		while(allCategories.hasNext()) {
			if(allCategories.next().equals(category)) {
				k++;
			}
		}
		if (k==0) {
			throw new IllegalArgumentException("Category does not exist.");
		}
		apps.add(newApp); 
		uploader.upload(this.findAppByAppId(appId));
		return newApp;
	}
	/**
	 * Downloads an app for a user. 
	 * @param user
	 * @param app
	 */
	public void downloadApp(User user, App app) {
		if(hasUserDownloadedApp(user,app)) {
			throw new IllegalArgumentException("App already downloaded.");
		}
		else
			app.download(user);
	}
	/**
	 * Allows user to rate an app. 
	 * @param user
	 * @param app
	 * @param rating
	 */
	public void rateApp(User user, App app, short rating) {
		if(!hasUserDownloadedApp(user, app)) {
			throw new IllegalArgumentException
			("You need to download the app before rating.");
		}
		if(rating < 1|| rating > 5) {
			throw new IllegalArgumentException
			("Rating should be between 1-5");
		}
		else 
			app.rate(user,rating);
	}
	/**
	 * Checks is user has downloaded a particular app.
	 * @param user
	 * @param app
	 * @return true if user has downloaded that app. 
	 */
	public boolean hasUserDownloadedApp(User user, App app) {
		Iterator<App> dwnApps = user.getAllDownloadedApps().iterator();
		while(dwnApps.hasNext()) {
			if (dwnApps.next().getAppId().equals(app.getAppId())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Gets the top free apps - in specified or across all categories. 
	 * @param category
	 * @return
	 */
	public List<App> getTopFreeApps(String category) {
		/* Returns a list of top free apps in a given category. 
		 * If no category is passed, then it returns top free apps 
		 * across all categories.
	       To calculate the top free apps, the following algorithm is used:
	       -- Apps are then sorted by their scores and the top apps 
	       are computed
	       -- Apps with same scores are ordered by their timestamp. 
	       An older app is ranked higher than a new app.*/

		AppScoreComparator comparator = new AppScoreComparator();
		List<App> sortedApps = new ArrayList<App>();

		for(int i = 0; i<apps.size(); i++) {
			if(apps.get(i).getPrice()==0.0 ) {
				sortedApps.add(apps.get(i));
			}
		}
		if(category == null) {
			Collections.sort(sortedApps, comparator);
			return sortedApps;
		}
		List<App> list = new ArrayList<App>();

		for (int j = 0; j < sortedApps.size(); j++) {
			if(sortedApps.get(j).getCategory().equals(category)) {
				list.add(sortedApps.get(j));
			}
		}
		Collections.sort(list,comparator);
		return list;

	}
	/**
	 * Gets top paid apps - without and with a specified category. 
	 * @param category
	 * @return
	 */
	public List<App> getTopPaidApps(String category) {
		AppScoreComparator comparator = new AppScoreComparator();
		List<App> sortedApps = new ArrayList<App>();	

		for(int i = 0; i<apps.size(); i++) {
			if(apps.get(i).getPrice() > (double)0.0) {				
				sortedApps.add(apps.get(i));				
			}
		}
		List<App> list = new ArrayList<App>();
		if(category == null) {
			Collections.sort(sortedApps, comparator);
			return sortedApps;
		}
		else 
		{
			for (int i = 0; i < sortedApps.size(); i++) 
			{
				if(sortedApps.get(i).getCategory().equals(category)) {
					list.add(sortedApps.get(i));
				}
			}
		}
		Collections.sort(list,new AppScoreComparator());
		return list;
	}
	/**
	 * Gets most recent apps using timestamp comparison.
	 * @param category
	 * @return a list of apps ordered by their launch dates. 
	 * If no category is specified,
		   return most recent apps across all categories.
	 */
	public List<App> getMostRecentApps(String category) {
		//Returns a list of apps ordered by their launch dates. 
		//If no category is specified,
		//return most recent apps across all categories.
		List<App> sortedApps = new ArrayList<App>();
		sortedApps = apps;

		if(category == null) {
			Collections.sort(sortedApps);
			return sortedApps;		
		}
		List<App> list1 = new ArrayList<App>();
		for (int i = 0; i < sortedApps.size(); i++) {
			if(sortedApps.get(i).getCategory().
					toLowerCase().equals(category.toLowerCase())) {
				list1.add(apps.get(i));
			}
		}
		Collections.sort(list1);
		return list1;
	}
} 



