package nz.co.twoten.concordion;

import java.awt.Robot;

import nz.co.twoten.concordion.screenshot.ScreenshotTakingExtension;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

/**
 * Adds screenshots to the Concordion output, typically when failures or exceptions occur. The screenshot is displayed when you hover over the relevant element, 
and clicking on the element will open the image in the current browser window.
 * <p>
 * It can also be used as a command, to explicitly include screenshots in the output. 
 * <p>
 * <h4>Installing and configuring</h4>
 * By default, this extension will add screenshots to the output whenever an assertion fails, or an uncaught Throwable occurs in the test. 
 * These options are configurable and an option is also available to take screenshots when assertions pass. 
 * <p>
 * Typical usage is:
 * <pre>
 *       ScreenshotExtension.setMaxWidth(400);
 *    // ScreenshotExtension.setScreenshotOnThrowable(false);
 *    // ScreenshotExtension.setScreenshotOnAssertionSuccess(true);
 *       System.setProperty("concordion.extensions", "nz.co.twoten.concordion.ScreenshotExtension");
 * </pre>
 * <h4>Customising the Screenshot Taker</h4>
 * The screenshot will be of the full visible screen, by default.  This can be overridden using a custom {@link ScreenshotTaker}.  For example, 
 * a Selenium2 ScreenshotTaker would ensure that only the browser window is captured, that the full browser page is captured and that it is 
 * captured regardless of whether the browser window is currently displayed. 
 * <pre> 
 *       EventFiringWebDriver driver = new EventFiringWebDriver(new FirefoxDriver());
 *       ScreenshotTaker screenshotTaker = new SeleniumScreenshotTaker();
 *       driver.register(screenshotTaker);
 *       ScreenshotExtension.setScreenshotTaker(screenshotTaker);
 * </pre>
 * <h4>Using as a command</h4>
 * To explicitly include a screenshot in your output, add an attribute named <code>screenshot</code> using the namespace
 * <code>"http://210.co.nz/2010/concordion"</code> to your Concordion HTML. For example: 
 * <pre>
 * &lt;html xmlns:concordion="http://www.concordion.org/2007/concordion" xmlns:twoten="http://210.co.nz/2010/concordion"&gt;
 * ....
 * &lt;div twoten:screenshot=""/&gt;
 * ...
 * </pre> 
 * By default, the screenshot is embedded in the output HTML. If you'd rather have it linked, set the attribute value to <code>linked</code>, for example:
 * <pre>
 * &lt;p&gt;See &lt;span twoten:screenshot="linked" style="text-decoration: underline;"&gt;this screen&lt;/span&gt;&lt;/p&gt;
 * </pre>
 */
public class ScreenshotExtension implements ConcordionExtension {

    public static final String COMMAND_NAME = "screenshot";
    public static final String EXTENSION_NAMESPACE = "http://210.co.nz/2010/concordion";

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        ScreenshotTakingExtension extension = new ScreenshotTakingExtension();
        concordionExtender.withSpecificationProcessingListener(extension);
        concordionExtender.withBuildListener(extension);
        concordionExtender.withAssertEqualsListener(extension);
        concordionExtender.withAssertTrueListener(extension);
        concordionExtender.withAssertFalseListener(extension);
        concordionExtender.withThrowableListener(extension);
        concordionExtender.withCommand(EXTENSION_NAMESPACE, COMMAND_NAME, extension);
        concordionExtender.withEmbeddedCSS(extension.getCSS());
        concordionExtender.withEmbeddedJavaScript(extension.getJavaScript());
    }

    /**
     * Set a custom screenshot taker. If not set, the extension will default to using {@link Robot}
     * which will take a shot of the full visible screen.
     * 
     * @param screenshotTaker
     */
    public static void setScreenshotTaker(ScreenshotTaker screenshotTaker) {
        ScreenshotTakingExtension.setScreenshotTaker(screenshotTaker);
    }

    /**
     * Sets the maximum width that will be used for display of the images in the output.
     */
    public static void setMaxWidth(int maxWidth) {
        ScreenshotTakingExtension.setMaxWidth(maxWidth);
    }

    /** 
     * Sets whether screenshots will be embedded in the output when assertions fail. Defaults to <b><code>true</code></b>.
     * @param takeShot <code>true</code> to take screenshots on assertion failures, <code>false</code> to not take screenshots.
     */
    public static void setScreenshotOnAssertionFailure(boolean takeShot) {
        ScreenshotTakingExtension.setScreenshotOnAssertionFailure(takeShot);
    }

    /** 
     * Sets whether screenshots will be embedded in the output when assertions pass. Defaults to <b><code>false</code></b>.
     * @param takeShot <code>true</code> to take screenshots on assertion success, <code>false</code> to not take screenshots.
     */
    public static void setScreenshotOnAssertionSuccess(boolean takeShot) {
        ScreenshotTakingExtension.setScreenshotOnAssertionSuccess(takeShot);
    }

    /** 
     * Sets whether screenshots will be embedded in the output when uncaught Throwables occur in the test. Defaults to <b><code>true</code></b>.
     * @param takeShot <code>true</code> to take screenshots on uncaught Throwables, <code>false</code> to not take screenshots.
     */
    public static void setScreenshotOnThrowable(boolean takeShot) {
        ScreenshotTakingExtension.setScreenshotOnThrowable(takeShot);
    }
}
