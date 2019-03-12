package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Organization;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrganizationProcessor extends AbstractSystemDataProcessor<Organization> {

    @Value("${filename.organizations}")
    private String filename;

    @Override
    protected Optional<Organization> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getOrganizations();
    }

    @Override
    protected List<Organization> getChildren(Organization fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    protected String getFilename() {
        return filename;
    }
}
