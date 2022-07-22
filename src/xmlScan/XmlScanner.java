package xmlScan;

import xmlScan.tracker.MultithreadXmlDocsTracker;
import xmlScan.tracker.Tracker;
import xmlScan.tracker.TrackerException;

/**
 * Start point.
 * 
 * @author Serj Sintsov
 */
public class XmlScanner
{
    public static void main( String[] args )
    {
        try
        {
            Tracker tracker = new MultithreadXmlDocsTracker();
            Thread trackerThread = new Thread( tracker );
            trackerThread.start();
        }
        catch ( TrackerException e )
        {
            e.printStackTrace(System.err);
        }
    }

}
