package nz.co.twoten.concordion;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class Extensions implements ConcordionExtension {

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        new LoggingTooltipExtension().addTo(concordionExtender);
        new ScreenshotOnFailureExtension().addTo(concordionExtender);
        new TimestampFormatterExtension().addTo(concordionExtender);
    }
}
