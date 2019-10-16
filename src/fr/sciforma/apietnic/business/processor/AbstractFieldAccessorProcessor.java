package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.FieldAccessor;
import fr.sciforma.apietnic.service.SciformaService;

import java.util.List;

/**
 * Created on 12/03/19.
 */
public abstract class AbstractFieldAccessorProcessor<T extends FieldAccessor> extends AbstractProcessor<T> {

    protected abstract List<T> getFieldAccessors(SciformaService sciformaService);

}
