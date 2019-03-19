package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Timesheet;
import com.sciforma.psnext.api.TimesheetAssignment;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class TimesheetProcessor extends AbstractProcessor<TimesheetAssignment> {

    @Value("${filename.timesheets}")
    private String filename;

    @Autowired
    private StringExtractor<TimesheetAssignment> stringExtractor;
    @Autowired
    private DecimalExtractor<TimesheetAssignment> decimalExtractor;
    @Autowired
    private BooleanExtractor<TimesheetAssignment> booleanExtractor;
    @Autowired
    private DateExtractor<TimesheetAssignment> dateExtractor;
    @Autowired
    private IntegerExtractor<TimesheetAssignment> integerExtractor;
    @Autowired
    private ListExtractor<TimesheetAssignment> listExtractor;
    @Autowired
    private CalendarExtractor<TimesheetAssignment> calendarExtractor;
    @Autowired
    private EffortExtractor<TimesheetAssignment> effortExtractor;

    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        csvLines = new ArrayList<>();
    }

    public void process(Resource resource, Date from, Date to, SciformaService sciformaService) {
        Optional<Timesheet> timesheet = sciformaService.getTimesheet(resource, from, to);

        if(timesheet.isPresent()) {

            List timesheetAssignmentList = timesheet.get().getTimesheetAssignmentList();
            Iterator tsIterator = timesheetAssignmentList.iterator();

            while (tsIterator.hasNext()) {
                TimesheetAssignment timesheetAssignment = (TimesheetAssignment) tsIterator.next();

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : getFieldsToExtract()) {
                    extractorMap.get(sciformaField.getType()).extractAsString(timesheetAssignment, sciformaField.getName()).ifPresent(csvLine::add);
                }

                csvLines.add(csvLine.toString());

            }
        }
    }

    @Override
    public StringExtractor<TimesheetAssignment> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<TimesheetAssignment> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<TimesheetAssignment> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<TimesheetAssignment> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<TimesheetAssignment> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<TimesheetAssignment> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<TimesheetAssignment> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<TimesheetAssignment> getEffortExtractor() {
        return effortExtractor;
    }

    @Override
    protected String getFilename() {
        return filename;
    }
}
