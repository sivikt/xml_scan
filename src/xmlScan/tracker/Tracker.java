package xmlScan.tracker;

/**
 * Implementing this interface your class will be able to process files.
 *
 * @see TrackerException
 *
 * @author Serj Sintsov
 */
public interface Tracker extends Runnable
{
	/**
	 * Start tracker
	 * @throws TrackerException if there are starting issues
	 */
	void start() throws TrackerException;

}
