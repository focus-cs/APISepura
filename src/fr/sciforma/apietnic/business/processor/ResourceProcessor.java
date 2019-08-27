package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ResourceProcessor extends AbstractFieldAccessorProcessor<Resource> {

    @Autowired
    private StringExtractor<Resource> stringExtractor;
    @Autowired
    private DecimalExtractor<Resource> decimalExtractor;
    @Autowired
    private BooleanExtractor<Resource> booleanExtractor;
    @Autowired
    private DateExtractor<Resource> dateExtractor;
    @Autowired
    private IntegerExtractor<Resource> integerExtractor;
    @Autowired
    private ListExtractor<Resource> listExtractor;
    @Autowired
    private CalendarExtractor<Resource> calendarExtractor;
    @Autowired
    private EffortExtractor<Resource> effortExtractor;
    @Autowired
    private DoubleDatedExtractor<Resource> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<Resource> stringDatedExtractor;

    @Autowired
    private TimesheetProcessor timesheetProcessor;

    public Map<Double, String> getResourcesById(SciformaService sciformaService) {

        Map<Double, String> resourcesById = new HashMap<>();

        for (Resource fieldAccessor : getFieldAccessors(sciformaService)) {

            Optional<Double> internalId = (Optional<Double>) extractorMap.get(FieldType.DECIMAL).extract(fieldAccessor, "Internal ID");

            internalId.ifPresent(aDouble -> resourcesById.putIfAbsent(aDouble, buildCsvLine(fieldAccessor)));

            timesheetProcessor.process(sciformaService, fieldAccessor);

        }

        return resourcesById;
    }

    @Override
    protected List<Resource> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getWorkingResources();
    }

    @Override
    public StringExtractor<Resource> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<Resource> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<Resource> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<Resource> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<Resource> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<Resource> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<Resource> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<Resource> getEffortExtractor() {
        return effortExtractor;
    }

    @Override
    public DoubleDatedExtractor<Resource> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<Resource> getStringDatedExtractor() {
        return stringDatedExtractor;
    }
}
