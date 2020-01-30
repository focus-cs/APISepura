package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Timesheet;
import com.sciforma.psnext.api.TimesheetAssignment;
import fr.sciforma.apietnic.business.csv.TimesheetCsvHelper;
import fr.sciforma.apietnic.business.provider.TimesheetFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Getter
public class TimesheetProcessor extends AbstractProcessor<TimesheetAssignment> {

    @Value("${timesheet.yearfrom}")
    protected String timesheetYear;

    @Autowired
    private TimesheetFieldProvider fieldProvider;
    @Autowired
    private TimesheetCsvHelper csvHelper;

    public void process(SciformaService sciformaService, Resource resource) {

        Date startOfYear;
        Date endOfYear;

        Integer year = LocalDate.now().getYear();

        if (timesheetYear.length() == 4) {

            try {
                year = Integer.valueOf(timesheetYear);
            } catch (NumberFormatException e) {
                Logger.error(e, "Failed to parse date " + timesheetYear);
            }

        }

        startOfYear = Date.from(LocalDate.now()
                .withYear(year)
                .withMonth(1)
                .withDayOfYear(1)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        endOfYear = Date.from(LocalDate.now()
                .withMonth(12)
                .withDayOfMonth(31)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        String resourceName = null;

        try {

            resourceName = resource.getStringField("Name");

            Logger.info("Processing timesheet assignements for resource " + resourceName);

            Optional<Timesheet> timesheet = sciformaService.getTimesheet(resource, startOfYear, endOfYear);

            if (timesheet.isPresent()) {
                List<TimesheetAssignment> timesheetAssignmentList = timesheet.get().getTimesheetAssignmentList();

                LocalDate startDate = startOfYear.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = endOfYear.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                for (TimesheetAssignment timesheetAssignment : timesheetAssignmentList) {

                    for (LocalDate localDate = startDate; localDate.isBefore(endDate); localDate = localDate.plusDays(1)) {

                        buildTimeDistributedCsvLine(timesheetAssignment, localDate, true).ifPresent(csvHelper::addLine);

                    }

                }

            }

            Logger.info("Timesheet assignements for resource " + resourceName + " have been processed successfully");

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve timesheet assignement for resource " + resourceName);
        } finally {
            csvHelper.flush();
        }

    }

}
