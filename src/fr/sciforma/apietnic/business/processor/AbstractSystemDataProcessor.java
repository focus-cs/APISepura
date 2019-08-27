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

        Logger.info("Processing file " + csvHelper.getFilename());

        Optional<T> fieldAccessors = getFieldAccessors(sciformaService);

        fieldAccessors.ifPresent(this::parse);

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

    }

    private void parse(T root) {

        List<T> children = getChildren(root);

        if (children == null || children.isEmpty()) {

            csvHelper.addLine(buildCsvLine(root));

        } else {

            for (T child : children) {

                parse(child);

            }
        }

    }

}
