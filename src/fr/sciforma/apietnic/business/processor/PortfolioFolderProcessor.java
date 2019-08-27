package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PortfolioFolder;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.HierarchicalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PortfolioFolderProcessor extends AbstractSystemDataProcessor<PortfolioFolder> {

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
    @Autowired
    private DoubleDatedExtractor<PortfolioFolder> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<PortfolioFolder> stringDatedExtractor;
    @Autowired
    private HierarchicalExtractor<PortfolioFolder> hierarchicalExtractor;

    @Override
    protected Optional<PortfolioFolder> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getPortfolioFolders();
    }

    @Override
    protected List<PortfolioFolder> getChildren(PortfolioFolder fieldAccessor) {
        return fieldAccessor.getChildren();
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

    @Override
    public DoubleDatedExtractor<PortfolioFolder> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<PortfolioFolder> getStringDatedExtractor() {
        return stringDatedExtractor;
    }

    @Override
    public HierarchicalExtractor<PortfolioFolder> getHierarchicalExtractor() {
        return hierarchicalExtractor;
    }
}
