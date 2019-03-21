package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.JobClassification;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JobClassificationProcessor extends AbstractSystemDataProcessor<JobClassification> {

    @Autowired
    private StringExtractor<JobClassification> stringExtractor;
    @Autowired
    private DecimalExtractor<JobClassification> decimalExtractor;
    @Autowired
    private BooleanExtractor<JobClassification> booleanExtractor;
    @Autowired
    private DateExtractor<JobClassification> dateExtractor;
    @Autowired
    private IntegerExtractor<JobClassification> integerExtractor;
    @Autowired
    private ListExtractor<JobClassification> listExtractor;
    @Autowired
    private CalendarExtractor<JobClassification> calendarExtractor;
    @Autowired
    private EffortExtractor<JobClassification> effortExtractor;

    @Override
    protected Optional<JobClassification> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getJobClassifications();
    }

    @Override
    protected List<JobClassification> getChildren(JobClassification fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    public StringExtractor<JobClassification> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<JobClassification> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<JobClassification> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<JobClassification> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<JobClassification> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<JobClassification> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<JobClassification> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<JobClassification> getEffortExtractor() {
        return effortExtractor;
    }
}
