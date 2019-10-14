package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.DatedData;
import com.sciforma.psnext.api.DoubleDatedData;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.StringDatedData;
import com.sciforma.psnext.api.Timesheet;
import com.sciforma.psnext.api.TimesheetAssignment;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalNoPrecisionExtractor;
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.HierarchicalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class TimesheetProcessor extends AbstractProcessor<TimesheetAssignment> {

    public static final String ACTUAL_EFFORT = "Actual Effort";
    @Autowired
    private StringExtractor<TimesheetAssignment> stringExtractor;
    @Autowired
    private DecimalExtractor<TimesheetAssignment> decimalExtractor;
    @Autowired
    private DecimalNoPrecisionExtractor<TimesheetAssignment> decimalNoPrecisionExtractor;
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
    @Autowired
    private DoubleDatedExtractor<TimesheetAssignment> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<TimesheetAssignment> stringDatedExtractor;
    @Autowired
    private HierarchicalExtractor<TimesheetAssignment> hierarchicalExtractor;

    protected void process(SciformaService sciformaService, Resource resource) {

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

                        buildTimeDistributedCsvLine(timesheetAssignment, localDate).ifPresent(csvHelper::addLine);

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

    Optional<String> buildTimeDistributedCsvLine(TimesheetAssignment distributedValue, LocalDate localDate) throws PSException {
        Map<String, String> header = new HashMap<>();

        for (String headerItem : csvHelper.getHeaderAsList()) {
            header.put(headerItem, null);
        }

        for (SciformaField sciformaField : getFieldsToExtract()) {

            if (sciformaField.getType().equals(FieldType.EFFORT)) {

                Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                List<DoubleDatedData> datedData = distributedValue.getDatedData(sciformaField.getName(), DatedData.DAY, from, to);

                if (!datedData.isEmpty()) {

                    header.putIfAbsent(START_HEADER, sdf.format(datedData.get(0).getStart()));
                    header.putIfAbsent(FINISH_HEADER, sdf.format(datedData.get(0).getFinish()));
                    header.put(sciformaField.getName(), String.valueOf(datedData.get(0).getData()));

                }

            } else if (sciformaField.getType().equals(FieldType.STRING_DATED)) {

                Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                List<StringDatedData> datedData = distributedValue.getDatedData(sciformaField.getName(), DatedData.DAY, from, to);

                if (!datedData.isEmpty()) {

                    header.putIfAbsent(START_HEADER, sdf.format(datedData.get(0).getStart()));
                    header.putIfAbsent(FINISH_HEADER, sdf.format(datedData.get(0).getFinish()));
                    header.put(sciformaField.getName(), String.valueOf(datedData.get(0).getData()));

                }

            } else {

                extractorMap.get(sciformaField.getType()).extractAsString(distributedValue, sciformaField.getName()).ifPresent(fieldValue -> header.put(sciformaField.getName(), fieldValue));

            }
        }

        if (header.containsKey(ACTUAL_EFFORT) && header.get(ACTUAL_EFFORT) != null && Double.valueOf(header.get(ACTUAL_EFFORT)) > 0) {

            if (header.get(START_HEADER) != null && !header.get(START_HEADER).isEmpty()) {

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

                return Optional.of(csvLine.toString());

            } else {
                return Optional.empty();
            }

        } else {
            return Optional.empty();
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
    public DecimalNoPrecisionExtractor<TimesheetAssignment> getDecimalNoPrecisionExtractor() {
        return decimalNoPrecisionExtractor;
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
    public DoubleDatedExtractor<TimesheetAssignment> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<TimesheetAssignment> getStringDatedExtractor() {
        return stringDatedExtractor;
    }

    @Override
    public HierarchicalExtractor<TimesheetAssignment> getHierarchicalExtractor() {
        return hierarchicalExtractor;
    }

}
