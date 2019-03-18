package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.JobClassification;
import com.sciforma.psnext.api.Organization;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrganizationProcessor extends AbstractSystemDataProcessor<Organization> {

    @Value("${filename.organizations}")
    private String filename;

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
    protected String getFilename() {
        return filename;
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
