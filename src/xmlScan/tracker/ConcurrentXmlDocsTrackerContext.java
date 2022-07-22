package xmlScan.tracker;

import xmlScan.tracker.config.XmlDocsTrackerConfig;
import xmlScan.worker.results.FindByXPathResultsStorage;
import xmlScan.worker.results.ResultsStorage;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Concurrent {@link TrackerContext} can be used by many clients simultaneously
 *
 * @see TrackerContext
 *
 * @author Serj Sintsov
 */
public class ConcurrentXmlDocsTrackerContext implements TrackerContext
{
    private ConcurrentLinkedQueue<File> processingFiles;
    private XmlDocsTrackerConfig cfg;

    private ResultsStorage<?> resultsStorage;

    private ConcurrentXmlDocsTrackerContext() {}

    public static ConcurrentXmlDocsTrackerContext createContext( XmlDocsTrackerConfig cfg )
            throws TrackerException {
        ConcurrentXmlDocsTrackerContext ctx = new ConcurrentXmlDocsTrackerContext();

        ctx.processingFiles = new ConcurrentLinkedQueue<File>();
        ctx.cfg = cfg;
        ctx.resultsStorage = new FindByXPathResultsStorage();

        ctx.initResources();

        return ctx;
    }

    /**
     * Initialize resources
     * @throws TrackerException if resources are unavailable
     */
    private void initResources() throws TrackerException
    {
        File xmlDocsFolder = new File( cfg.getFilesDirPath() );

        if ( !xmlDocsFolder.exists() )
            throw new TrackerException( "Specified xml documents folder doesn't exist!" );

        if ( !xmlDocsFolder.isDirectory() )
            throw new TrackerException( "Specified xml documents folder isn't a directory!" );

        if ( !xmlDocsFolder.canRead() )
            throw new TrackerException( "Specified xml documents folder can not be read!" );

        File[] xmlFiles = xmlDocsFolder.listFiles( new XmlFileFilter() );
        Collections.addAll(processingFiles, xmlFiles);
    }

    @Override
    public File nextFile()
    {
        return processingFiles.poll();
    }

    @Override
    public int filesCount()
    {
        return processingFiles.size();
    }

    @Override
    public ResultsStorage<?> resultsStorage()
    {
        return resultsStorage;
    }

    /**
     * Accepts only xml files
     */
    private class XmlFileFilter implements FileFilter
    {
        @Override
        public boolean accept( File file )
        {
            return file.exists() &&
                   file.isFile() &&
                   file.canRead() &&
                   file.getName().endsWith( ".xml" );
        }
    }

}
