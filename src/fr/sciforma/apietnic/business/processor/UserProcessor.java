package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.StringJoiner;

@Component
public class UserProcessor extends AbstractProcessor<User> {

    @Value("${filename.users}")
    protected String filename;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + filename);

        fieldsToExtract = extractorFactory.getFields();
        csvLines = new ArrayList<>();

        for (User user : sciformaService.getUsers()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : fieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(user, sciformaField.getName()).ifPresent(csvLine::add);

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
