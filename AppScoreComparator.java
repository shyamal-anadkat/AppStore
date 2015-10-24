import java.util.Comparator;


/**
 * Implements the Comparator interface and 
 * provides an implementation for the compare() function that 
 * compares the scores of two app objects and decides their ordering.
 *
 * Bugs: none known
 *
 * @author       Shyamal Anadkat
 * @version      1.0
 */
public class AppScoreComparator implements Comparator<App> {

	@Override
	public int compare(App app1, App app2) {
		if (app1.getAppScore() > app2.getAppScore()) {
			return -1;
		}
		if(app1.getAppScore() < app2.getAppScore()) {
			return 1;
		}
		else
			return app1.compareTo(app2);	

	}

}

