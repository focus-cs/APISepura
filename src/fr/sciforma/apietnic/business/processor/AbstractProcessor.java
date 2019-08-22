package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.DatedData;
import com.sciforma.psnext.api.DoubleDatedData;
import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import fr.sciforma.apietnic.business.csv.CsvHelper;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.FieldProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public abstract class AbstractProcessor<T extends FieldAccessor> {

    protected static final String START_HEADER = "**Start**";
    protected static final String FINISH_HEADER = "**Finish**";

    protected SimpleDateFormat sdf;

    @Value("${csv.delimiter}")
    protected String csvDelimiter;
    @Autowired
    FieldProvider<T> fieldProvider;
    @Autowired
    CsvHelper<T> csvHelper;

    Map<FieldType, Extractor<T, ?>> extractorMap = new EnumMap<>(FieldType.class);

    @PostConstruct
    public void postConstruct() {
        extractorMap.putIfAbsent(FieldType.STRING, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.DECIMAL, getDecimalExtractor());
        extractorMap.putIfAbsent(FieldType.BOOLEAN, getBooleanExtractor());
        extractorMap.putIfAbsent(FieldType.COST, getDecimalExtractor());
        extractorMap.putIfAbsent(FieldType.EFFORT, getEffortExtractor());
        extractorMap.putIfAbsent(FieldType.DATE, getDateExtractor());
        extractorMap.putIfAbsent(FieldType.FORMULA, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.DURATION, getDecimalExtractor());
        extractorMap.putIfAbsent(FieldType.INTEGER, getIntegerExtractor());
        extractorMap.putIfAbsent(FieldType.USER, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.RESOURCE, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.URL, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.CALENDAR, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.EFFORT_RATE, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.LIST, getListExtractor());

        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    String buildCsvLine(T fieldAccessor) {
        StringJoiner csvLine = new StringJoiner(csvDelimiter);

        for (SciformaField sciformaField : getFieldsToExtract()) {

            Optional<String> value = extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName());
            if (value.isPresent()) {
                csvLine.add(value.get());
            } else {
                csvLine.add("");
            }

        }
        return csvLine.toString();
    }

    Optional<String> buildTimeDistributedCsvLine(T distributedValue, LocalDate localDate) throws PSException {
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

                    header.put(START_HEADER, sdf.format(datedData.get(0).getStart()));
                    header.put(FINISH_HEADER, sdf.format(datedData.get(0).getFinish()));
                    header.put(sciformaField.getName(), String.valueOf(datedData.get(0).getData()));

                }

            } else {

                extractorMap.get(sciformaField.getType()).extractAsString(distributedValue, sciformaField.getName()).ifPresent(fieldValue -> header.put(sciformaField.getName(), fieldValue));

            }
        }

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

    }

    List<SciformaField> getFieldsToExtract() {
        return fieldProvider.getFields();
    }

    public abstract StringExtractor<T> getStringExtractor();

    public abstract DecimalExtractor<T> getDecimalExtractor();

    public abstract BooleanExtractor<T> getBooleanExtractor();

    public abstract DateExtractor<T> getDateExtractor();

    public abstract IntegerExtractor<T> getIntegerExtractor();

    public abstract ListExtractor<T> getListExtractor();

    public abstract CalendarExtractor<T> getCalendarExtractor();

    public abstract EffortExtractor<T> getEffortExtractor();

}
