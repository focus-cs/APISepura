package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.*;
import fr.sciforma.apietnic.business.csv.CsvHelper;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class ResourceAssignementProcessor extends AbstractProcessor<ResAssignment> {

    @Autowired
    private StringExtractor<ResAssignment> stringExtractor;
    @Autowired
    private DecimalExtractor<ResAssignment> decimalExtractor;
    @Autowired
    private BooleanExtractor<ResAssignment> booleanExtractor;
    @Autowired
    private DateExtractor<ResAssignment> dateExtractor;
    @Autowired
    private IntegerExtractor<ResAssignment> integerExtractor;
    @Autowired
    private ListExtractor<ResAssignment> listExtractor;
    @Autowired
    private CalendarExtractor<ResAssignment> calendarExtractor;
    @Autowired
    private EffortExtractor<ResAssignment> effortExtractor;

    protected void process(Task task, Date start, Date end) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            List<ResAssignment> resAssignmentList = task.getResAssignmentList();

            LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (ResAssignment resAssignment : resAssignmentList) {

                for (LocalDate localDate = startDate; localDate.isBefore(endDate); localDate = localDate.plusDays(1)) {

                    Map<String, String> header = new HashMap<>();

                    for (String headerItem : csvHelper.getHeaderAsList()) {
                        header.put(headerItem, null);
                    }

                    for (SciformaField sciformaField : getFieldsToExtract()) {

                        if (sciformaField.getType().equals(FieldType.EFFORT)) {

                            Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                            Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                            List<DoubleDatedData> datedData = resAssignment.getDatedData(sciformaField.getName(), DatedData.DAY, from, to);

                            if (!datedData.isEmpty()) {

                                header.put(CsvHelper.START_HEADER, sdf.format(datedData.get(0).getStart()));
                                header.put(CsvHelper.FINISH_HEADER, sdf.format(datedData.get(0).getFinish()));
                                header.put(sciformaField.getName(), String.valueOf(datedData.get(0).getData()));

                            }

                        } else {

                            extractorMap.get(sciformaField.getType()).extractAsString(resAssignment, sciformaField.getName()).ifPresent(fieldValue -> header.put(sciformaField.getName(), fieldValue));

                        }
                    }

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

        } catch (PSException e) {

            Logger.error(e, "Failed to retrieve resource assignement for task");

        }

    }

    @Override
    public StringExtractor<ResAssignment> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<ResAssignment> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<ResAssignment> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<ResAssignment> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<ResAssignment> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<ResAssignment> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<ResAssignment> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<ResAssignment> getEffortExtractor() {
        return effortExtractor;
    }
}
