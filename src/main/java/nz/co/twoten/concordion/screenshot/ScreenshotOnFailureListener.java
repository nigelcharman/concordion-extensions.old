package nz.co.twoten.concordion.screenshot;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.concordion.api.ConcordionBuildEvent;
import org.concordion.api.ConcordionBuildListener;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.concordion.api.command.AssertEqualsListener;
import org.concordion.api.command.AssertFailureEvent;
import org.concordion.api.command.AssertFalseListener;
import org.concordion.api.command.AssertSuccessEvent;
import org.concordion.api.command.AssertTrueListener;
import org.concordion.api.command.SpecificationProcessingEvent;
import org.concordion.api.command.SpecificationProcessingListener;

public class ScreenshotOnFailureListener implements AssertEqualsListener, AssertTrueListener, AssertFalseListener, ConcordionBuildListener, SpecificationProcessingListener {

    public static final String CSS = "\n" +
    	".screenshot {\n" +
        "   position:absolute;\n" +
        "   visibility:hidden;\n" +
        "   border:solid 1px black;\n" +
        "   z-index:30;\n" +
        "}\n";

    private static int index = 1;
    
    private Resource resource;
    private Target target;

    @Override
    public void successReported(AssertSuccessEvent event) {
    }

    @Override
    public void failureReported(AssertFailureEvent event) {
        Element element = event.getElement();
        
        String imgName;
        Resource imgResource;
        do {
            imgName = getNextImageName();
            imgResource = resource.getRelativeResource(imgName);
        } while (target.exists(imgResource));
        
        try {
            saveBackgroundImage(imgResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Element imgElement = new Element("img");
        imgElement.setId(imgName);
        imgElement.addStyleClass("screenshot");
        imgElement.addAttribute("src", imgName);
        imgElement.addAttribute("width", "1024");
        
        Element hoverElement = element;  // the element that must be hovered over to show screenshot
        Element[] childElements = element.getChildElements();
        if (childElements.length > 0 && "span".equals(childElements[0].getLocalName())) { 
            hoverElement = childElements[0];  // add hover to child span so that it co-exists nicely with log annotator extension on tables 
        }
        hoverElement.addAttribute("onMouseOver", "document.getElementById('" + imgName + "').style.visibility = 'visible'");
        hoverElement.addAttribute("onMouseOut",  "document.getElementById('" + imgName + "').style.visibility = 'hidden'");

        element.appendChild(imgElement);
    }

    private String getNextImageName() {
        return String.format("img%d.jpg", index++);
    }

    private void saveBackgroundImage(Resource outputResource) throws IOException {
        BufferedImage image = getBackgroundImage();
        ImageIO.write(image, "jpg", target.getOutputStream(outputResource));
    }

    public BufferedImage getBackgroundImage() {
        try {
            Robot rbt = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            BufferedImage background = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
            return background;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
}
