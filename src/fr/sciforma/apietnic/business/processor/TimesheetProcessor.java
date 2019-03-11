package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Timesheet;
import fr.sciforma.apietnic.service.SciformaService;

public class TimesheetProcessor extends AbstractProcessor<Timesheet> {

    @Override
    public void process(SciformaService sciformaService) {

    }

    @Override
    protected String getFilename() {
        return null;
    }
}
