package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceProcessor extends AbstractFieldAccessorProcessor<Resource> {

    @Value("${filename.resources}")
    private String filename;

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

    @Override
    protected String getFilename() {
        return filename;
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
}
