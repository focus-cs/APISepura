package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Organization;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.OrganizationExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class OrganizationProcessor extends AbstractProcessor {

    @Autowired
    OrganizationExtractorFactory extractorFactory;

    private Map<FieldType, Extractor<Organization>> extractorMap = new EnumMap<>(FieldType.class);
    private List<SciformaField> userFieldsToExtract;

    @PostConstruct
    public void postConstruct() {
        extractorMap.putIfAbsent(FieldType.STRING, stringExtractor);
        extractorMap.putIfAbsent(FieldType.DECIMAL, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.BOOLEAN, booleanExtractor);
        extractorMap.putIfAbsent(FieldType.COST, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.EFFORT, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.DATE, dateExtractor);
        extractorMap.putIfAbsent(FieldType.FORMULA, stringExtractor);
        extractorMap.putIfAbsent(FieldType.DURATION, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.INTEGER, integerExtractor);
        extractorMap.putIfAbsent(FieldType.USER, stringExtractor);
        extractorMap.putIfAbsent(FieldType.RESOURCE, stringExtractor);
        extractorMap.putIfAbsent(FieldType.URL, stringExtractor);
        extractorMap.putIfAbsent(FieldType.CALENDAR, stringExtractor);
        extractorMap.putIfAbsent(FieldType.EFFORT_RATE, stringExtractor);
        extractorMap.putIfAbsent(FieldType.LIST, listExtractor);
    }

    @Override
    public void process(SciformaService sciformaService) {

        userFieldsToExtract = extractorFactory.getFields();

        Optional<Organization> organizations = sciformaService.getOrganizations();

        organizations.ifPresent(this::parseOrganizations);

    }

    private void parseOrganizations(Organization root) {

        List<Organization> children = root.getChildren();

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : userFieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(root, sciformaField.getName()).ifPresent(csvLine::add);

            }

            Logger.info(csvLine.toString());

        } else {

            for (Organization child : children) {

                parseOrganizations(child);

            }
        }

    }
}
