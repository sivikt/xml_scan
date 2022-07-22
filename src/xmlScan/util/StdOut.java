package xmlScan.util;

import java.io.PrintStream;

/**
 * Util functions to print messages to standard output.
 *
 * @author Serj Sintsov
 */
public class StdOut {

    private final static PrintStream OUT = System.out;

    /**
     * Prints messages to std output.
     */
    public static void printInfoMsg( String msg )
    {
        OUT.println( msg );
    }

    /**
     * Prints formatted messages to std output using
     * {@link String#format(String, Object...)}.
     */
    public static void printInfoMsg( String msgTpl, Object params )
    {
        OUT.println( String.format(msgTpl, params) );
    }

}
