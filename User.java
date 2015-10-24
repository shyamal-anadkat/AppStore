import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The User class represents a single user registered at the app store. 
 *
 * Bugs: none known
 *
 * @author       Shyamal Anadkat
 * @version      1.0
 */
public class User {

	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String country;
	private String type; 
	List<App> downloadedApps;
	List<App> uploadedApps;
	/**
	 * Constructor for single user.
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param country
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public User(String email, String password, String firstName,
			String lastName, String country, String type)
					throws IllegalArgumentException {

		List<String> check = new ArrayList<String>
		(Arrays.asList(email,password,firstName,lastName,country,type));
		if (check.size()!=6) {
			throw new IllegalArgumentException("Fields missing.");
		}
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country; 
		this.type = type; 
		downloadedApps = new ArrayList<App>();
		uploadedApps = new ArrayList<App>();
	}
	/**
	 * Gets user's unique email id 
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Verifies user's password entered.
	 * @param testPassword
	 * @return true is password matches
	 */
	public boolean verifyPassword(String testPassword) {
		if (password.equals(testPassword)) {
			return true; 
		}
		else {
			return false;
		}
	}
	/**
	 * Gets user's first name.
	 * @return
	 */
	public String getFirstName() {
		return this.firstName;
	}
	/**
	 * Gets user's last name.
	 * @return
	 */
	public String getLastName() {
		return this.lastName; 

	}
	/**
	 * Gets user's country.
	 * @return
	 */
	public String getCountry() {
		return this.country; 
	}
	/**
	 * Checks is user is developer
	 * @return true if user is developer
	 */
	public boolean isDeveloper() {
		String dev = "developer";
		if (this.type.equals(dev.toLowerCase())) {
			return true; 
		}
		else {
			return false;
		}
	}
	/**
	 * Subsribes user as developer
	 */
	public void subscribeAsDeveloper() {
		// Registers user as a developer at the AppStore 
		this.type = "developer";
	}
	/**
	 * Downloads an App and adds it to downloaded app list. 
	 * @param app
	 */
	public void download(App app) { 
		this.downloadedApps.add(app);
	}
	/**
	 * Adds app to the uploaded list of app for the user.
	 * @param app
	 */
	public void upload(App app) {
		this.uploadedApps.add(app);
	}
	/**
	 * Gets all downloaded apps by user 
	 * @return a list of downloaded apps
	 */
	public List<App> getAllDownloadedApps() {
		return downloadedApps;
	}
	/**
	 * Gets all uploaded apps by user
	 * @return a list of uploaded apps
	 */
	public List<App> getAllUploadedApps() {
		return uploadedApps;
	}

}

