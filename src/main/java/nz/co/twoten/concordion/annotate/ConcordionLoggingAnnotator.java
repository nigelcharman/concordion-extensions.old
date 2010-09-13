package nz.co.twoten.concordion.annotate;

import org.concordion.api.Resource;
import org.concordion.internal.ConcordionExtender;
import org.concordion.internal.ConcordionExtension;

public class ConcordionLoggingAnnotator implements ConcordionExtension {
    
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
        JulTooltipExtension extension = createExtension();
        
        concordionExtender.withExecuteListener(extension).withAssertEqualsListener(extension).withAssertTrueListener(extension).withAssertFalseListener(extension).withVerifyRowsListener(extension);
        concordionExtender.withSpecificationProcessingListener(extension);
        concordionExtender.withCSS(TOOLTIP_CSS_SOURCE_PATH, TOOLTIP_CSS_TARGET_RESOURCE); 
        concordionExtender.withResource(BUBBLE_RESOURCE_PATH, BUBBLE_IMAGE_RESOURCE);
        concordionExtender.withResource(BUBBLE_FILLER_RESOURCE_PATH, BUBBLE_FILLER_IMAGE_RESOURCE);
        concordionExtender.withResource(INFO_RESOURCE_PATH, INFO_IMAGE_RESOURCE);
    }

    private JulTooltipExtension createExtension() {
        //TODO - Bubble resource path hardcoded in CSS.  Check whether it can be passed in.
        return new JulTooltipExtension(INFO_IMAGE_RESOURCE, TOOLTIP_CSS_TARGET_RESOURCE);
    }
}
