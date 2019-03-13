package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Timesheet;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.service.SciformaService;

import java.util.List;

public class TimesheetProcessor extends AbstractFieldAccessorProcessor<Timesheet> {

    @Override
    protected List<Timesheet> getFieldAccessors(SciformaService sciformaService) {
        return null;
    }

    @Override
    public void process(SciformaService sciformaService) {

    }

    @Override
    public StringExtractor<Timesheet> getStringExtractor() {
        return null;
    }

    @Override
    public DecimalExtractor<Timesheet> getDecimalExtractor() {
        return null;
    }

    @Override
    public BooleanExtractor<Timesheet> getBooleanExtractor() {
        return null;
    }

    @Override
    public DateExtractor<Timesheet> getDateExtractor() {
        return null;
    }

    @Override
    public IntegerExtractor<Timesheet> getIntegerExtractor() {
        return null;
    }

    @Override
    public ListExtractor<Timesheet> getListExtractor() {
        return null;
    }

    @Override
    public CalendarExtractor<Timesheet> getCalendarExtractor() {
        return null;
    }

    @Override
    protected String getFilename() {
        return null;
    }
}
