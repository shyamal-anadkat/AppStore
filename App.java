import java.util.*;
import java.io.*;


/**
 * Represents a single app at the app store and holds all the attributes
 * that are associated with a typical app.
 *
 * Bugs: none known
 *
 * @author       Shyamal Anadkat (2015)
 * @version      1.0
 */
public class App implements Comparable<App> {

	//Private variables
	private String appId;
	private String appName;
	private String category;
	private double price; 
	private long uploadTimestamp;
	private User developer; 
	private int downloads;
	private boolean appRated = true;
	// List of rated Apps of the type AppRating 
	private List<AppRating> appRating; 
	/**
	 * Constructs an app object with specified attributes. 
	 * @param developer
	 * @param appId
	 * @param appName
	 * @param category
	 * @param price
	 * @param uploadTimestamp
	 * @throws IllegalArgumentException
	 */
	public App(User developer, String appId, String appName, String category,
			double price, long uploadTimestamp) 
					throws IllegalArgumentException {

		if(developer.equals(null) || appId.equals(null) 
				|| appName.equals(null) 
				|| category.equals(null)) {
			throw new IllegalArgumentException();
		}
		this.appName =appName; 
		this.category = category;
		this.price = price; 
		this.uploadTimestamp = uploadTimestamp; 
		this.appId = appId;
		this.developer = developer; 
		this.downloads = 0;
		appRating = new ArrayList<AppRating>();
	}
	/**
	 * Gets developer(user) object of given app. 
	 * @return
	 */
	public User getDeveloper() {
		return this.developer;
	}
	/**
	 * Gets the app ID for this app. 
	 * @return
	 */
	public String getAppId() {
		return this.appId;
	}
	/**
	 * Gets the name of this app. 
	 * @return
	 */
	public String getAppName() {
		return this.appName; 
	}
	/**
	 * Gets category to which this app belongs. 
	 * @return
	 */
	public String getCategory() {
		return this.category; 
	}
	/**
	 * Gets the price for this app. 
	 * @return
	 */
	public double getPrice() {
		if(price<0.0) {
			throw new IllegalArgumentException
			("Negative prices not allowed.");
		}
		else 
			return this.price;
	}
	/**
	 * Gets the time stamp at which app was uploaded 
	 * @return
	 */
	public long getUploadTimestamp() {
		if(uploadTimestamp<0) {
			throw new IllegalArgumentException
			("Negative timestamps not allowed.");
		}
		else
			return this.uploadTimestamp;
	}
	/** 
	 * Increments download count of this App. 
	 * @param user
	 */
	public void download(User user) {		
		user.download(this);
		this.downloads++;
	}
	/**
	 * Assigns rating to app given by a user. 
	 * @param user
	 * @param rating
	 * @throws IllegalArgumentException
	 */
	public void rate(User user, short rating) throws IllegalArgumentException{
		Iterator<AppRating> ratedApps = appRating.iterator(); 
		//checks is the user has already rated the app
		while(ratedApps.hasNext()) {
			AppRating thisApp = ratedApps.next();
			if(thisApp.getApp().equals(this)) {
				if(thisApp.getUser().equals(user)){
					throw new IllegalArgumentException("App already rated.");	
				}
			}
		}
		appRating.add(new AppRating(this,user,rating));
		appRated = true;
	}
	/**
	 * Gets the total downloads for the particular app. 
	 * @return total number of downloads. 
	 */
	public long getTotalDownloads() {
		return downloads;
	}
	/**
	 * Gives the average rating for the rated app. 
	 * @return the avg. rating. 
	 */
	public double getAverageRating() {
		// sum of ratings / no of ratings given 
		double avgRating = 0; 
		double sum = 0; 
		int numRatings = this.appRating.size();
		if (numRatings == 0) {
			return 0.0; 
		}
		for(int i=0; i < numRatings;i++) {
			sum = sum + this.appRating.get(i).getRating();
		}
		if (!appRated) {
			return 0.0;
		}
		avgRating = (double) sum /numRatings;
		return avgRating;
	}
	/**
	 * Calculates the total revenue earned by app based on total downloads.
	 * @return the total revenue for the app.
	 */
	public double getRevenueForApp() {
		//Total revenue : (total downloads * price of app) – 30% cut imposed 
		double revenue = getTotalDownloads()*getPrice();
		double totalRevenue = 0.7*revenue;
		return totalRevenue;
	}
	/**
	 * Calculates the score for app using given formula.
	 * @return the app score. 
	 */
	public double getAppScore() {
		double appScore = this.getAverageRating() 
				* Math.log(1 + this.getTotalDownloads());
		return appScore; 
	}
	/**
	 *  Compares two apps based on upload timestamp. 
	 */
	@Override
	public int compareTo(App otherApp) {
		if (otherApp.uploadTimestamp< this.uploadTimestamp) {
			return -1;
		}
		else if (otherApp.uploadTimestamp > this.uploadTimestamp) {
			return 1; 
		}
		else {
			return 0;
		}
	}
}


