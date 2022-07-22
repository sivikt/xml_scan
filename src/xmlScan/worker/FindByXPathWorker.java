package xmlScan.worker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import xmlScan.tracker.TrackerContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xmlScan.util.XPathHelper;

/**
 * This {@link Worker} looks for nodes in xml file by using specified XPath
 * expression and gets the text value of founded nodes.
 * 
 * For example:
 * There's a xml file below
 * 
 * <?xml version="1.0"?>
 * <nodes type="list" date="20.12.20010">
 * 		<node>
 *	    	text_content0
 *	    	<id>0</id>
 *	    	<name>node0</name>
 *		</node>
 *		<node>
 *	    	<id>1</id>
 *	    	<name>node1</name>
 *		</node>
 *		<node>
 *	    	node2
 *	    	<id>2</id>
 *		</node>
 *	</nodes>
 * 
 * And targetXPath = node.
 * The worker's result will be the following strings:
 * "text_content0" and "node2".
 * 
 * For the targetXPath = nodes/@* 
 * the result is "list" and "20.12.20010"
 * 
 * @see RunnableCyclicWorker
 * @see Worker
 *
 * @author Serj Sintsov
 */
public class FindByXPathWorker extends RunnableCyclicWorker<String>
{
    private String targetXPath;
    private XPathHelper xpathHelper;

    /**
     * Validates xpath expression correctness and creates an instance of
     * {@link FindByXPathWorker}.
     *
     * @param trackerCtx related {@link TrackerContext} context
     * @param targetXPath XPath expression to search nodes
     *
     * @throws WorkerException if targetXPath is incorrect
     * @throws IllegalArgumentException if targetXPath is null
     */
    public FindByXPathWorker(Warden manager, TrackerContext trackerCtx,
                             String targetXPath)
            throws WorkerException
    {
        super( manager, trackerCtx );

        assert targetXPath == null : "targetXPath cannot be null!";

        this.targetXPath = targetXPath;
        try
        {
            this.xpathHelper = new XPathHelper( this.targetXPath );
        }
        catch ( XPathExpressionException e )
        {
            throw new WorkerException( "Specified XPath expression is incorrect!" );
        }
    }

    @Override
    protected List<String> scanFile( File src ) throws WorkerException
    {
        try
        {
            NodeList nodes = xpathHelper.evalXPath(src);

            if (nodes == null) return new ArrayList<String>(0);

            List<String> results = new ArrayList<String>( nodes.getLength() );

            for ( int i = 0; i < nodes.getLength(); i++ )
            {
                NodeList childs = nodes.item( i ).getChildNodes();
                StringBuilder buf = new StringBuilder();

                for ( int j = 0; j < childs.getLength(); j++ )
                {
                    Node child = childs.item( j );
                    if ( child.getNodeType() == Node.TEXT_NODE )
                    {
                        String result = escapeSpaces(child.getTextContent());
                        buf.append( result );
                    }
                }

                results.add( buf.toString() );
            }

            return results;
        }
        catch ( SAXException e )
        {
            throw new WorkerException(
                "File '%s' has incorrect xml format. Failed due to error '%s'",
                src.getName(), e.getMessage()
            );
        }
        catch ( IOException e )
        {
            throw new WorkerException(
                "I/O operation failed while scanning file '%s'. %s",
                src.getName(), e.getMessage()
            );
        }
        catch ( Exception e )
        {
            throw new WorkerException(
                "An error occurred while scanning file '%s'. %s",
                src.getName(), e.getMessage() != null ? e.getMessage() : ""
            );
        }
    }

    private String escapeSpaces( String str )
    {
        return str.replaceAll( "\\s+", " " ).trim();
    }

}
