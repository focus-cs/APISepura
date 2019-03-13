package fr.sciforma.apietnic.business.processor;

import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.factory.ExtractorFactory;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class AbstractProcessor<T> {

    @Value("${csv.delimiter}")
    protected String csvDelimiter;
    @Value("${csv.path}")
    protected String path;
    @Autowired
    ExtractorFactory<T> extractorFactory;

    Map<FieldType, Extractor<? super T, ?>> extractorMap = new EnumMap<>(FieldType.class);

    List<SciformaField> fieldsToExtract;
    List<String> csvLines;

    protected abstract String getFilename();
    protected abstract void process(SciformaService sciformaService);

    @PostConstruct
    public void postConstruct() {
        extractorMap.putIfAbsent(FieldType.STRING, getStringExtractor());
        extractorMap.putIfAbsent(FieldType.DECIMAL, getDecimalExtractor());
        extractorMap.putIfAbsent(FieldType.BOOLEAN, getBooleanExtractor());
        extractorMap.putIfAbsent(FieldType.COST, getDecimalExtractor());
        extractorMap.putIfAbsent(FieldType.EFFORT, getDecimalExtractor());
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
    }

    void toCsv() {
        String filePath = path + getFilename();

        try (FileWriter fileWriter = new FileWriter(filePath)) {

            StringJoiner header = new StringJoiner(csvDelimiter);

            for (SciformaField field : fieldsToExtract) {
                header.add(field.getName());
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

    public abstract StringExtractor<? super T> getStringExtractor();
    public abstract DecimalExtractor<? super T> getDecimalExtractor();
    public abstract BooleanExtractor<? super T> getBooleanExtractor();
    public abstract DateExtractor<? super T> getDateExtractor();
    public abstract IntegerExtractor<? super T> getIntegerExtractor();
    public abstract ListExtractor<? super T> getListExtractor();
    public abstract CalendarExtractor<? super T> getCalendarExtractor();

}
