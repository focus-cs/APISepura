package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.*;
import fr.sciforma.apietnic.business.csv.CsvHelper;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class TimesheetProcessor extends AbstractProcessor<TimesheetAssignment> {

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

    protected void process(SciformaService sciformaService, Resource resource) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date startOfYear = Date.from(LocalDate.now()
                .withMonth(1)
                .withDayOfYear(1)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        Date endOfYear = Date.from(LocalDate.now()
                .withMonth(12)
                .withDayOfYear(31)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        try {

            Logger.info("Processing timesheet from resource " + resource.getStringField("Name"));

            Optional<Timesheet> timesheet = sciformaService.getTimesheet(resource, startOfYear, endOfYear);

            if (timesheet.isPresent()) {
                List<TimesheetAssignment> timesheetAssignmentList = timesheet.get().getTimesheetAssignmentList();

                LocalDate startDate = startOfYear.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = endOfYear.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                for (TimesheetAssignment timesheetAssignment : timesheetAssignmentList) {

                    for (LocalDate localDate = startDate; localDate.isBefore(endDate); localDate = localDate.plusDays(1)) {

                        Map<String, String> header = new HashMap<>();

                        for (String headerItem : csvHelper.getHeaderAsList()) {
                            header.put(headerItem, null);
                        }

                        for (SciformaField sciformaField : getFieldsToExtract()) {

                            if (sciformaField.getType().equals(FieldType.EFFORT)) {

                                Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                                Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                                List<DoubleDatedData> datedData = timesheetAssignment.getDatedData(sciformaField.getName(), DatedData.DAY, from, to);

                                if (!datedData.isEmpty()) {

                                    header.put(CsvHelper.START_HEADER, sdf.format(datedData.get(0).getStart()));
                                    header.put(CsvHelper.FINISH_HEADER, sdf.format(datedData.get(0).getFinish()));
                                    header.put(sciformaField.getName(), String.valueOf(datedData.get(0).getData()));

                                }

                            } else {

                                extractorMap.get(sciformaField.getType()).extractAsString(timesheetAssignment, sciformaField.getName()).ifPresent(fieldValue -> header.put(sciformaField.getName(), fieldValue));

                            }
                        }

                        // If no distributed value, skip the line
                        if(header.get(CsvHelper.START_HEADER) != null && ! header.get(CsvHelper.START_HEADER).isEmpty()) {

                            StringJoiner csvLine = new StringJoiner(csvDelimiter);

                            for (String headerItem : csvHelper.getHeaderAsList()) {

                                if (header.containsKey(headerItem)) {

                                    if (header.get(headerItem) != null) {
                                        csvLine.add(header.get(headerItem));
                                    } else {
                                        csvLine.add("");
                                    }

                                }

                            }

                            csvHelper.addLine(csvLine.toString());

                        }

                    }

                }

            }

        } catch (PSException e) {

            Logger.error(e, "Failed to retrieve timesheet assignement for resource");

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

}
