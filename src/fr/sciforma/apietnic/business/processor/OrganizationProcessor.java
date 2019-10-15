package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Organization;
import fr.sciforma.apietnic.business.csv.OrganizationCsvHelper;
import fr.sciforma.apietnic.business.provider.OrganizationFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Getter
public class OrganizationProcessor extends AbstractSystemDataProcessor<Organization> {

    @Autowired
    private OrganizationFieldProvider fieldProvider;
    @Autowired
    private OrganizationCsvHelper csvHelper;

    private Map<String, Integer> organizationByName;

    public Map<String, Integer> getOrganizationByName(SciformaService sciformaService) {

        Logger.info("Processing file " + getCsvHelper().getFilename());

        organizationByName = new HashMap<>();

        Optional<Organization> fieldAccessors = getFieldAccessors(sciformaService);

        fieldAccessors.ifPresent(this::parse);

        getCsvHelper().flush();

        Logger.info("File " + getCsvHelper().getFilename() + " has been processed successfully");

        return organizationByName;

    }

    private void parse(Organization root) {

        String csvLine = buildCsvLine(root);
        getCsvHelper().addLine(csvLine);

        List<Organization> children = getChildren(root);

        if (children != null && !children.isEmpty()) {

            for (Organization child : children) {

                parse(child);

            }

        }

    }

    @Override
    protected Optional<Organization> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getOrganizations();
    }

    @Override
    protected List<Organization> getChildren(Organization fieldAccessor) {
        return fieldAccessor.getChildren();
    }

}
