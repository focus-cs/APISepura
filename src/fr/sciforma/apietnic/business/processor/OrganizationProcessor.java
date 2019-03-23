package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Organization;
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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrganizationProcessor extends AbstractSystemDataProcessor<Organization> {

    @Autowired
    private StringExtractor<Organization> stringExtractor;
    @Autowired
    private DecimalExtractor<Organization> decimalExtractor;
    @Autowired
    private BooleanExtractor<Organization> booleanExtractor;
    @Autowired
    private DateExtractor<Organization> dateExtractor;
    @Autowired
    private IntegerExtractor<Organization> integerExtractor;
    @Autowired
    private ListExtractor<Organization> listExtractor;
    @Autowired
    private CalendarExtractor<Organization> calendarExtractor;
    @Autowired
    private EffortExtractor<Organization> effortExtractor;

    @Override
    protected Optional<Organization> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getOrganizations();
    }

    @Override
    protected List<Organization> getChildren(Organization fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    public StringExtractor<Organization> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<Organization> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<Organization> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<Organization> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<Organization> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<Organization> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<Organization> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<Organization> getEffortExtractor() {
        return effortExtractor;
    }
}
