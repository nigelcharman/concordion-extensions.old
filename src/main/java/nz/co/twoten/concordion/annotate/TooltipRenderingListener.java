package nz.co.twoten.concordion.annotate;

import java.io.ByteArrayOutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import nz.co.twoten.jul.formatter.TimeAndMessageFormatter;

import org.concordion.api.Element;
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
import org.concordion.api.command.ThrowableCaughtEvent;
import org.concordion.api.command.ThrowableCaughtListener;
import org.concordion.api.command.VerifyRowsListener;

public class TooltipRenderingListener implements AssertEqualsListener, AssertTrueListener, AssertFalseListener, ExecuteListener,
        SpecificationProcessingListener, VerifyRowsListener, ThrowableCaughtListener {

    private TooltipRenderer renderer;
    private Resource resource;

    private final ByteArrayOutputStream baos;
    private final StreamHandler streamHandler;

    public TooltipRenderingListener(Resource iconResource, String loggerNames, String loggingLevel, boolean removeRootConsoleLoggingHandler) {
        baos = new ByteArrayOutputStream(4096);
        streamHandler = new StreamHandler(baos, new TimeAndMessageFormatter());
        Level requestedLevel = Level.parse(loggingLevel.toUpperCase());
        streamHandler.setLevel(requestedLevel);
        for (String loggerName : loggerNames.split(",")) {
            Logger logger = Logger.getLogger(loggerName.trim());
            logger.addHandler(streamHandler);
        }
        renderer = new TooltipRenderer(iconResource);
        if (removeRootConsoleLoggingHandler) {
            removeRootConsoleHandler();
        }
    }

    private  void removeRootConsoleHandler() {
        Logger logger = Logger.getLogger("");
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                System.out.println("LoggingTooltipExtension: removing root console logging handler");
                logger.removeHandler(handler);
            }
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
    public void executeCompleted(ExecuteEvent event) {
        renderLogMessages(event.getElement());
    }

    @Override
    public void failureReported(AssertFailureEvent event) {
        renderLogMessages(event.getElement());
    }

    @Override
    public void successReported(AssertSuccessEvent event) {
        renderLogMessages(event.getElement());
    }

    @Override
    public void expressionEvaluated(ExpressionEvaluatedEvent event) {
        renderLogMessages(event.getElement());
    }

    @Override
    public void throwableCaught(ThrowableCaughtEvent event) {
        renderLogMessages(event.getElement());
    }

    @Override
    public void missingRow(MissingRowEvent event) {
    }

    @Override
    public void surplusRow(SurplusRowEvent event) {
    }

    private void renderLogMessages(Element element) {
        String text = getLogMessages();

        if (text.length() > 0) {
            renderer.hoverText(resource, element, text);
        }
    }

    private String getLogMessages() {
        streamHandler.flush();
        String text = baos.toString();
        baos.reset();
        return text;
    }
}
