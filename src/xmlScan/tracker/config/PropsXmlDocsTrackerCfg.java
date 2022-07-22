package xmlScan.tracker.config;

import static xmlScan.util.StdOut.printInfoMsg;

import java.util.MissingResourceException;

/**
 * Property based configuration
 *
 * @see XmlDocsTrackerConfig
 *
 * @author Serj Sintsov
 */
public class PropsXmlDocsTrackerCfg implements XmlDocsTrackerConfig {

    private final static String DEFAULT_FILES_DIR = ".";
    private final static int MIN_THREADS_NUM = 1;

    /**
     * Actual concurrent threads number. Tracker can change
     * this value depending on the processing files count.
     * There's no sense to create more performers then count
     * of files to process.
     */
    private int threadsNum;

    private String filesDirPath;
    private String targetXPath;

    private PropsXmlDocsTrackerCfg() {}

    public static PropsXmlDocsTrackerCfg createConfig(String bundle)
            throws TrackerConfigException {

        PropsXmlDocsTrackerCfg cfg = new PropsXmlDocsTrackerCfg();

        XmlDocsConfigReader cfgReader;
        try {
            cfgReader = new XmlDocsConfigReader( bundle );
        }
        catch ( MissingResourceException e )
        {
            throw new TrackerConfigException( bundle + ".properties file not found!" );
        }

        cfg.targetXPath = cfgReader.asString(Property.TargetXPath);
        cfg.filesDirPath = cfgReader.asString(Property.XmlDocsSourceFolder);
        cfg.threadsNum = cfgReader.asInteger(Property.ConcurrentThreadsNumber);

        cfg.validateAndAdjust();

        return cfg;
    }

    private void validateAndAdjust() throws TrackerConfigException
    {
        if ( targetXPath == null || targetXPath.length() == 0 )
            throw new TrackerConfigException("\"targetXPath\" property must be specified." );

        if ( filesDirPath == null || filesDirPath.length() == 0 )
        {
            filesDirPath = DEFAULT_FILES_DIR;
            printInfoMsg(
                    "\"filesDirPath\" property was not specified. Use default value."
            );
        }

        if ( threadsNum < MIN_THREADS_NUM )
        {
            threadsNum = MIN_THREADS_NUM;
            printInfoMsg(
                    "\"threadsNum\" property was specified incorrectly. Use default value."
            );
        }

        if ( !targetXPath.startsWith( "/" ) )
            targetXPath = "//".concat( targetXPath );
    }

    @Override
    public int getThreadsNum() {
        return threadsNum;
    }

    @Override
    public String getFilesDirPath() {
        return filesDirPath;
    }

    @Override
    public String getTargetXPath() {
        return targetXPath;
    }

}
