package fr.sciforma.apietnic.business.processor;

import fr.sciforma.apietnic.business.csv.CsvHelper;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.FieldProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProcessor<T> {

    @Value("${csv.delimiter}")
    protected String csvDelimiter;
    @Autowired
    FieldProvider<T> fieldProvider;
    @Autowired
    CsvHelper<T> csvHelper;

    Map<FieldType, Extractor<? super T, ?>> extractorMap = new EnumMap<>(FieldType.class);

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
    }

    List<SciformaField> getFieldsToExtract() {
        return fieldProvider.getFields();
    }
    public abstract StringExtractor<? super T> getStringExtractor();
    public abstract DecimalExtractor<? super T> getDecimalExtractor();
    public abstract BooleanExtractor<? super T> getBooleanExtractor();
    public abstract DateExtractor<? super T> getDateExtractor();
    public abstract IntegerExtractor<? super T> getIntegerExtractor();
    public abstract ListExtractor<? super T> getListExtractor();
    public abstract CalendarExtractor<? super T> getCalendarExtractor();
    public abstract EffortExtractor<? super T> getEffortExtractor();

}
