package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Organization;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.factory.OrganizationExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class OrganizationProcessor extends AbstractProcessor<Organization> {

    @Value("${filename.organizations}")
    private String filename;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + filename);

        fieldsToExtract = extractorFactory.getFields();
        csvLines = new ArrayList<>();

        Optional<Organization> organizations = sciformaService.getOrganizations();

        organizations.ifPresent(this::parseOrganizations);

        toCsv();

        Logger.info("File " + filename + " has been processed successfully");

    }

    @Override
    protected String getFilename() {
        return filename;
    }

    private void parseOrganizations(Organization root) {

        List<Organization> children = root.getChildren();

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : fieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(root, sciformaField.getName()).ifPresent(csvLine::add);

            }

            csvLines.add(csvLine.toString());

        } else {

            for (Organization child : children) {

                parseOrganizations(child);

            }
        }

    }
}
