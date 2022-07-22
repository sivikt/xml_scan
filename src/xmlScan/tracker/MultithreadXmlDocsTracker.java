package xmlScan.tracker;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import xmlScan.tracker.config.PropsXmlDocsTrackerCfg;
import xmlScan.tracker.config.TrackerConfigException;
import xmlScan.tracker.config.XmlDocsTrackerConfig;
import xmlScan.worker.*;
import static xmlScan.util.StdOut.printInfoMsg;

/**
 * Implementation of {@link Tracker} which manages the process of handling xml
 * files using performers {@link xmlScan.worker.Worker}.
 * Tracker reads configuration from property file, creates needed number of
 * tasks performers and provides them with resources they need.
 * 
 * @see Tracker
 * @see xmlScan.worker.RunnableCyclicWorker
 *
 * @author Serj Sintsov
 */
public class MultithreadXmlDocsTracker implements Tracker, Warden
{
    private final static String BUNDLE_NAME = "xmlScan";

    private AtomicBoolean isWorking;
    private Semaphore workingWorkers;

    private XmlDocsTrackerConfig config;
    private TrackerContext trackerCtx;

    public MultithreadXmlDocsTracker() throws TrackerException
    {
        initConfig();
        isWorking = new AtomicBoolean( false );
    }

    private void initConfig() throws TrackerException {
        try
        {
            config = PropsXmlDocsTrackerCfg.createConfig(BUNDLE_NAME);
        }
        catch ( TrackerConfigException e )
        {
            throw new TrackerException("Tracker configuration error", e );
        }
    }

    @Override
    public void run()
    {
        try
        {
            start();
        }
        catch ( TrackerException e )
        {
            printInfoMsg( e.getMessage() );
        }
    }

    @Override
    public void start() throws TrackerException
    {
        if ( !isWorking.compareAndSet( false, true ) )
            throw new TrackerException( "Tracker is already working!" );

        trackerCtx = ConcurrentXmlDocsTrackerContext.createContext(config);

        try
        {
            RunnableCyclicWorker worker = new FindByXPathWorker( this, trackerCtx, config.getTargetXPath() );


            int threadsNumber = config.getThreadsNum() > trackerCtx.filesCount()
                    ? trackerCtx.filesCount()
                    : config.getThreadsNum();

            workingWorkers = new Semaphore(threadsNumber - 1);

            for ( int i = 0; i < threadsNumber; i++ )
                new Thread( worker ).start();

            wait4Results();
        }
        catch ( WorkerException e )
        {
            throw new TrackerException( "Tracker startup is failed. Worker " +
                    "creation error", e );
        }
        catch (Exception e) {
            throw new TrackerException( "Tracker startup is failed", e );
        }
        finally
        {
            isWorking.set( false );
        }
    }

    /**
     * This method waits until all performers
     * finished their work and processes a findings.
     *
     * @throws TrackerException
     */
    private void wait4Results() throws TrackerException
    {
        try
        {
            workingWorkers.acquire();

            // print results
            printInfoMsg( "processing is finished!" );

            if ( trackerCtx.resultsStorage().resultsCount() != 0 )
            {
                printInfoMsg( "\nresults:" );
                trackerCtx.resultsStorage().print(System.out);
                printInfoMsg("Total count %d", trackerCtx.resultsStorage().resultsCount());
            }
            else
                printInfoMsg( "no results..." );
        }
        catch ( InterruptedException e )
        {
            throw new TrackerException( "Tracker thread has been interrupted", e );
        }
    }

    @Override
    public void signFinished() {
        workingWorkers.release();
    }

}
