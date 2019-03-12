package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PortfolioFolder;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PortfolioFolderProcessor extends AbstractProcessor<PortfolioFolder> {

    @Value("${filename.portfolioFolders}")
    private String filename;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + filename);

        fieldsToExtract = extractorFactory.getFields();

        csvLines = new ArrayList<>();

        Optional<PortfolioFolder> portfolioFolders = sciformaService.getPortfolioFolders();

        portfolioFolders.ifPresent(this::parsePortfolioFolders);

        toCsv();

        Logger.info("File " + filename + " has been processed successfully");

    }

    @Override
    protected String getFilename() {
        return filename;
    }

    private void parsePortfolioFolders(PortfolioFolder root) {

        List<PortfolioFolder> children = root.getChildren();

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : fieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(root, sciformaField.getName()).ifPresent(csvLine::add);

            }

            csvLines.add(csvLine.toString());

        } else {

            for (PortfolioFolder child : children) {

                parsePortfolioFolders(child);

            }
        }

    }
}
