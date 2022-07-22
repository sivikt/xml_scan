package xmlScan.tracker.config;

/**
 * Provides configuration information for
 * {@link xmlScan.tracker.MultithreadXmlDocsTracker} tracker.
 *
 * @see xmlScan.tracker.MultithreadXmlDocsTracker
 *
 * @author Serj Sintsov
 */
public interface XmlDocsTrackerConfig {

    int getThreadsNum();

    String getFilesDirPath();

    String getTargetXPath();

}
