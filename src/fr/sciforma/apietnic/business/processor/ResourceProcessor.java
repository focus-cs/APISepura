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
import java.util.Optional;

@Component
@Getter
public class ResourceProcessor extends AbstractFieldAccessorProcessor<Resource> {

    @Autowired
    private ResourceFieldProvider fieldProvider;
    @Autowired
    private ResourceCsvHelper csvHelper;

    public Map<Double, Resource> getResourcesById(SciformaService sciformaService) {

        Map<Double, Resource> resourcesById = new HashMap<>();

        int cpt = 0;

        for (Resource resource : getFieldAccessors(sciformaService)) {

            Optional<Double> internalId = (Optional<Double>) extractorMap.get(FieldType.DECIMAL).extract(resource, "Internal ID");

            internalId.ifPresent(aDouble -> resourcesById.putIfAbsent(aDouble, resource));

//            cpt++;
//            if (cpt > 5) {
//                break;
//            }

        }

        return resourcesById;
    }

    @Override
    protected List<Resource> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getWorkingResources();
    }

}
