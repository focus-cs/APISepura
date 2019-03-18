package fr.sciforma.apietnic.business.processor;

import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created on 12/03/19.
 */
public abstract class AbstractFieldAccessorProcessor<T> extends AbstractProcessor<T> {

    protected abstract List<T> getFieldAccessors(SciformaService sciformaService);

    public void process(SciformaService sciformaService) {
        Logger.info("Processing file " + getFilename());

        csvLines = new ArrayList<>();

        for (T fieldAccessor : getFieldAccessors(sciformaService)) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : getFieldsToExtract()) {

                extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName()).ifPresent(csvLine::add);

            }

            csvLines.add(csvLine.toString());
        }

        toCsv();

        Logger.info("File " + getFilename() + " has been processed successfully");
    }

}
