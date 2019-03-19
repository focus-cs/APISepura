package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.User;
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
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class UserProcessor extends AbstractFieldAccessorProcessor<User> {

    @Value("${filename.resources}")
    protected String filename;

    @Autowired
    private StringExtractor<User> stringExtractor;
    @Autowired
    private DecimalExtractor<User> decimalExtractor;
    @Autowired
    private BooleanExtractor<User> booleanExtractor;
    @Autowired
    private DateExtractor<User> dateExtractor;
    @Autowired
    private IntegerExtractor<User> integerExtractor;
    @Autowired
    private ListExtractor<User> listExtractor;
    @Autowired
    private CalendarExtractor<User> calendarExtractor;
    @Autowired
    private EffortExtractor<User> effortExtractor;

    @Autowired
    ResourceProcessor resourceProcessor;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + getFilename());

        Map<Double, String> userById = new HashMap<>();

        StringJoiner csvLine;

        for (User fieldAccessor : getFieldAccessors(sciformaService)) {

            csvLine = new StringJoiner(csvDelimiter);

            Optional<Double> internalId = (Optional<Double>) extractorMap.get(FieldType.DECIMAL).extract(fieldAccessor, "Internal ID");

            if (internalId.isPresent()) {

                for (SciformaField sciformaField : getFieldsToExtract()) {

                    extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName()).ifPresent(csvLine::add);

                }

                userById.putIfAbsent(internalId.get(), csvLine.toString());

            }

        }

        Map<Double, String> resourcesById = resourceProcessor.getResourcesById(sciformaService);

        csvLines = new ArrayList<>();

        for (Map.Entry<Double, String> entry : userById.entrySet()) {

            csvLine = new StringJoiner(csvDelimiter);
            csvLine.add(entry.getValue());

            if(resourcesById.containsKey(entry.getKey())) {
                csvLine.add(resourcesById.get(entry.getKey()));
            }

            csvLines.add(csvLine.toString());

        }

        toCsv();

        Logger.info("File " + getFilename() + " has been processed successfully");
    }

    @Override
    void toCsv() {

        String filePath = path + getFilename();

        try (FileWriter fileWriter = new FileWriter(filePath)) {

            StringJoiner header = new StringJoiner(csvDelimiter);

            // Add header for users
            for (SciformaField sciformaField : getFieldsToExtract()) {
                header.add(sciformaField.getName());
            }

            // Add header for resources
            for (SciformaField sciformaField : resourceProcessor.getFieldsToExtract()) {
                header.add(sciformaField.getName());
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

    @Override
    protected String getFilename() {
        return filename;
    }

    @Override
    protected List<User> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getUsers();
    }

    @Override
    public StringExtractor<User> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<User> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<User> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<User> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<User> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<User> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<User> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<User> getEffortExtractor() {
        return effortExtractor;
    }

}
