package xmlScan.tracker;

import xmlScan.worker.results.ResultsStorage;

import java.io.File;

/**
 * Implementing this interface your class will be able to process files.
 * Tracker manages the process of handling xml files using performers.
 * 
 * @see xmlScan.worker.Worker
 *
 * @author Serj Sintsov
 */
public interface TrackerContext
{
	/**
	 * Returns the file to be processed.
	 */
	File nextFile();

    /**
     * Total files count.
     */
    int filesCount();
	
	/**
	 * Provides a shared results storage.
	 */
	ResultsStorage<?> resultsStorage();

}
