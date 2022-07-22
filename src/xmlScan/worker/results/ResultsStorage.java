package xmlScan.worker.results;

import java.io.OutputStream;
import java.util.Collection;

/**
 * {@link xmlScan.worker.Worker} performs some work and return
 * results. Worker uses generic ResultsStorage to add its results to.
 *
 * Each type of Workers has its own results storage type that implements
 * {@link ResultsStorage<T>} interface.
 *
 * @param <T> the type of items which can be stored to the storage instance
 *
 * @see Collection
 * @see xmlScan.worker.Worker
 *
 * @author Serj Sintsov
 */
public interface ResultsStorage<T>
{
    /**
     * Adds collection of T objects to the storage.
     */
    void addAll( Collection<T> results );

    /**
     * Adds object of a type T to a storage.
     */
    void add( T result );

    /**
     * Prints results to the specified output stream.
     */
    void print( OutputStream out );

    /**
     * Returns results count.
     */
    int resultsCount();

}
