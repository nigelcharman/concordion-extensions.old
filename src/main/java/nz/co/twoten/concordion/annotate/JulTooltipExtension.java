package nz.co.twoten.concordion.annotate;

import java.io.ByteArrayOutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import nz.co.twoten.jul.formatter.TimeAndMessageFormatter;

import org.concordion.api.Resource;
import org.concordion.api.command.AssertEqualsListener;
import org.concordion.api.command.AssertFailureEvent;
import org.concordion.api.command.AssertFalseListener;
import org.concordion.api.command.AssertSuccessEvent;
import org.concordion.api.command.AssertTrueListener;
import org.concordion.api.command.ExecuteEvent;
import org.concordion.api.command.ExecuteListener;
import org.concordion.api.command.ExpressionEvaluatedEvent;
import org.concordion.api.command.MissingRowEvent;
import org.concordion.api.command.SpecificationProcessingEvent;
import org.concordion.api.command.SpecificationProcessingListener;
import org.concordion.api.command.SurplusRowEvent;
import org.concordion.api.command.VerifyRowsListener;

public class JulTooltipExtension implements AssertEqualsListener, AssertTrueListener, AssertFalseListener, ExecuteListener, SpecificationProcessingListener, VerifyRowsListener { //, DocumentParsingListener {

    private TooltipRenderer renderer;
    private Resource resource;

    private static final ByteArrayOutputStream baos;
    private static final StreamHandler streamHandler; 

    static {
        baos = new ByteArrayOutputStream(4096);
        streamHandler = new StreamHandler(baos, new TimeAndMessageFormatter());
        Logger logger = Logger.getLogger("");
        removeConsoleHandler(logger);
        logger.addHandler(streamHandler);
    }

    private static void removeConsoleHandler(Logger logger) {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                System.out.println("Removing console handler");
                logger.removeHandler(handler);
            }
        }
    }
    
    JulTooltipExtension(Resource iconResource, Resource tooltipCssResource) {
        renderer = new TooltipRenderer(iconResource);
    }

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {
        resource = event.getResource();
    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
    }

    public void executeCompleted(ExecuteEvent event) {
        String text = getLogMessages();
        
        if (text.length() > 0) {
            renderer.hoverText(resource, event.getElement(), text);
        }
    }

    @Override
    public void failureReported(AssertFailureEvent event) {
        String text = getLogMessages();
        
        if (text.length() > 0) {
            renderer.hoverText(resource, event.getElement(), text);
        }
    }

    @Override
    public void successReported(AssertSuccessEvent event) {
        String text = getLogMessages();
        
        if (text.length() > 0) {
            renderer.hoverText(resource, event.getElement(), text);
        }
    }

    @Override
    public void expressionEvaluated(ExpressionEvaluatedEvent event) {
        String text = getLogMessages();
        
        if (text.length() > 0) {
            renderer.hoverText(resource, event.getElement(), text);
        }
    }

    @Override
    public void missingRow(MissingRowEvent event) {
    }

    @Override
    public void surplusRow(SurplusRowEvent event) {
    }

    private String getLogMessages() {
        streamHandler.flush();
        String text = baos.toString();
        baos.reset();
        return text;
    }
}
