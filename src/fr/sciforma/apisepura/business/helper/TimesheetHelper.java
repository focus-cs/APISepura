package fr.sciforma.apisepura.business.helper;

import com.sciforma.psnext.api.Timesheet;
import fr.sciforma.apisepura.business.model.SepuraTimesheet;
import lombok.Builder;

@Builder
public class TimesheetHelper {
    private SepuraTimesheet sepuraTimesheet;
    private Timesheet sciformaTimesheet;
}
