package nz.co.twoten.concordion.screenshot;

import java.io.IOException;
import java.io.OutputStream;

import nz.co.twoten.concordion.ScreenshotExtension;
import nz.co.twoten.concordion.ScreenshotTaker;
import nz.co.twoten.concordion.ScreenshotUnavailableException;

import org.concordion.api.CommandCall;
import org.concordion.api.ConcordionBuildEvent;
import org.concordion.api.ConcordionBuildListener;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.Resource;
import org.concordion.api.ResultRecorder;
import org.concordion.api.Target;
import org.concordion.api.command.AbstractCommand;
import org.concordion.api.command.AssertEqualsListener;
import org.concordion.api.command.AssertFailureEvent;
import org.concordion.api.command.AssertFalseListener;
import org.concordion.api.command.AssertSuccessEvent;
import org.concordion.api.command.AssertTrueListener;
import org.concordion.api.command.SpecificationProcessingEvent;
import org.concordion.api.command.SpecificationProcessingListener;
import org.concordion.api.command.ThrowableCaughtEvent;
import org.concordion.api.command.ThrowableCaughtListener;
import org.concordion.internal.util.Check;

public class ScreenshotTakingExtension extends AbstractCommand implements AssertEqualsListener, AssertTrueListener, AssertFalseListener, ConcordionBuildListener, SpecificationProcessingListener, ThrowableCaughtListener {

    private static int index = 1;
    private static ScreenshotTaker screenshotTaker = new RobotScreenshotTaker();
    static int maxWidth;
    private static boolean screenshotOnAssertionFailure = true;
    private static boolean screenshotOnAssertionSuccess = false;
    private static boolean screenshotOnThrowable = true;
    
    private Resource resource;
    private Target target;
    
    @Override
    public void successReported(AssertSuccessEvent event) {
        if (screenshotOnAssertionSuccess) {
            addScreenshotTo(event.getElement(), true);
        }
    }

    @Override
    public void failureReported(AssertFailureEvent event) {
        if (screenshotOnAssertionFailure) {
            addScreenshotTo(event.getElement(), true);
        }
    }

    @Override
    public void throwableCaught(ThrowableCaughtEvent event) {
        if (screenshotOnThrowable) {
            addScreenshotTo(event.getElement(), true);
        }
    }

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        Check.isFalse(commandCall.hasChildCommands(), "Nesting commands inside an 'screenshot' is not supported");
        Element element = commandCall.getElement();

        String props = element.getAttributeValue(ScreenshotExtension.COMMAND_NAME, ScreenshotExtension.EXTENSION_NAMESPACE);
        
        boolean linked = false;
        if ("linked".equals(props)){
            linked = true;
        }

        addScreenshotTo(element, linked);
    }
    
    private void addScreenshotTo(Element element, boolean hidden) {
        String imageName = getNextImageName();
        Resource imageResource = resource.getRelativeResource(imageName);
        
        int imageWidth;
        try {
            OutputStream outputStream = target.getOutputStream(imageResource);
            imageWidth = screenshotTaker.writeScreenshotTo(outputStream);
            new ImageRenderer(hidden, maxWidth).addImageToElement(element, imageName, imageWidth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ScreenshotUnavailableException ignore) {
        }
    }

    private String getNextImageName() {
        String fileExtension = getFileExtension();
        return String.format("img%d.%s", index++, fileExtension);
    }

    private String getFileExtension() {
        return screenshotTaker.getFileExtension();
    }

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {
        resource = event.getResource();
    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
    }

    @Override
    public void concordionBuilt(ConcordionBuildEvent event) {
        target = event.getTarget();
    }

    public static void setScreenshotTaker(ScreenshotTaker screenshotTaker) {
        ScreenshotTakingExtension.screenshotTaker = screenshotTaker;
    }

    public static void setMaxWidth(int maxWidth) {
        ScreenshotTakingExtension.maxWidth = maxWidth;
    }

    public static void setScreenshotOnAssertionFailure(boolean takeShot) {
        screenshotOnAssertionFailure = takeShot;
    }

    public static void setScreenshotOnAssertionSuccess(boolean takeShot) {
        screenshotOnAssertionSuccess = takeShot;
    }
    
    public static void setScreenshotOnThrowable(boolean takeShot) {
        screenshotOnThrowable = takeShot;
    }

    public String getCSS() {
        return ImageRenderer.CSS;
    }
    
    public String getJavaScript() {
        return ImageRenderer.script;
    }
}
