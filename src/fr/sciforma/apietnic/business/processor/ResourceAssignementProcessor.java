package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.DatedData;
import com.sciforma.psnext.api.DoubleDatedData;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.ResAssignment;
import com.sciforma.psnext.api.Task;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class ResourceAssignementProcessor extends AbstractProcessor<ResAssignment> {

    @Value("${filename.resAssignements}")
    private String filename;

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

    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        csvLines = new ArrayList<>();
    }

    @Override
    protected String getFilename() {
        return filename;
    }

    protected void process(Task task, Date start, Date end) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            List<ResAssignment> resAssignmentList = task.getResAssignmentList();

            LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (ResAssignment resAssignment : resAssignmentList) {

                for (LocalDate localDate = startDate; localDate.isBefore(endDate); localDate = localDate.plusDays(1)) {

                    Map<String, String> header = new HashMap<>();

                    for (String headerItem : getHeaderAsList()) {
                        header.put(headerItem, null);
                    }

                    for (SciformaField sciformaField : getFieldsToExtract()) {

                        if (sciformaField.getType().equals(FieldType.EFFORT)) {

                            Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                            Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                            List<DoubleDatedData> datedData = resAssignment.getDatedData(sciformaField.getName(), DatedData.DAY, from, to);

                            if (!datedData.isEmpty()) {

                                header.put("Start", sdf.format(datedData.get(0).getStart()));
                                header.put("Finish", sdf.format(datedData.get(0).getFinish()));
                                header.put(sciformaField.getName(), String.valueOf(datedData.get(0).getData()));

                            }

                        } else {

                            extractorMap.get(sciformaField.getType()).extractAsString(resAssignment, sciformaField.getName()).ifPresent(fieldValue -> header.put(sciformaField.getName(), fieldValue));

                        }
                    }

                    StringJoiner csvLine = new StringJoiner(csvDelimiter);

                    for (String headerItem : getHeaderAsList()) {

                        if (header.containsKey(headerItem)) {

                            if (header.get(headerItem) != null) {
                                csvLine.add(header.get(headerItem));
                            } else {
                                csvLine.add("");
                            }

                        }

                    }

                    csvLines.add(csvLine.toString());

                }

            }

        } catch (PSException e) {

            Logger.error(e, "Failed to retrieve resource assignement for task");

        }

    }

    @Override
    void toCsv() {
        String filePath = path + getFilename();

        try (FileWriter fileWriter = new FileWriter(filePath)) {

            StringJoiner header = new StringJoiner(csvDelimiter);

            for (String headerItem : getHeaderAsList()) {
                header.add(headerItem);
            }

            fileWriter.append(header.toString()).append("\n");

            for (String csvLine : csvLines) {
                fileWriter.append(csvLine).append("\n");
            }

            fileWriter.flush();

        } catch (IOException e) {
            Logger.error(e, "Failed to create file with path " + filePath);
        }
    }

    private List<String> getHeaderAsList() {
        List<String> header = new ArrayList<>();

        header.add("Start");
        header.add("Finish");

        for (SciformaField sciformaField : getFieldsToExtract()) {
            header.add(sciformaField.getName());
        }

        return header;
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
