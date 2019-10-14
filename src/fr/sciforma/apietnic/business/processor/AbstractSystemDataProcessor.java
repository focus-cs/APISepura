package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.FieldAccessor;
import fr.sciforma.apietnic.business.model.BaseBo;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created on 12/03/19.
 */
public abstract class AbstractSystemDataProcessor<T extends FieldAccessor, B extends BaseBo> extends AbstractProcessor<T> {

    protected abstract Optional<T> getFieldAccessors(SciformaService sciformaService);
    protected abstract List<T> getChildren(T fieldAccessor);
    protected abstract B buildBusinessObject(List<String> fields);

    private Map<Integer, B> boByInternalId = new HashMap<>();

    public Map<Integer, B> process(SciformaService sciformaService) {

        Logger.info("Processing file " + csvHelper.getFilename());

        Optional<T> fieldAccessors = getFieldAccessors(sciformaService);

        fieldAccessors.ifPresent(this::parse);

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

        return boByInternalId;

    }

    private void parse(T root) {

        String csvLine = buildCsvLine(root);
        csvHelper.addLine(csvLine);
        B businessObject = buildBusinessObject(splitCsvLine(csvLine));
        boByInternalId.putIfAbsent(businessObject.getInternalId(), businessObject);

        List<T> children = getChildren(root);

        if (children != null && !children.isEmpty()) {

            for (T child : children) {

                parse(child);

            }

        }

    }

}
