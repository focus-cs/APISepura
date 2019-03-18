package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.TimesheetAssignment;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.service.SciformaService;

import java.util.List;

public class TimesheetProcessor extends AbstractFieldAccessorProcessor<TimesheetAssignment> {

    @Override
    protected List<TimesheetAssignment> getFieldAccessors(SciformaService sciformaService) {
        return null;
    }

    @Override
    public void process(SciformaService sciformaService) {

    }

    @Override
    public StringExtractor<TimesheetAssignment> getStringExtractor() {
        return null;
    }

    @Override
    public DecimalExtractor<TimesheetAssignment> getDecimalExtractor() {
        return null;
    }

    @Override
    public BooleanExtractor<TimesheetAssignment> getBooleanExtractor() {
        return null;
    }

    @Override
    public DateExtractor<TimesheetAssignment> getDateExtractor() {
        return null;
    }

    @Override
    public IntegerExtractor<TimesheetAssignment> getIntegerExtractor() {
        return null;
    }

    @Override
    public ListExtractor<TimesheetAssignment> getListExtractor() {
        return null;
    }

    @Override
    public CalendarExtractor<TimesheetAssignment> getCalendarExtractor() {
        return null;
    }

    @Override
    protected String getFilename() {
        return null;
    }
}
