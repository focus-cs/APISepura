package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.JobClassification;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class JobClassificationProcessor extends AbstractProcessor<JobClassification> {

    @Value("${filename.jobClassifications}")
    private String filename;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + filename);
        fieldsToExtract = extractorFactory.getFields();

        csvLines = new ArrayList<>();

        Optional<JobClassification> jobClassifications = sciformaService.getJobClassifications();

        jobClassifications.ifPresent(this::parseJobClassifications);

        toCsv();

        Logger.info("File " + filename + " has been processed successfully");

    }

    @Override
    protected String getFilename() {
        return filename;
    }

    private void parseJobClassifications(JobClassification root) {

        List<JobClassification> children = root.getChildren();

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : fieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(root, sciformaField.getName()).ifPresent(csvLine::add);

            }

            csvLines.add(csvLine.toString());

        } else {

            for (JobClassification child : children) {

                parseJobClassifications(child);

            }
        }

    }
}
