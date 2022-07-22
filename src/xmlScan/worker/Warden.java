package xmlScan.worker;

/**
 * Controls {@link Worker}'s work.
 *
 * @see Worker
 *
 * @author Serj Sintsov
 */
public interface Warden {

    /**
     * {@link Worker} notifies the {@link Warden} that he has finished work.
     */
    void signFinished();

}
