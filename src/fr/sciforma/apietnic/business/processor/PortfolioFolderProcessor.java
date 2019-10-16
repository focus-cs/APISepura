package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PortfolioFolder;
import fr.sciforma.apietnic.business.csv.PortfolioFolderCsvHelper;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.PortfolioFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
@Getter
public class PortfolioFolderProcessor extends AbstractSystemDataProcessor<PortfolioFolder> {

    @Value("${multivalue.delimiter}")
    protected String delimiter;

    @Autowired
    private PortfolioFieldProvider fieldProvider;
    @Autowired
    private PortfolioFolderCsvHelper csvHelper;

    private Map<String, String> portfolioFolderByName;

    public Map<String, String> getPortfoliosByName(SciformaService sciformaService, Map<String, String> usersByName) {

        Logger.info("Processing file " + getCsvHelper().getFilename());

        portfolioFolderByName = new HashMap<>();

        Optional<PortfolioFolder> fieldAccessors = getFieldAccessors(sciformaService);

        fieldAccessors.ifPresent(portfolioFolder -> parse(portfolioFolder, usersByName));

        getCsvHelper().flush();

        Logger.info("File " + getCsvHelper().getFilename() + " has been processed successfully");

        return portfolioFolderByName;

    }

    private void parse(PortfolioFolder root, Map<String, String> usersByName) {

        if (root.getParent() != null) {
            getCsvHelper().addLine(buildCsvLine(root, usersByName));

            Optional<String> organizationIID = extractorMap.get(FieldType.DECIMAL_NO_PRECISION).extractAsString(root, "Internal ID");
            organizationIID.ifPresent(s -> portfolioFolderByName.putIfAbsent(root.toString(), s));
        }

        List<PortfolioFolder> children = getChildren(root);

        if (children != null && !children.isEmpty()) {

            for (PortfolioFolder child : children) {

                parse(child, usersByName);

            }

        }

    }

    String buildCsvLine(PortfolioFolder fieldAccessor, Map<String, String> usersByName) {
        StringJoiner csvLine = new StringJoiner(csvDelimiter);

        for (SciformaField sciformaField : getFieldProvider().getFields()) {

            Optional<String> value = Optional.empty();

            if (sciformaField.getName().equals("Managers")) {

                Optional<?> extract = extractorMap.get(sciformaField.getType()).extract(fieldAccessor, sciformaField.getName());

                if (extract.isPresent()) {

                    List<String> managers = (List<String>) extract.get();

                    StringJoiner managersIds = new StringJoiner(delimiter);

                    for (String manager : managers) {

                        if(usersByName.containsKey(manager)) {
                            managersIds.add(usersByName.get(manager));
                        }

                    }

                    value = Optional.of(managersIds.toString());

                }

            } else {

                value = extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName());

            }

            csvLine.add(value.orElse(""));

        }
        return csvLine.toString();
    }

    @Override
    protected Optional<PortfolioFolder> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getPortfolioFolders();
    }

    @Override
    protected List<PortfolioFolder> getChildren(PortfolioFolder fieldAccessor) {
        return fieldAccessor.getChildren();
    }

}
