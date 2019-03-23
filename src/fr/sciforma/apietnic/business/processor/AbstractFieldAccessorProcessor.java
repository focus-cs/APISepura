package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.FieldAccessor;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;

import java.util.List;

/**
 * Created on 12/03/19.
 */
public abstract class AbstractFieldAccessorProcessor<T extends FieldAccessor> extends AbstractProcessor<T> {

    protected abstract List<T> getFieldAccessors(SciformaService sciformaService);

    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + csvHelper.getFilename());

        for (T fieldAccessor : getFieldAccessors(sciformaService)) {

            csvHelper.addLine(buildCsvLine(fieldAccessor));

        }

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");
    }

}
