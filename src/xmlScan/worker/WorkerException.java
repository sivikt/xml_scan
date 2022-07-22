package xmlScan.worker;

/**
 * {@link Worker} throws this exception during its work
 *
 * @see Worker
 * 
 * @author Serj Sintsov
 */
public class WorkerException extends Exception
{
    private static final long serialVersionUID = -44285740731702181L;

    public WorkerException(String msg)
    {
        super( msg );
    }

    public WorkerException( String msgTpl, Object... params )
    {
        this( null, msgTpl, params );
    }

    public WorkerException( Throwable cause, String msgTpl, Object... params)
    {
        super( String.format(msgTpl, params), cause );
    }

}
