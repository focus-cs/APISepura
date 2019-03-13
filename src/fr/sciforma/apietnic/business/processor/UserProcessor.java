package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.User;
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
public class UserProcessor extends AbstractFieldAccessorProcessor<User> {

    @Value("${filename.users}")
    protected String filename;

    @Autowired
    private StringExtractor<User> stringExtractor;
    @Autowired
    private DecimalExtractor<User> decimalExtractor;
    @Autowired
    private BooleanExtractor<User> booleanExtractor;
    @Autowired
    private DateExtractor<User> dateExtractor;
    @Autowired
    private IntegerExtractor<User> integerExtractor;
    @Autowired
    private ListExtractor<User> listExtractor;
    @Autowired
    private CalendarExtractor<User> calendarExtractor;

    @Override
    protected String getFilename() {
        return filename;
    }

    @Override
    protected List<User> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getUsers();
    }

    @Override
    public StringExtractor<User> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<User> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<User> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<User> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<User> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<User> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<User> getCalendarExtractor() {
        return calendarExtractor;
    }

}
