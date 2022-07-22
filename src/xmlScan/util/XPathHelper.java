package xmlScan.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class provides some usefull tools
 * to process xml using XPath expression.
 * 
 * @author Serj Sintsov
 */
public class XPathHelper 
{
	private String targetXPath;
	private static DocErrorHandler errorHandler = new DocErrorHandler();
	
	// only one for a thread
	private static final ThreadLocal<DocumentBuilder> docBuilderLocal =
	    new ThreadLocal<DocumentBuilder>() 
	    {
	        @Override 
	        protected DocumentBuilder initialValue() 
	        {
	    		try 
	    		{
	    			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    			docFactory.setNamespaceAware( true );
	    			docFactory.setIgnoringComments( true );
	    			docFactory.setIgnoringElementContentWhitespace( true );
	    			
	    			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    			docBuilder.setErrorHandler( errorHandler );
	    			
					return docBuilder;
				} 
	    		catch (ParserConfigurationException e) {}
				
				return null;
	        }
	    };
	
	// only one for a thread
	private static final ThreadLocal<XPath> xpathLocal =
	    new ThreadLocal<XPath>() 
	    {
	        @Override 
	        protected XPath initialValue() 
	        {
    			XPathFactory xpathFactory = XPathFactory.newInstance();
    			return xpathFactory.newXPath(); 
	        }
	   	};   	
	
	// only one for a thread
	private static ThreadLocal<XPathExpression> xpathExprLocal = new ThreadLocal<XPathExpression>();
	   	
	public XPathHelper( String targetXPath ) throws XPathExpressionException
	{
		this.targetXPath = targetXPath;
		
		//XPathFactory xpathFactory = XPathFactory.newInstance();
		//XPath xpath = xpathFactory.newXPath(); 
		//xpath.compile( this.targetXPath );
	}
	
	/**
	 * Parse file with jdom and evaluate the XPath 
	 * expression specified in the constructor.
	 * 
	 * @param xmlFile a xml file
	 * @return list of nodes or null if something wrong...
	 *  
	 * @throws IOException 
	 * @throws SAXException  
	 */
	public NodeList evalXPath( File xmlFile ) throws SAXException, IOException
	{	
		DocumentBuilder docBuilder = docBuilderLocal.get();
		docBuilder.reset();
		
		Document doc = docBuilder.parse( xmlFile );
		
		PrefixResolver resolver = new PrefixResolverDefault( doc.getDocumentElement() );
		NamespaceContext ctx = new NamespaceContextImp( resolver ); 
		
		XPath xpath = xpathLocal.get();
		xpath.reset();
		xpath.setNamespaceContext( ctx );
		
		try 
		{
			XPathExpression expr = xpathExprLocal.get();
			if ( expr == null ) 
			{
				expr = xpath.compile( targetXPath );
				xpathExprLocal.set( expr );
			}
		
			return ( NodeList ) expr.evaluate( doc, XPathConstants.NODESET );
		} 
		catch ( XPathExpressionException e ) 
		{
			return null;
		}
	}
}

class NamespaceContextImp implements NamespaceContext
{
	private PrefixResolver resolver;
	
	public NamespaceContextImp( PrefixResolver resolver )
	{
		this.resolver = resolver;
	}
	
	public String getNamespaceURI( String prefix ) 
	{
	    return resolver.getNamespaceForPrefix( prefix );
	}
	   
	public Iterator<?> getPrefixes( String val ) 
	{
	    return null;
	}
	   
	public String getPrefix( String uri ) 
	{
	    return null;
	}
}

class DocErrorHandler implements ErrorHandler
{
	@Override
	public void warning( SAXParseException exception ) throws SAXException 
	{
		throw exception;
	}
	
	@Override
	public void fatalError(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}
	
	@Override
	public void error(SAXParseException exception) throws SAXException 
	{
		throw exception;
	}
}
