package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.ResourceExtractorFactory;
import fr.sciforma.apietnic.business.factory.UserExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ResourceProcessor extends AbstractProcessor<Resource> {

    @Value("${filename.resources}")
    private String filename;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + filename);

        fieldsToExtract = extractorFactory.getFields();

        csvLines = new ArrayList<>();

        for (Resource resource : sciformaService.getWorkingResources()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : fieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(resource, sciformaField.getName()).ifPresent(csvLine::add);

            }

            csvLines.add(csvLine.toString());
        }

        toCsv();

        Logger.info("File " + filename + " has been processed successfully");

    }

    @Override
    protected String getFilename() {
        return filename;
    }
}
