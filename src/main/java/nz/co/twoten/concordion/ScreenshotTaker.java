package nz.co.twoten.concordion;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Takes shots of the system under test. 
 */
public interface ScreenshotTaker {

    /**
     * Take a shot and write to the given output stream.  If unable to take a screenshot, 
     * throw {@link ScreenshotUnavailableException}. 
     */
    int writeScreenshotTo(OutputStream outputStream) throws IOException;

    /**
     * Returns the filename extension that should be used for images taken by this object.
     */
    String getFileExtension();
}