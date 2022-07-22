package xmlScan.worker.results;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple starvation thread-safe implementation of the {@link ResultsStorage}
 * interface.
 *
 * This data type of results storage contains only unique strings
 * themselves and a number of each such string.
 *
 * Storage prints its contents to a specified output stream and sorts it in
 * DESC order by the number of unique string occurrences.
 * 
 * @author Serj Sintsov
 */
public class FindByXPathResultsStorage implements ResultsStorage<String>
{
    private final static String NONE_VALUE = "N/A";

    private Map<String, Integer> uniques;
    private int naValuesCount;
    private FindByXPathResultComparator comparator;

    public FindByXPathResultsStorage()
    {
        naValuesCount = 0;
        uniques = new HashMap<String, Integer>();
        comparator = new FindByXPathResultComparator();
    }

    @Override
    public void addAll( Collection<String> results )
    {
        if ( results == null || results.isEmpty() )
            add( null );
        else
            for ( String newKey : results ) addEntity( newKey );
    }

    @Override
    public synchronized void add( String result )
    {
        if ( result == null ) naValuesCount++;
        else addEntity( result );
    }

    private void addEntity( String entity )
    {
        Integer val = uniques.get( entity );

        if ( val != null) uniques.put( entity, val + 1 );
        else uniques.put( entity, 1 );
    }

    @Override
    public synchronized int resultsCount()
    {
        return uniques.size() + ( naValuesCount > 0 ? 1 : 0 );
    }

    @Override
    public synchronized String toString()
    {
        List<String> keys = Arrays.asList(
                                uniques.keySet().toArray(
                                        new String[uniques.size()] ) );

        Collections.sort( keys, comparator );

        StringBuilder buf = new StringBuilder();

        if ( naValuesCount > 0 )
            buf.append( String.format( "%s, %d\n", NONE_VALUE, naValuesCount ) );

        for ( String key : keys )
            buf.append( String.format( "\"%s\", %d\n", key, uniques.get( key ) ) );

        return buf.toString();
    }

    @Override
    public void print( OutputStream out )
    {
        PrintStream ps = new PrintStream( out );
        ps.println( toString() );
    }

    private class FindByXPathResultComparator implements Comparator<String>
    {
        @Override
        public int compare( String o1, String o2 )
        {
            Integer val1 = uniques.get( o1 );
            Integer val2 = uniques.get( o2 );

            int res = val2.compareTo( val1 );

            if ( res == 0 ) return o1.compareToIgnoreCase( o2 );
            else return res;
        }
    }

}
