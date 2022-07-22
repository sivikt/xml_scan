package xmlScan.tracker;

/**
 * {@link Tracker} throws this exception when something goes wrong.
 * 
 * @author Serj Sintsov
 */
public class TrackerException extends Exception 
{
	private static final long serialVersionUID = -8751370018063205822L;
	
	public TrackerException( String msg )
	{
		super( msg );
	}

    public TrackerException( String msg, Throwable cause )
    {
        super( msg, cause );
    }
}
