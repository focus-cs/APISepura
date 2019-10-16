package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import fr.sciforma.apietnic.business.csv.ResourceCsvHelper;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.provider.ResourceFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class ResourceProcessor extends AbstractFieldAccessorProcessor<Resource> {

    @Autowired
    private ResourceFieldProvider fieldProvider;
    @Autowired
    private ResourceCsvHelper csvHelper;

    public Map<String, Resource> getResourcesById(SciformaService sciformaService) {

        Map<String, Resource> resourcesById = new HashMap<>();

        for (Resource resource : getFieldAccessors(sciformaService)) {

            extractorMap.get(FieldType.DECIMAL_NO_PRECISION).extractAsString(resource, "Internal ID").ifPresent(iid -> resourcesById.putIfAbsent(iid, resource));

        }

        return resourcesById;
    }

    @Override
    protected List<Resource> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getWorkingResources();
    }

}
