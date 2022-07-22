package xmlScan.worker;

import xmlScan.tracker.TrackerContext;

/**
 * If some class wants to be a worker it has to implement this interface.
 * Worker performs files processing in the specified tracker context and is
 * subject to the specified warden.
 *
 * @see Warden
 * @see TrackerContext
 *
 * @author sivikt
 */
public interface Worker
{
    /**
     * Worker starts processing by calling this method.
     * @throws WorkerException if something goes wrong
     */
    public void perform( Warden warden, TrackerContext context )
            throws WorkerException;

}
