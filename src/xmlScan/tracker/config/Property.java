package xmlScan.tracker.config;

/**
 * Enum with available properties from "config.properties" config.
 * Use {@link XmlDocsConfigReader#asString(Property
 * .PROPERTY_NAME)}
 * or {@link XmlDocsConfigReader#asInteger(Property
 * .PROPERTY_NAME)}, or other... for access to property's value.
 * 
 * @see XmlDocsConfigReader
 * 
 * @author Serj Sintsov
 */
public enum Property 
{
	XmlDocsSourceFolder( "xmldocs.source.folder" ),
	TargetXPath( "target.xpath" ),
	ConcurrentThreadsNumber( "concurrent.threads.number" );
	
	private String propertyName;

	private Property( String propName ) 
	{
		propertyName = propName;
	}

	public String getPropertyName() 
	{
		return propertyName;
	}
}

