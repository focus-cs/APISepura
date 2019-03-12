package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceProcessor extends AbstractFieldAccessorProcessor<Resource> {

    @Value("${filename.resources}")
    private String filename;

    @Override
    protected String getFilename() {
        return filename;
    }

    @Override
    protected List<Resource> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getWorkingResources();
    }
}
