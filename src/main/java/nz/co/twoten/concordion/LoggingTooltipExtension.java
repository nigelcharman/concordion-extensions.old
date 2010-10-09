package nz.co.twoten.concordion;

import java.util.logging.Level;
import java.util.logging.Logger;

import nz.co.twoten.concordion.annotate.TooltipRenderingListener;

import org.concordion.api.Resource;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

/**
 * Annotates the Concordion HTML output with logging information captured using java.util.logging. See <a href="http://tutansblog.blogspot.com/2010/09/whats-happening-in-my-acceptance-tests.html">this blog entry</a> for details and screenshots.
 * <p>
 * <h4>Installing and configuring</h4>
 * To install the extension:
 * <pre>
 *       System.setProperty("concordion.extensions", "nz.co.twoten.concordion.LoggingTooltipExtension");
 * </pre>
 * By default, this extension will capture all output from the root logger and disable console logging of the root logger.
 * For finer-grained control, the logger names can be configured by passing a comma separated list of logger names to {@link #setLoggerNames(String)}.
 * The logging level of this handler can also be set, and the option to disable console logging can be overridden.
 * <p>
 * <pre>
 *       LoggingTooltipExtension.setLoggerNames("carbon.CarbonCalculatorTest", "selenium.events");
 *       LoggingTooltipExtension.setLoggingLevel("FINE");
 *       LoggingTooltipExtension.setRemoveRootConsoleLoggingHandler(false);
 *       System.setProperty("concordion.extensions", "nz.co.twoten.concordion.LoggingTooltipExtension");
 *       Logger.getLogger("carbon.CarbonCalculatorTest").setLevel(Level.FINE);
 * </pre>
 * <p>
 * Thanks to Trent Richardson for the <a href="http://trentrichardson.com/examples/csstooltips/">CSS Tooltip</a> implementation.
 */
public class LoggingTooltipExtension implements ConcordionExtension {

    private static final String TOOLTIP_CSS_SOURCE_PATH = "/nz/co/twoten/resource/tooltip.css";
    private static final Resource TOOLTIP_CSS_TARGET_RESOURCE = new Resource("/tooltip.css");

    private static final Resource BUBBLE_FILLER_IMAGE_RESOURCE = new Resource("/image/bubble_filler.gif");
    private static final String BUBBLE_FILLER_RESOURCE_PATH = "/nz/co/twoten/resource/bubble_filler.gif";
    private static final Resource BUBBLE_IMAGE_RESOURCE = new Resource("/image/bubble.gif");
    private static final String BUBBLE_RESOURCE_PATH = "/nz/co/twoten/resource/bubble.gif";
    private static final Resource INFO_IMAGE_RESOURCE = new Resource("/image/info16.png");
    private static final String INFO_RESOURCE_PATH = "/nz/co/twoten/resource/i16.png";
    
    private static String loggerNames = "";
    private static boolean removeRootConsoleLoggingHandler = true;
    private static String loggingLevel = "info";

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        TooltipRenderingListener extension = createExtension();

        concordionExtender.withExecuteListener(extension).withAssertEqualsListener(extension).withAssertTrueListener(extension)
                .withAssertFalseListener(extension).withVerifyRowsListener(extension).withThrowableListener(extension);
        concordionExtender.withSpecificationProcessingListener(extension);
        concordionExtender.withLinkedCSS(TOOLTIP_CSS_SOURCE_PATH, TOOLTIP_CSS_TARGET_RESOURCE);
        concordionExtender.withResource(BUBBLE_RESOURCE_PATH, BUBBLE_IMAGE_RESOURCE);
        concordionExtender.withResource(BUBBLE_FILLER_RESOURCE_PATH, BUBBLE_FILLER_IMAGE_RESOURCE);
        concordionExtender.withResource(INFO_RESOURCE_PATH, INFO_IMAGE_RESOURCE);
    }

    private TooltipRenderingListener createExtension() {
        return new TooltipRenderingListener(INFO_IMAGE_RESOURCE, loggerNames, loggingLevel, removeRootConsoleLoggingHandler);
    }

    /**
     * Restricts the log messages that are included in the Concordion output. If not set, all output from the root
     * logger will be captured.
     * <p>
     * This parameter must be set prior to setting the system property to install this extension. 
     * 
     * @param loggerNames a comma separated list of the names of loggers whose output is to be shown in the Concordion output
     */
    public static void setLoggerNames(String loggerNames) {
        LoggingTooltipExtension.loggerNames = loggerNames;
    }

    /**
     * Sets whether the console handler for the root logger is removed. Defaults to <code>true</code>.
     * 
     * @param removeRootConsoleLoggingHandler <code>true</code> to remove console output for the root logger, <code>false</code> to show the console output
     */
    public static void setRemoveRootConsoleLoggingHandler(boolean removeRootConsoleLoggingHandler) {
        LoggingTooltipExtension.removeRootConsoleLoggingHandler = removeRootConsoleLoggingHandler;
    }

    /**
     * Sets the logging {@link Level} for the handler that writes to the Concordion output. Log messages of this level and
     * higher will be output.  Note that the associated {@link Logger}s must also have an appropriate logging level set.
     * <p>
     * This parameter must be set prior to setting the system property to install this extension. 
     * 
     * @param loggingLevel either a level name or an integer value
     * <p>For example:
     * <ul>
     * <li>"FINE"</li>
     * <li>"1000"</li>
     * </ol> 
     */
    public static void setLoggingLevel(String loggingLevel) {
        LoggingTooltipExtension.loggingLevel = loggingLevel;
    }
}
