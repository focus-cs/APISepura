package fr.sciforma.apietnic.business.processor;

import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created on 12/03/19.
 */
public abstract class AbstractSystemDataProcessor<T> extends AbstractProcessor<T> {

    protected abstract Optional<T> getFieldAccessors(SciformaService sciformaService);
    protected abstract List<T> getChildren(T fieldAccessor);

    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + getFilename());

        Optional<T> skills = getFieldAccessors(sciformaService);

        skills.ifPresent(this::parse);

        Logger.info("File " + getFilename() + " has been processed successfully");

    }

    private void parse(T root) {

        List<T> children = getChildren(root);

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : getFieldsToExtract()) {

                Optional<String> value = extractorMap.get(sciformaField.getType()).extractAsString(root, sciformaField.getName());
                if (value.isPresent()) {
                    csvLine.add(value.get());
                } else {
                    csvLine.add("");
                }

            }

            csvHelper.addLine(csvLine.toString());

        } else {

            for (T child : children) {

                parse(child);

            }
        }

    }

}
