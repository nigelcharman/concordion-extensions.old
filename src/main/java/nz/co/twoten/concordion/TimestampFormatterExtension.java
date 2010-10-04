package nz.co.twoten.concordion;

import nz.co.twoten.concordion.footer.TimestampFormattingSpecificationListener;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class TimestampFormatterExtension implements ConcordionExtension {

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationProcessingListener(new TimestampFormattingSpecificationListener());
    }
}
