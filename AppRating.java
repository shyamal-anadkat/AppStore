

/**
 * Helper class to store the app rating given by a user.
 *
 * Bugs: none known
 *
 * @author       Shyamal Anadkat
 * @version      1.0
 */
public class AppRating {	
	private App app;
	private User user;
	private short rating;

	public AppRating(App app, User user, short rating) {
		this.app = app;
		this.user = user;
		this.rating = rating;
	}
	public App getApp() {
		return this.app;
	}

	public User getUser() {
		return this.user;
	}

	public short getRating() {
		return this.rating;
	}
}

