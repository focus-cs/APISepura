package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PortfolioFolder;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PortfolioFolderProcessor extends AbstractSystemDataProcessor<PortfolioFolder> {

    @Value("${filename.portfolioFolders}")
    private String filename;

    @Autowired
    private StringExtractor<PortfolioFolder> stringExtractor;
    @Autowired
    private DecimalExtractor<PortfolioFolder> decimalExtractor;
    @Autowired
    private BooleanExtractor<PortfolioFolder> booleanExtractor;
    @Autowired
    private DateExtractor<PortfolioFolder> dateExtractor;
    @Autowired
    private IntegerExtractor<PortfolioFolder> integerExtractor;
    @Autowired
    private ListExtractor<PortfolioFolder> listExtractor;
    @Autowired
    private CalendarExtractor<PortfolioFolder> calendarExtractor;
    @Autowired
    private EffortExtractor<PortfolioFolder> effortExtractor;

    @Override
    protected Optional<PortfolioFolder> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getPortfolioFolders();
    }

    @Override
    protected List<PortfolioFolder> getChildren(PortfolioFolder fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    protected String getFilename() {
        return filename;
    }

    @Override
    public StringExtractor<PortfolioFolder> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<PortfolioFolder> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<PortfolioFolder> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<PortfolioFolder> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<PortfolioFolder> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<PortfolioFolder> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<PortfolioFolder> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<PortfolioFolder> getEffortExtractor() {
        return effortExtractor;
    }
}
