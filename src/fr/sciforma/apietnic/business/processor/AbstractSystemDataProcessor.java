package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.FieldAccessor;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Created on 12/03/19.
 */
public abstract class AbstractSystemDataProcessor<T extends FieldAccessor> extends AbstractProcessor<T> {

    protected abstract Optional<T> getFieldAccessors(SciformaService sciformaService);
    protected abstract List<T> getChildren(T fieldAccessor);

    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + getCsvHelper().getFilename());

        Optional<T> fieldAccessors = getFieldAccessors(sciformaService);

        fieldAccessors.ifPresent(this::parse);

        getCsvHelper().flush();

        Logger.info("File " + getCsvHelper().getFilename() + " has been processed successfully");

    }

    private void parse(T root) {

        String csvLine = buildCsvLine(root);
        getCsvHelper().addLine(csvLine);

        List<T> children = getChildren(root);

        if (children != null && !children.isEmpty()) {

            for (T child : children) {

                parse(child);

            }

        }

    }

}
