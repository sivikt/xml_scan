package xmlScan.worker;

import java.io.File;
import java.util.List;

import static xmlScan.util.StdOut.printInfoMsg;
import xmlScan.worker.results.ResultsStorage;
import xmlScan.tracker.TrackerContext;

/**
 * Abstract {@link Runnable} implementation of the {@link Worker}.
 * This class enables you to request files from {@link TrackerContext} and
 * notifies the {@link Warden} about work completion.
 * <p/>
 * Thread-safe abstract implementation. You can perform the same instance of
 * that worker in parallel on multiply threads. Keep it in mind when extending
 * this class.
 *
 * @param <T> the type of result that can be stored to {@link ResultsStorage}
 *            by this {@link Worker}
 *
 * @see Worker
 *
 * @author Serj Sintsov
 */
public abstract class RunnableCyclicWorker< T > implements Worker, Runnable
{
    protected TrackerContext trackerCtx;
    protected Warden warden;

    public RunnableCyclicWorker(Warden warden, TrackerContext trackerCtx)
    {
        this.warden = warden;
        this.trackerCtx = trackerCtx;
    }

    @Override
    public void run()
    {
        try
        {
            perform(warden, trackerCtx);
        }
        catch ( WorkerException e )
        {
            printInfoMsg("Worker failed due to error '%s'", e.getMessage());
            warden.signFinished();
        }
    }

    @Override
    public void perform(Warden warden, TrackerContext context)
            throws WorkerException
    {
        File file;
        List<T> results;

        try {
            while ( (file = trackerCtx.nextFile()) != null )
            {
                results = scanFile( file );
                ( (ResultsStorage<T>) trackerCtx.resultsStorage() ).addAll( results );
            }
        }
        finally
        {
            warden.signFinished();
        }
    }

    /**
     * Scans a file and receives some results. Must be overridden by subclasses.
     * @param src source file to process
     */
    protected abstract List<T> scanFile( File src ) throws WorkerException;

}
