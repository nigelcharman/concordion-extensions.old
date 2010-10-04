package nz.co.twoten.concordion;

import nz.co.twoten.concordion.annotate.TooltipRenderingListener;

import org.concordion.api.Resource;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class LoggingTooltipExtension implements ConcordionExtension {

    private static final String TOOLTIP_CSS_SOURCE_PATH = "/nz/co/twoten/resource/tooltip.css";
    private static final Resource TOOLTIP_CSS_TARGET_RESOURCE = new Resource("/tooltip.css");

    private static final Resource BUBBLE_FILLER_IMAGE_RESOURCE = new Resource("/image/bubble_filler.gif");
    private static final String BUBBLE_FILLER_RESOURCE_PATH = "/nz/co/twoten/resource/bubble_filler.gif";
    private static final Resource BUBBLE_IMAGE_RESOURCE = new Resource("/image/bubble.gif");
    private static final String BUBBLE_RESOURCE_PATH = "/nz/co/twoten/resource/bubble.gif";
    private static final Resource INFO_IMAGE_RESOURCE = new Resource("/image/info16.png");
    private static final String INFO_RESOURCE_PATH = "/nz/co/twoten/resource/i16.png";

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
        return new TooltipRenderingListener(INFO_IMAGE_RESOURCE, TOOLTIP_CSS_TARGET_RESOURCE);
    }
}
