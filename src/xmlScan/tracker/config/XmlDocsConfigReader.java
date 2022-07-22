package xmlScan.tracker.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class with some util methods to reading project configuration.
 * 
 * @author Serj Sintsov
 */
public class XmlDocsConfigReader
{
	private String bundleName;
	private ResourceBundle resBundle;
	
	public XmlDocsConfigReader(String bundleName) throws MissingResourceException
	{
		this.bundleName = bundleName;
		resBundle = ResourceBundle.getBundle( this.bundleName );
	}
	
	/**
	 * Get value of property from the specified bundle as {@link String}
	 */
	public String asString(Property property)
	{
		return resBundle.getString( property.getPropertyName() );
	}

	/**
	 * Get value of property from the specified bundle as {@link Integer}
	 */
	public Integer asInteger(Property property)
	{
		return Integer.parseInt(
            resBundle.getString( property.getPropertyName() )
        );
	}
}
