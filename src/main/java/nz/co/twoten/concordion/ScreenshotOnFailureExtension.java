package nz.co.twoten.concordion;

import nz.co.twoten.concordion.screenshot.ScreenshotOnFailureListener;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class ScreenshotOnFailureExtension implements ConcordionExtension {

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        ScreenshotOnFailureListener listener = new ScreenshotOnFailureListener();
        concordionExtender.withSpecificationProcessingListener(listener);
        concordionExtender.withBuildListener(listener);
        concordionExtender.withAssertEqualsListener(listener);
        concordionExtender.withAssertTrueListener(listener);
        concordionExtender.withAssertFalseListener(listener);
        concordionExtender.withEmbeddedCSS(ScreenshotOnFailureListener.CSS);
    }
}
