package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.PortfolioFolder;
import com.sciforma.psnext.api.Skill;
import fr.sciforma.apietnic.business.csv.PortfolioFolderCsvHelper;
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

    private Map<String, Integer> portfolioFolderByName;

    public Map<String, Integer> getPortfoliosByName(SciformaService sciformaService, Map<String, Integer> usersByName) {

        Logger.info("Processing file " + getCsvHelper().getFilename());

        portfolioFolderByName = new HashMap<>();

        Optional<PortfolioFolder> fieldAccessors = getFieldAccessors(sciformaService);

        fieldAccessors.ifPresent(portfolioFolder -> parse(portfolioFolder, usersByName));

        getCsvHelper().flush();

        Logger.info("File " + getCsvHelper().getFilename() + " has been processed successfully");

        return portfolioFolderByName;

    }

    private void parse(PortfolioFolder root, Map<String, Integer> usersByName) {

        if (root.getParent() != null) {
            getCsvHelper().addLine(buildCsvLine(root, usersByName));

            try {
                portfolioFolderByName.putIfAbsent(root.toString(), Double.valueOf(root.getDoubleField("Internal ID")).intValue());
            } catch (PSException e) {
                Logger.error(e);
            }
        }

        List<PortfolioFolder> children = getChildren(root);

        if (children != null && !children.isEmpty()) {

            for (PortfolioFolder child : children) {

                parse(child, usersByName);

            }

        }

    }

    String buildCsvLine(PortfolioFolder fieldAccessor, Map<String, Integer> usersByName) {
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
                            managersIds.add(String.valueOf(usersByName.get(manager)));
                        }

                    }

                    value = Optional.of(managersIds.toString());

                }

            } else {

                value = extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName());

            }

            if (value.isPresent()) {
                csvLine.add(value.get());
            } else {
                csvLine.add("");
            }

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
